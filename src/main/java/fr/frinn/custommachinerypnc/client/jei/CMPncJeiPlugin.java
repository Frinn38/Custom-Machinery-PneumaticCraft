package fr.frinn.custommachinerypnc.client.jei;

import fr.frinn.custommachinery.client.integration.jei.DummyIngredientRenderer;
import fr.frinn.custommachinerypnc.CustomMachineryPnc;
import fr.frinn.custommachinerypnc.client.jei.pressure.Pressure;
import fr.frinn.custommachinerypnc.client.jei.pressure.PressureIngredientHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.IModIngredientRegistration;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;

@JeiPlugin
public class CMPncJeiPlugin implements IModPlugin {

    public static final ResourceLocation PLUGIN_ID = CustomMachineryPnc.rl("jei_plugin");
    public static final IIngredientType<Pressure> PRESSURE_INGREDIENT = () -> Pressure.class;

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        registration.register(PRESSURE_INGREDIENT, Collections.emptyList(), new PressureIngredientHelper(), new DummyIngredientRenderer<>(), Pressure.CODEC);
    }
}
