package downingjj.prismatech.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyStorageSeriealizable extends EnergyStorage {

    public EnergyStorageSeriealizable(int capacity) { super(capacity); }

    protected void onContentsChanged(){};

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate){
        int newEnergy = super.receiveEnergy(maxReceive, simulate);
        onContentsChanged();
        return newEnergy;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate){
        int newEnergy = super.extractEnergy(maxExtract, simulate);
        onContentsChanged();
        return newEnergy;
    }

    public void readFromNBT(NBTTagCompound tag){
        this.energy = tag.getInteger("energy");
    }

    public NBTTagCompound writeToNBT(){
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("energy", this.energy);
        return tag;
    }


}
