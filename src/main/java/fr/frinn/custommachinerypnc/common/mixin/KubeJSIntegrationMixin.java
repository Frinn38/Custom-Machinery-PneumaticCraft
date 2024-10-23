package fr.frinn.custommachinerypnc.common.mixin;

import fr.frinn.custommachinery.common.integration.kubejs.CustomCraftRecipeBuilderJS;
import fr.frinn.custommachinery.common.integration.kubejs.CustomMachineRecipeBuilderJS;
import fr.frinn.custommachinerypnc.common.integration.kubejs.PressureRequirementJS;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({CustomMachineRecipeBuilderJS.class, CustomCraftRecipeBuilderJS.class})
public abstract class KubeJSIntegrationMixin implements PressureRequirementJS {

}
