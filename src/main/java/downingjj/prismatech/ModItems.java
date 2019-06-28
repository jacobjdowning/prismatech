package downingjj.prismatech;

import downingjj.prismatech.item.ItemLime;
import downingjj.prismatech.item.ItemSalt;
import net.minecraft.item.Item;

import java.util.Collection;
import java.util.HashMap;

public class ModItems {
    private static HashMap<String, Item> items;

    public static void createItemMap(){
        items = new HashMap<String, Item>();

        //all mod items for registration
        add("salt", new ItemSalt());
        add("lime", new ItemLime());
    }

    private static void add(String name, Item item){
        item.setRegistryName(name);
        item.setUnlocalizedName(Prismatech.MOD_NAME + "." + name);

        items.put(name, item);
    }

    public static Item get(String name){
        return items.get(name);
    }

    public static Collection<Item> getAll(){
        return items.values();
    }
}
