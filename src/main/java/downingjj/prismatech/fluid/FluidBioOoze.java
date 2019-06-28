package downingjj.prismatech.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidBioOoze extends FluidDNAHolder {
    public FluidBioOoze(){
        super("bio_ooze", new ResourceLocation("minecraft:blocks/water_still"), new ResourceLocation("minecraft:blocks/water_flow"));
        FluidRegistry.addBucketForFluid(this);
    }
}
