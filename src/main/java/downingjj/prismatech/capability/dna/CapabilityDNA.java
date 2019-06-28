package downingjj.prismatech.capability.dna;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityDNA {
    @CapabilityInject(IDNAStorage.class)
    public static final Capability<IDNAStorage> DNA = null;

    public static void register(){
        CapabilityManager.INSTANCE.register(IDNAStorage.class, new Capability.IStorage<IDNAStorage>() {
            @Nullable
            @Override
            public NBTBase writeNBT(Capability<IDNAStorage> capability, IDNAStorage instance, EnumFacing side) {
                return null;
            }

            @Override
            public void readNBT(Capability<IDNAStorage> capability, IDNAStorage instance, EnumFacing side, NBTBase nbt) {

            }
        },
                ()->new DNAStorage());
    }
}
