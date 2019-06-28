package downingjj.prismatech;

import downingjj.prismatech.util.IOreDictionaried;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import static downingjj.prismatech.Prismatech.logger;


@Mod.EventBusSubscriber
public class Registration {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event){
       for(Block b : ModBlocks.getAll()) {
           event.getRegistry().register(b);
           if (b instanceof IOreDictionaried){
               OreDictionary.registerOre(((IOreDictionaried) b).getDictionaryName(), b);
           }
           if (b.hasTileEntity(b.getDefaultState())) {
               GameRegistry.registerTileEntity(b.createTileEntity(null,
                       b.getDefaultState()).getClass(),
                       b.getRegistryName());
           }
       }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        for(Item i : ModItems.getAll()){
            event.getRegistry().register(i);
            if (i instanceof IOreDictionaried){
                OreDictionary.registerOre(((IOreDictionaried) i).getDictionaryName(), i);
            }
        }
        for(Block b : ModBlocks.getAll()){
            event.getRegistry().register(new ItemBlock(b).setRegistryName(b.getRegistryName()));
        }
    }

    public static void registerFluids(){
        for(Fluid f : ModFluids.getAll()){
            FluidRegistry.registerFluid(f);
        }
    }
}
