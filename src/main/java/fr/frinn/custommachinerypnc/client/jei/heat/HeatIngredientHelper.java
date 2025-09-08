package fr.frinn.custommachinerypnc.client.jei.heat;

import fr.frinn.custommachinerypnc.CustomMachineryPnc;
import fr.frinn.custommachinerypnc.client.jei.CMPncJeiPlugin;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class HeatIngredientHelper implements IIngredientHelper<Heat> {

    @Override
    public IIngredientType<Heat> getIngredientType() {
        return CMPncJeiPlugin.HEAT_INGREDIENT;
    }

    @Override
    public String getDisplayName(Heat ingredient) {
        return  Component.translatable("custommachinerypnc.jei.ingredient.heat", ingredient.amount()).getString();
    }

    //Safe to remove
    @SuppressWarnings("removal")
    @Override
    public String getUniqueId(Heat ingredient, UidContext context) {
        return "" + ingredient.amount();
    }

    @Override
    public ResourceLocation getResourceLocation(Heat ingredient) {
        return CustomMachineryPnc.rl("" + ingredient.amount());
    }

    @Override
    public Heat copyIngredient(Heat ingredient) {
        return new Heat(ingredient.amount());
    }

    @Override
    public String getErrorInfo(@Nullable Heat ingredient) {
        return "";
    }
}
