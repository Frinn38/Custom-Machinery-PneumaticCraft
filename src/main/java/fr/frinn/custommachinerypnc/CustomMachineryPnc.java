package fr.frinn.custommachinerypnc;

import fr.frinn.custommachinery.common.init.CustomMachineTile;
import fr.frinn.custommachinerypnc.common.Registration;
import fr.frinn.custommachinerypnc.common.component.PressureMachineComponent;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

@Mod(CustomMachineryPnc.MODID)
public class CustomMachineryPnc {

    public static final String MODID = "custommachinerypnc";

    public CustomMachineryPnc(final IEventBus MOD_BUS) {
        Registration.GUI_ELEMENTS.register(MOD_BUS);
        Registration.MACHINE_COMPONENTS.register(MOD_BUS);
        Registration.REQUIREMENTS.register(MOD_BUS);
        MOD_BUS.addListener(this::attachCapabilities);
    }

    private void attachCapabilities(final RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(PNCCapabilities.AIR_HANDLER_MACHINE, fr.frinn.custommachinery.common.init.Registration.CUSTOM_MACHINE_TILE.get(), new ICapabilityProvider<>() {
            @Nullable
            @Override
            public IAirHandlerMachine getCapability(CustomMachineTile machine, Direction context) {
                return machine.getComponentManager().getComponent(Registration.PRESSURE_COMPONENT.get())
                        .map(PressureMachineComponent::getHandler)
                        .orElse(null);
            }
        });
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
