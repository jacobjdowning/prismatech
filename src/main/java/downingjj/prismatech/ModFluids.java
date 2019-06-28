package downingjj.prismatech;

import downingjj.prismatech.fluid.*;
import net.minecraftforge.fluids.Fluid;

import java.util.Collection;
import java.util.HashMap;

public class ModFluids {
    private static HashMap<String, Fluid> fluids;

    public static void createFluidMap(){
        fluids = new HashMap<String, Fluid>();

        //all mod fluid for registration
        add(new FluidSucrose());
        add(new FluidBioOoze());
        add(new FluidSteam());
        add(new FluidSaline());
        add(new FluidSolute());
    }

    private static void add(Fluid fluid){
        fluid.setUnlocalizedName(Prismatech.MOD_NAME + "." + fluid.getName());

        fluids.put(fluid.getName(), fluid);
    }

    public static Fluid get(String name){
        return fluids.get(name);
    }

    public static Collection<Fluid> getAll(){
        return fluids.values();
    }
}
