package downingjj.prismatech.crafting.infuser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import downingjj.prismatech.fluid.FluidDNAHolder;
import downingjj.prismatech.item.ItemSalt;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Map;

import static downingjj.prismatech.Prismatech.logger;

public class InfuserRecipeFactory implements IRecipeFactory {
    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
        InfuserRecipe.InfuserRecipeBuilder recipeBuilder = new InfuserRecipe.InfuserRecipeBuilder();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            switch (entry.getKey()) {
                case "solid":
                    addSolidIngredient(entry.getValue(), recipeBuilder);
                    break;
                case "fluid":
                    addFluidIngredient(entry.getValue(), recipeBuilder);
                    break;
                case "result":
                    addOutput(entry.getValue(), recipeBuilder);
                    break;
                case "time":
                    recipeBuilder.setTimeUsage(entry.getValue().getAsInt());
                    break;
                case "energy":
                    recipeBuilder.setPowerUsage(entry.getValue().getAsInt());
                    break;
            }
        }
        return recipeBuilder.createInfuserRecipe();
    }

    private ItemStack getItemStackFromJson(JsonObject jsono){
        if (jsono.has("item")) {
            int meta = 0;
            int count = 1;
            if (jsono.has("data") && jsono.get("data").getAsInt() != OreDictionary.WILDCARD_VALUE) {
                meta = jsono.get("data").getAsInt();
            }
            if (jsono.has("count")) {
                count = jsono.get("count").getAsInt();
            }
            ItemStack stack = GameRegistry.makeItemStack(jsono.get("item").getAsString(), meta, count, null);
            return stack;
//            return GameRegistry.makeItemStack(jsono.get("item").getAsString(), meta, count, null);
        }
        return ItemStack.EMPTY;
    }

    @Nullable
    private FluidStack getFluidStackFromJson(JsonObject jsono){
        if(jsono.has("fluid")){
            int count = 1000;
            if (jsono.has("count")){
                count = jsono.get("count").getAsInt();
            }
            Fluid fluid = FluidRegistry.getFluid(jsono.get("fluid").getAsString());
            FluidStack stack = new FluidStack(fluid, count);
            if (jsono.has("dna") && jsono.get("dna").isJsonObject()){
                stack = FluidDNAHolder.writeDNAtoFluid(getItemStackFromJson(jsono.getAsJsonObject("dna")), stack);
            }
            return stack;
        }
        return null;
    }

    private void addOutput(JsonElement json, InfuserRecipe.InfuserRecipeBuilder recipeBuilder) {
        if(json instanceof JsonObject){
            JsonObject jsono = (JsonObject) json;
            if(jsono.has("item")) {
                recipeBuilder.setSolidOutput(getItemStackFromJson(jsono));
            }else if(jsono.has("fluid")){
                recipeBuilder.setFluidOutput(getFluidStackFromJson(jsono));
            }
        }
    }

    private void addFluidIngredient(JsonElement json, InfuserRecipe.InfuserRecipeBuilder recipeBuilder) {
        if (! (json instanceof JsonObject)) {
            logger.error("fluid ingredient in Infuser Recipe must be a json object");
            return;
        }
        JsonObject jsono =(JsonObject) json;

        if(!jsono.has("fluid") || !jsono.has("count")) {
            logger.error("fluid ingredient in Infuser Recipe must have \"fluid\" and \"count\" attributes");
            return;
        }
        recipeBuilder.setFluidIngredient(getFluidStackFromJson(jsono));
        if (jsono.has("min"))
            recipeBuilder.setFluidMin(jsono.get("min").getAsInt());
    }

    private void addSolidIngredient(JsonElement json, InfuserRecipe.InfuserRecipeBuilder recipeBuilder) {
        if (!(json instanceof JsonObject)) {
            logger.error("solid ingredient in Infuser Recipe must be a json object");
            return;
        }
        JsonObject jsono = (JsonObject) json;
        if (jsono.has("type") && (jsono.get("type").getAsString().equals("forge:ore_dict"))) {
            if (jsono.has("ore")) {
                recipeBuilder.setSolidIngredient(jsono.get("ore").getAsString());
                if (jsono.has("count")) {
                    recipeBuilder.setSolidCount(jsono.get("count").getAsInt());
                }
            } else {
                logger.error("ore dictionary solid in Infuser Recipe has no \"ore\" attribute");
            }
        } else {
            recipeBuilder.setSolidIngredient(getItemStackFromJson(jsono));
        }
    }
}
