package downingjj.prismatech.fluid;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.MinecraftDummyContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.awt.*;

//Not 100% sold on this implementation
public class FluidDNAHolder extends Fluid {
    public FluidDNAHolder(String fluidName, ResourceLocation still, ResourceLocation flowing, Color color) {
        super(fluidName, still, flowing, color);
    }

    public FluidDNAHolder(String fluidName, ResourceLocation still, ResourceLocation flowing, @Nullable ResourceLocation overlay, Color color) {
        super(fluidName, still, flowing, overlay, color);
    }

    public FluidDNAHolder(String fluidName, ResourceLocation still, ResourceLocation flowing, int color) {
        super(fluidName, still, flowing, color);
    }

    public FluidDNAHolder(String fluidName, ResourceLocation still, ResourceLocation flowing, @Nullable ResourceLocation overlay, int color) {
        super(fluidName, still, flowing, overlay, color);
    }

    public FluidDNAHolder(String fluidName, ResourceLocation still, ResourceLocation flowing) {
        super(fluidName, still, flowing);
    }

    public FluidDNAHolder(String fluidName, ResourceLocation still, ResourceLocation flowing, @Nullable ResourceLocation overlay) {
        super(fluidName, still, flowing, overlay);
    }

    public static FluidStack writeDNAtoFluid(ItemStack dna, FluidStack fluid){
        if(dna == null || dna.isEmpty() || !(fluid.getFluid() instanceof FluidDNAHolder))
            return fluid;
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("id", Item.REGISTRY.getNameForObject(dna.getItem()).toString());
        tag.setInteger("meta", dna.getItemDamage());
        if(fluid.tag == null)
            fluid.tag = new NBTTagCompound();
        fluid.tag.setTag("dna", tag);
        return fluid;
    }

    public ItemStack getItemstackFromDNA(FluidStack fStack){
        NBTTagCompound dna = fStack.tag.getCompoundTag("dna");
        return GameRegistry.makeItemStack(dna.getString("id"), dna.getInteger("meta"), 1, null);
    }

    @Override
    public String getLocalizedName(FluidStack stack) {
        String fluidName = super.getLocalizedName(stack);
        if(stack.tag == null || !stack.tag.hasKey("dna")){
            return fluidName;
        }
        ItemStack dna = getItemstackFromDNA(stack);
        String dnaName = dna.getUnlocalizedName();
        //Not 100% sure getLocalizedName doesn't run on client.
        dnaName = I18n.format(dna.getUnlocalizedName());
        return String.format("%s(%s DNA)", fluidName, dna);
    }
}
