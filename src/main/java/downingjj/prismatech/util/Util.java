package downingjj.prismatech.util;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class Util {
    public static <T extends TileEntity>T getTEfromPos(IBlockAccess world, BlockPos pos, Class<T> clazz){
        if(!clazz.isInstance(world.getTileEntity(pos))){
            throw new RuntimeException("TE retrieved was not the class provided");
        }else{
            return clazz.cast(world.getTileEntity(pos));
        }
    }
}
