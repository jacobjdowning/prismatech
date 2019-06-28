package downingjj.prismatech.block;

import downingjj.prismatech.tile.TileInfuser;
import downingjj.prismatech.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

public class BlockInfuser extends Block {
    public static final PropertyBool INFUSING = PropertyBool.create("infusing");
    public BlockInfuser() {
        super(Material.IRON);

        this.setHardness(1.5f);
        setDefaultState(this.getBlockState().getBaseState().withProperty(INFUSING, false));
    }

    @Override
    public boolean hasTileEntity(IBlockState state){
        return true;
    }

    @Override
    public TileEntity createTileEntity(@Nullable World world, IBlockState state){
        return new TileInfuser();
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, INFUSING);
    }

    @Override
    public int getMetaFromState(IBlockState state){
        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos){
        return state.withProperty(INFUSING, Util.getTEfromPos(worldIn, pos, TileInfuser.class).isInfusing());
    }

    @Override
    public boolean onBlockActivated(World worldIn,
                                    BlockPos pos,
                                    IBlockState state,
                                    EntityPlayer playerIn,
                                    EnumHand hand,
                                    EnumFacing facing,
                                    float hitX,
                                    float hitY,
                                    float hitZ) {
        if(!worldIn.isRemote) {
            ItemStack stackInHand = playerIn.getHeldItem(hand);
            TileInfuser tile = Util.getTEfromPos(worldIn, pos, TileInfuser.class);
            if (stackInHand.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, facing);
            } else if (!worldIn.isRemote && playerIn.isSneaking()) {
                playerIn.sendStatusMessage(new TextComponentString(tile.infoString()), false);
            } else if (!stackInHand.isEmpty()) {
                playerIn.setHeldItem(hand, tile.insertItem(stackInHand));
                playerIn.inventoryContainer.detectAndSendChanges();
            } else {
                playerIn.setHeldItem(hand, tile.removeItem());
                playerIn.inventoryContainer.detectAndSendChanges();
            }
        }
        return true;
    }
}
