package downingjj.prismatech.tile;

import downingjj.prismatech.block.BlockInfuser;
import downingjj.prismatech.capability.EnergyStorageSeriealizable;
import downingjj.prismatech.crafting.infuser.InfuserRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static downingjj.prismatech.Prismatech.logger;


public class TileInfuser extends TileEntity implements ICapabilityProvider, ITickable {
    private final int ENERGY_MAX = 100;

    protected FluidTank tank;
    protected ItemStackHandler container;
    protected EnergyStorageSeriealizable battery;
    protected InfuserRecipe currentRecipe;
    protected int progress;
    protected boolean infusing = false;

    public TileInfuser(){
        this.tank = new FluidTank(Fluid.BUCKET_VOLUME * 2){
            @Override
            protected void onContentsChanged(){
                super.onContentsChanged();
                checkStartCrafting();
                markDirty();
            }
        };
        this.tank.setTileEntity(this);

        this.container = new ItemStackHandler(){
            @Override
            protected void onContentsChanged(int slot){
                super.onContentsChanged(slot);
                checkStartCrafting();
                markDirty();
            }
        };

        this.battery = new EnergyStorageSeriealizable(ENERGY_MAX){
            @Override
            protected void onContentsChanged(){
                super.onContentsChanged();
                markDirty();
            }
        };


    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        tank.readFromNBT(tag);
        NBTBase batteryCompound = tag.getTag("battery");
        if(batteryCompound instanceof NBTTagCompound){
            battery.readFromNBT((NBTTagCompound)batteryCompound);
        }
        NBTBase containerCompound = tag.getTag("container");
        if(containerCompound instanceof NBTTagCompound) {
            container.deserializeNBT((NBTTagCompound) tag.getTag("container"));
        }
        this.progress = tag.getInteger("progress");
        this.infusing = tag.getBoolean("infusing");
        checkStartCrafting();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag = super.writeToNBT(tag);
        tank.writeToNBT(tag);
        tag.setTag("battery", battery.writeToNBT());
        tag.setTag("container", container.serializeNBT());
        tag.setInteger("progress", this.progress);
        tag.setBoolean("infusing", this.infusing);
        return tag;
    }

    @Override
    public NBTTagCompound getUpdateTag(){
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket(){
        NBTTagCompound updateTag = getUpdateTag();
        return new SPacketUpdateTileEntity(this.getPos(), 1, updateTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet){
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate){
        return !(newSate.getBlock() instanceof BlockInfuser);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ||
                capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                capability == CapabilityEnergy.ENERGY ||
                super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) tank;
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) container;
        if (capability == CapabilityEnergy.ENERGY)
            return (T) battery;
        return super.getCapability(capability, facing);
    }

    public ItemStack insertItem(ItemStack stack){
        return ItemHandlerHelper.insertItem(container, stack, false);
    }

    public ItemStack removeItem(){
        return container.extractItem(0, 64, false);
    }

    public int getFluidLevel() {
        return tank.getFluidAmount();
    }

    public int getEnergyLevel() { return battery.getEnergyStored(); }

    public boolean isInfusing(){return this.infusing;}

    private void setInfusing(boolean infusing){
        if(infusing != this.infusing){
            this.infusing = infusing;
            this.getWorld().setBlockState(this.pos,
                    this.blockType.getDefaultState().withProperty(BlockInfuser.INFUSING, this.infusing),
                    2);

        }
    }

    private void setCurrentRecipe(InfuserRecipe newRecipe) {
        if (newRecipe != this.currentRecipe){
            this.currentRecipe = newRecipe;
            this.progress = 0;
        }
    }

    private InfuserRecipe.InfuserCraftInventory getInvFromHandlers(){
        return new InfuserRecipe.InfuserCraftInventory(
                container.getStackInSlot(0),
                tank.getFluid());
    }

    private void checkStartCrafting(){
        InfuserRecipe.InfuserCraftInventory inv = getInvFromHandlers();
        setCurrentRecipe((InfuserRecipe)(CraftingManager.findMatchingRecipe(inv, this.getWorld())));
    }

    @Override
    public void update() {
        if(!(this.getWorld().isRemote)) {
            if (currentRecipe != null) {
                if (progress >= this.currentRecipe.getTimeUsage()) {
                    InfuserRecipe recipe = this.currentRecipe;
                    InfuserRecipe.InfuserCraftInventory inv = getInvFromHandlers();
                    this.container.setStackInSlot(0, recipe.getCraftingResult(inv));
                    this.tank.setFluid(recipe.getFluidResult(inv));
                    progress = 0;
                    this.markDirty();
                    setInfusing(false);
                    return;
                }
                if (battery.getEnergyStored() >= currentRecipe.getEnergyCost()) {
                    battery.extractEnergy(currentRecipe.getEnergyCost(), false);
                    progress += 1;
                    setInfusing(true);
                } else {
                    setInfusing(false);
                }
            }else{
                setInfusing(false);
            }
        }
    }

    public String infoString() {
        return "Energy: " + getEnergyLevel() +
                ", FluidLevel: " + getFluidLevel() +
                ", FluidName: " + (tank.getFluid() != null ? tank.getFluid().getFluid().getName() : "") +
                ", ItemName: " + (container.getStackInSlot(0) != null ? container.getStackInSlot(0).getDisplayName() : "") +
                ", ItemCount: " + container.getStackInSlot(0).getCount() +
                ", Progress: " + this.progress;
    }
}
