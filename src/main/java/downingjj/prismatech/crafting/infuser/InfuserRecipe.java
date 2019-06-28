package downingjj.prismatech.crafting.infuser;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

import static downingjj.prismatech.Prismatech.logger;

public class InfuserRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe{


    Ingredient solidIngredient;
    FluidStack fluidIngredient;

    int solidCount;
    int fluidMin;
    int powerUsage;
    int timeUsage;

    ItemStack solidOutput;
    FluidStack fluidOutput;


    public InfuserRecipe(Object solidIngredient, int solidCount, FluidStack fluidIngredient, int fluidMin, int powerUsage, int timeUsage,  ItemStack solidOutput, FluidStack fluidOutput){
        this.solidIngredient = CraftingHelper.getIngredient(solidIngredient);
        this.solidCount = solidCount;
        this.fluidIngredient = fluidIngredient;
        this.fluidMin = fluidMin;
        this.powerUsage = powerUsage;
        this.timeUsage = timeUsage;
        this.solidOutput = solidOutput;
        this.fluidOutput = fluidOutput;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {

        if (!(inv instanceof InfuserCraftInventory))
            return false;

        if(((InfuserCraftInventory)inv).solidIngredient == null || ((InfuserCraftInventory)inv).fluidIngredient == null)
            return false;

        if (!this.solidIngredient.apply(((InfuserCraftInventory) inv).solidIngredient))
            return false;

        if (FluidRegistry.getFluidName(this.fluidIngredient) != FluidRegistry.getFluidName(((InfuserCraftInventory) inv).fluidIngredient))
            return false;

        if (this.mustUseAllSolids())
            return (((InfuserCraftInventory) inv).solidIngredient.getCount() % this.solidCount) == 0 &&
                    this.fluidMin <= ((InfuserCraftInventory) inv).fluidIngredient.amount;

        if (this.mustUseAllLiquid())
            return (((InfuserCraftInventory) inv).fluidIngredient.amount % this.fluidIngredient.amount) == 0 &&
                    this.solidCount <= ((InfuserCraftInventory) inv).solidIngredient.getCount();

        return this.fluidMin <= ((InfuserCraftInventory) inv).fluidIngredient.amount &&
                    this.solidCount <= ((InfuserCraftInventory) inv).solidIngredient.getCount();
    }

    public int getNumCrafts(InfuserCraftInventory inv){
        if (this.mustUseAllLiquid())
            return inv.fluidIngredient.amount / this.fluidIngredient.amount;
        if (this.mustUseAllSolids())
            return inv.solidIngredient.getCount() / this.solidCount;
        return 1;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        if (!(inv instanceof InfuserCraftInventory)) {
            logger.error("Infuser Recipe result requested from non Infuser inventory ItemStack.Empty returned");
            return ItemStack.EMPTY;
        }
        InfuserCraftInventory iinv = (InfuserCraftInventory) inv;

        if(this.mustUseAllSolids()){
            ItemStack result = solidOutput.copy();
            result.setCount(getNumCrafts(iinv)* solidOutput.getCount());
            return result;
        }

        ItemStack remaining = iinv.solidIngredient.copy();
        remaining.setCount(iinv.solidIngredient.getCount() - this.solidCount * getNumCrafts(iinv));
        return remaining;
    }

    public FluidStack getFluidResult(InfuserCraftInventory inv) {
        if(this.mustUseAllLiquid()){
            FluidStack result = fluidOutput.copy();
            result.amount = getNumCrafts(inv) * fluidOutput.amount;
            return result;
        }

        FluidStack remaining = inv.fluidIngredient.copy();
        remaining.amount = inv.fluidIngredient.amount - this.fluidIngredient.amount * getNumCrafts(inv);
        return remaining;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width <= 1 && height <= 1;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return solidOutput.copy();
    }

    public FluidStack getFluidOutput(){
        return fluidOutput.copy();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> rem = NonNullList.create();
        rem.add(this.getCraftingResult(inv));
        return rem;
    }

    public NonNullList<FluidStack> getRemainingFluid(InfuserCraftInventory inv){
        NonNullList<FluidStack> rem = NonNullList.create();
        rem.add(this.getFluidResult(inv));
        return rem;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ings = NonNullList.create();
        ings.add(this.solidIngredient);
        return ings;
    }

    private boolean mustUseAllLiquid(){
        return this.fluidOutput != null;
    }

    private boolean mustUseAllSolids(){
        return !solidOutput.isEmpty();
    }

    public int getTimeUsage(){return this.timeUsage;}

    public int getEnergyCost(){return this.powerUsage;}

    public static class InfuserCraftInventory extends InventoryCrafting {
        ItemStack solidIngredient;
        FluidStack fluidIngredient;
        public InfuserCraftInventory(ItemStack solidIngredient, FluidStack fluidIngredient) {
            super(null, 1, 1);
            this.solidIngredient = solidIngredient;
            this.fluidIngredient = fluidIngredient;
        }
    }


    public static class InfuserRecipeBuilder {
        //Recipe Defaults
        private Object solidIngredient = ItemStack.EMPTY;
        private int solidCount = 0;
        private FluidStack fluidIngredient = null;
        private int fluidMin = -1;
        private int powerUsage = 0;
        private int timeUsage = 20;
        private ItemStack solidOutput = ItemStack.EMPTY;
        private FluidStack fluidOutput = null;

        public InfuserRecipeBuilder setSolidIngredient(ItemStack solidIngredient){
            this.solidIngredient = solidIngredient;
            this.solidCount = solidIngredient.getCount();
            return this;
        }

        public InfuserRecipeBuilder setSolidIngredient(String solidIngredient) {
            this.solidIngredient = solidIngredient;
            return this;
        }

        public InfuserRecipeBuilder setSolidCount(int solidCount) {
            this.solidCount = solidCount;
            return this;
        }

        public InfuserRecipeBuilder setFluidIngredient(FluidStack fluidIngredient) {
            this.fluidIngredient = fluidIngredient;
            return this;
        }

        public InfuserRecipeBuilder setFluidMin(int fluidMin) {
            this.fluidMin = fluidMin;
            return this;
        }

        public InfuserRecipeBuilder setPowerUsage(int powerUsage) {
            this.powerUsage = powerUsage;
            return this;
        }

        public InfuserRecipeBuilder setTimeUsage(int timeUsage) {
            this.timeUsage = timeUsage;
            return this;
        }

        public InfuserRecipeBuilder setSolidOutput(ItemStack solidOutput) {
            this.solidOutput = solidOutput;
            return this;
        }

        public InfuserRecipeBuilder setFluidOutput(FluidStack fluidOutput) {
            this.fluidOutput = fluidOutput;
            return this;
        }

        public InfuserRecipe createInfuserRecipe() {
            if (this.solidOutput.isEmpty() && this.fluidOutput == null){
                logger.warn("Infusion recipe was created with no output");
            }
            if(fluidIngredient != null && fluidMin < fluidIngredient.amount){
                logger.info("Infusion recipe was created with fluid min lower than fluid cost," +
                        " fluid min set to fluid cost");
                fluidMin = fluidIngredient.amount;
            }
            return new InfuserRecipe(solidIngredient, solidCount, fluidIngredient, fluidMin, powerUsage, timeUsage, solidOutput, fluidOutput);
        }
    }
}
