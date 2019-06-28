package downingjj.prismatech.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

//Named by DirtyFerdy82
public class FluidSucrose extends Fluid {
    public FluidSucrose(){
        super("sucrose", new ResourceLocation("minecraft:blocks/water_still"), new ResourceLocation("minecraft:blocks/water_flow"));
        FluidRegistry.addBucketForFluid(this);
    }
}
