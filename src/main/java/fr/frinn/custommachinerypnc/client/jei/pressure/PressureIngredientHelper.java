package fr.frinn.custommachinerypnc.client.jei.pressure;

import fr.frinn.custommachinerypnc.CustomMachineryPnc;
import fr.frinn.custommachinerypnc.client.jei.CMPncJeiPlugin;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class PressureIngredientHelper implements IIngredientHelper<Pressure> {

    @Override
    public IIngredientType<Pressure> getIngredientType() {
        return CMPncJeiPlugin.PRESSURE_INGREDIENT;
    }

    @Override
    public String getDisplayName(Pressure ingredient) {
        return  Component.translatable("custommachinerypnc.jei.ingredient.pressure", ingredient.min(), ingredient.max()).getString();
    }

    //Safe to remove
    @SuppressWarnings("removal")
    @Override
    public String getUniqueId(Pressure ingredient, UidContext context) {
        return "" + ingredient.min() + ingredient.max() + ingredient.volume();
    }

    @Override
    public ResourceLocation getResourceLocation(Pressure ingredient) {
        return CustomMachineryPnc.rl("" + ingredient.min() + ingredient.max() + ingredient.volume());
    }

    @Override
    public Pressure copyIngredient(Pressure ingredient) {
        return new Pressure(ingredient.min(), ingredient.max(), ingredient.volume());
    }

    @Override
    public String getErrorInfo(@Nullable Pressure ingredient) {
        return "";
    }
}
