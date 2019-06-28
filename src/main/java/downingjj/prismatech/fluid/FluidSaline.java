package downingjj.prismatech.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidSaline extends Fluid {
    public FluidSaline(){
        super("saline", new ResourceLocation("minecraft:blocks/water_still"), new ResourceLocation("minecraft:blocks/water_flow"));
        FluidRegistry.addBucketForFluid(this);
    }
}
