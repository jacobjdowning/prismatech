package downingjj.prismatech;


import downingjj.prismatech.capability.dna.CapabilityDNA;
import downingjj.prismatech.proxy.IProxy;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Prismatech.MOD_ID, version = Prismatech.VERSION, name = Prismatech.MOD_NAME, useMetadata = true, acceptedMinecraftVersions="[1.12,1.13)")
public class Prismatech {
    public static final String MOD_ID = "prismatech";
    public static final String VERSION = "0.1";
    public static final String  MOD_NAME = "Prismatech";

    @Mod.Instance
    public static Prismatech instance;

    @SidedProxy(clientSide = "downingjj.prismatech.proxy.ClientProxy", serverSide = "downingjj.prismatech.proxy.ServerProxy")
    public static IProxy proxy;

    public static Logger logger;

    public Prismatech(){
        //Enabling Universal Bucket as per Forge Fluid
        FluidRegistry.enableUniversalBucket();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        //Register Capabilities
//        CapabilityDNA.register();

        //Register blocks, items, and fluids
        ModBlocks.createBlockMap();
        ModItems.createItemMap();
        ModFluids.createFluidMap();
        Registration.registerFluids();

        //expose logger
        logger = event.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
    }
}