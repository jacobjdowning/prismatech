package downingjj.prismatech.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidSolute extends FluidDNAHolder {
    public FluidSolute(){
        super("solute", new ResourceLocation("minecraft:blocks/water_still"), new ResourceLocation("minecraft:blocks/water_flow"));
        FluidRegistry.addBucketForFluid(this);
    }
}
