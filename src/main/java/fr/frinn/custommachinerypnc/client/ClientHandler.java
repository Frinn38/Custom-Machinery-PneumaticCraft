package fr.frinn.custommachinerypnc.client;

import fr.frinn.custommachinery.api.guielement.RegisterGuiElementWidgetSupplierEvent;
import fr.frinn.custommachinery.api.integration.jei.RegisterGuiElementJEIRendererEvent;
import fr.frinn.custommachinery.client.screen.creation.component.RegisterComponentBuilderEvent;
import fr.frinn.custommachinery.client.screen.creation.gui.RegisterGuiElementBuilderEvent;
import fr.frinn.custommachinerypnc.CustomMachineryPnc;
import fr.frinn.custommachinerypnc.client.creation.component.HeatComponentBuilder;
import fr.frinn.custommachinerypnc.client.creation.component.PressureComponentBuilder;
import fr.frinn.custommachinerypnc.client.creation.gui.HeatGuiElementBuilder;
import fr.frinn.custommachinerypnc.client.creation.gui.PressureGuiElementBuilder;
import fr.frinn.custommachinerypnc.client.guielement.HeatGuiElementWidget;
import fr.frinn.custommachinerypnc.client.guielement.PressureGuiElementWidget;
import fr.frinn.custommachinerypnc.client.jei.pressure.PressureGuiElementJeiRenderer;
import fr.frinn.custommachinerypnc.common.Registration;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = CustomMachineryPnc.MODID, value = Dist.CLIENT)
public class ClientHandler {

    @SubscribeEvent
    public static void registerGuiElementWidgets(final RegisterGuiElementWidgetSupplierEvent event) {
        event.register(Registration.PRESSURE_ELEMENT.get(), PressureGuiElementWidget::new);
        event.register(Registration.HEAT_ELEMENT.get(), HeatGuiElementWidget::new);
    }

    @SubscribeEvent
    public static void registerGuiElementBuilders(final RegisterGuiElementBuilderEvent event) {
        event.register(Registration.PRESSURE_ELEMENT.get(), new PressureGuiElementBuilder());
        event.register(Registration.HEAT_ELEMENT.get(), new HeatGuiElementBuilder());
    }

    @SubscribeEvent
    public static void registerComponentBuilders(final RegisterComponentBuilderEvent event) {
        event.register(Registration.PRESSURE_COMPONENT.get(), new PressureComponentBuilder());
        event.register(Registration.HEAT_COMPONENT.get(), new HeatComponentBuilder());
    }

    @SubscribeEvent
    public static void registerGuiElementJeiRenderers(final RegisterGuiElementJEIRendererEvent event) {
        event.register(Registration.PRESSURE_ELEMENT.get(), new PressureGuiElementJeiRenderer());
    }
}
