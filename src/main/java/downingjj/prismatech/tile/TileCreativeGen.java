package downingjj.prismatech.tile;

import downingjj.prismatech.block.BlockCreativeGen;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileCreativeGen extends TileEntity implements ITickable {
    final int MAX_OUTPUT = 1000;
    TileEntity target = null;
    boolean cached = false;

    public TileCreativeGen(){}

    private EnumFacing getFacing(){
        return this.getWorld().getBlockState(this.getPos()).getValue(BlockCreativeGen.FACING);
    }


    public void checkCache(){
        EnumFacing facing = this.getFacing();
        BlockPos checkSpot = this.getPos().offset(facing);
        if(this.getWorld().isBlockLoaded(checkSpot)) {
            TileEntity tile = this.getWorld().getTileEntity(checkSpot);
            if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) {
                this.target = tile;
            }
        }
    }

    @Override
    public void update() {
        if (cached == false){
            checkCache();
            cached = true;
        }
        if(target != null){
            IEnergyStorage battery = target.getCapability(CapabilityEnergy.ENERGY, this.getFacing());
            if (battery.canReceive()) {
                battery.receiveEnergy(MAX_OUTPUT, false);
            }
        }
    }
}
