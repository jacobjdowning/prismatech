package downingjj.prismatech.item;

import downingjj.prismatech.util.IOreDictionaried;
import net.minecraft.item.Item;

public class ItemSalt extends Item implements IOreDictionaried {
    public ItemSalt(){
        super();
    }

    @Override
    public String getDictionaryName() {
        return "salt";
    }
}
