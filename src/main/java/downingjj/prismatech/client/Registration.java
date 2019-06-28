package downingjj.prismatech.client;

import downingjj.prismatech.ModBlocks;
import downingjj.prismatech.ModItems;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class Registration {
    @SubscribeEvent
    public static void registerItemModels(ModelRegistryEvent event) {
        for (Item i: ModItems.getAll()){
            ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "normal"));
        }
        for (Block b: ModBlocks.getAll()) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b),0, new ModelResourceLocation(b.getRegistryName(), "inventory"));
        }
    }
}
