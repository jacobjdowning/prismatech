package downingjj.prismatech;

import downingjj.prismatech.block.BlockCreativeGen;
import downingjj.prismatech.block.BlockInfuser;
import net.minecraft.block.Block;

import java.util.Collection;
import java.util.HashMap;

public class ModBlocks {
    private static HashMap<String, Block> blocks;

    public static void createBlockMap(){
        blocks = new HashMap<String, Block>();

        //all mod blocks for registration
        add("infuser", new BlockInfuser());
        add("creative_gen", new BlockCreativeGen());
    }

    private static void add(String name, Block block){
        block.setRegistryName(name);
        block.setUnlocalizedName(Prismatech.MOD_NAME + "." + name);

        blocks.put(name, block);
    }

    public static Block get(String name){
        return blocks.get(name);
    }

    public static Collection<Block> getAll(){
        return blocks.values();
    }
}
