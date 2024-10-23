package fr.frinn.custommachinerypnc.common.mixin;

import fr.frinn.custommachinery.common.init.CustomMachineTile;
import fr.frinn.custommachinery.common.integration.kubejs.function.MachineJS;
import fr.frinn.custommachinerypnc.common.Registration;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = MachineJS.class, remap = false)
public abstract class MachineJSMixin {

    @Final
    @Shadow(remap = false)
    private CustomMachineTile internal;

    /** PRESSURE **/

    public float getPressure() {
        return this.internal.getComponentManager()
                .getComponent(Registration.PRESSURE_COMPONENT.get())
                .map(component -> component.getHandler().getPressure())
                .orElse(0f);
    }

    public void setPressure(float pressure) {
        this.internal.getComponentManager()
                .getComponent(Registration.PRESSURE_COMPONENT.get())
                .ifPresent(component -> component.getHandler().setPressure(pressure));
    }

    public int getAir() {
        return this.internal.getComponentManager()
                .getComponent(Registration.PRESSURE_COMPONENT.get())
                .map(component -> component.getHandler().getAir())
                .orElse(0);
    }

    public void addAir(int air) {
        this.internal.getComponentManager()
                .getComponent(Registration.PRESSURE_COMPONENT.get())
                .ifPresent(component -> component.getHandler().addAir(air));
    }
}
