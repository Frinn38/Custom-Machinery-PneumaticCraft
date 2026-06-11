package fr.frinn.custommachinerypnc.common.component;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.IMachineComponentTemplate;
import fr.frinn.custommachinery.api.component.ISerializableComponent;
import fr.frinn.custommachinery.api.component.ISideConfigComponent;
import fr.frinn.custommachinery.api.component.ITickableComponent;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.network.ISyncable;
import fr.frinn.custommachinery.api.network.ISyncableStuff;
import fr.frinn.custommachinery.common.network.syncable.DoubleSyncable;
import fr.frinn.custommachinery.common.network.syncable.ToggleSideConfigSyncable;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import fr.frinn.custommachinery.impl.component.config.ToggleSideConfig;
import fr.frinn.custommachinerypnc.common.Registration;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.heat.IHeatExchangerLogic;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.Arrays;
import java.util.function.Consumer;

public class HeatMachineComponent extends AbstractMachineComponent implements ISideConfigComponent, ITickableComponent, ISerializableComponent, ISyncableStuff {

    private final ToggleSideConfig config;
    private final IHeatExchangerLogic heatExchanger;
    private double prevTemp;

    public HeatMachineComponent(IMachineComponentManager manager, double capacity, double resistance, ToggleSideConfig.Template config) {
        super(manager, ComponentIOMode.BOTH);
        this.config = config.build(manager.facing());
        this.config.setCallback((side, oldMode, newMode) -> {
            this.init();
            this.getManager().getLevel().updateNeighborsAt(this.getManager().getTile().getBlockPos(), this.getManager().getTile().getBlockState().getBlock());
        });
        this.heatExchanger = PneumaticRegistry.getInstance().getHeatRegistry().makeHeatExchangerLogic();
        this.heatExchanger.setThermalCapacity(capacity);
        this.heatExchanger.setThermalResistance(resistance);
        this.upgradeableD(capacity, "capacity", 0.0D, Double.MAX_VALUE, this.getHeatExchanger()::setThermalCapacity);
        this.upgradeableD(resistance, "resistance", 0.0D, Double.MAX_VALUE, this.getHeatExchanger()::setThermalResistance);
    }

    public IHeatExchangerLogic getHeatExchanger() {
        return this.heatExchanger;
    }

    @Override
    public MachineComponentType<HeatMachineComponent> getType() {
        return Registration.HEAT_COMPONENT.get();
    }

    @Override
    public ToggleSideConfig getConfig() {
        return this.config;
    }

    @Override
    public String getId() {
        return "PNCHeat";
    }

    @Override
    public void serverTick() {
        this.heatExchanger.tick();
        if(this.prevTemp != this.heatExchanger.getTemperature())
            this.getManager().markDirty();
        this.prevTemp = this.heatExchanger.getTemperature();
    }

    @Override
    public void init() {
        if(getManager().getLevel() == null)
            return;
        Direction[] validSides = Arrays.stream(Direction.values()).filter(side -> this.config.getDirectionMode(side).isEnabled()).toArray(Direction[]::new);
        this.heatExchanger.initializeAsHull(getManager().getLevel(), getManager().getTile().getBlockPos(), IHeatExchangerLogic.ALL_BLOCKS, validSides);
    }

    @Override
    public void serialize(CompoundTag nbt, Provider registries) {
        nbt.put("pnc_heat", this.heatExchanger.serializeNBT());
    }

    @Override
    public void deserialize(CompoundTag nbt, Provider registries) {
        if(nbt.contains("pnc_heat", Tag.TAG_COMPOUND))
            this.heatExchanger.deserializeNBT(nbt.getCompound("pnc_heat"));
    }

    @Override
    public void getStuffToSync(Consumer<ISyncable<?, ?>> container) {
        container.accept(DoubleSyncable.create(this.heatExchanger::getTemperature, this.heatExchanger::setTemperature));
        container.accept(ToggleSideConfigSyncable.create(this::getConfig, this.config::set));
    }

    public record Template(double capacity, double resistance, ToggleSideConfig.Template config) implements IMachineComponentTemplate<HeatMachineComponent> {

        public static final NamedCodec<Template> CODEC = NamedCodec.record(templateInstance ->
                templateInstance.group(
                        NamedCodec.doubleRange(0.0D, Double.MAX_VALUE).optionalFieldOf("capacity", 1.0D).forGetter(Template::capacity),
                        NamedCodec.doubleRange(0.0D, Double.MAX_VALUE).optionalFieldOf("resistance", 1.0D).forGetter(Template::resistance),
                        ToggleSideConfig.Template.CODEC.optionalFieldOf("config", ToggleSideConfig.Template.DEFAULT_ALL_ENABLED).forGetter(Template::config)
                ).apply(templateInstance, Template::new), "Heat component template"
        );


        @Override
        public MachineComponentType<HeatMachineComponent> getType() {
            return Registration.HEAT_COMPONENT.get();
        }

        @Override
        public String getId() {
            return "PNCHeat";
        }

        @Override
        public boolean canAccept(Object ingredient, boolean isInput, IMachineComponentManager manager) {
            return false;
        }

        @Override
        public HeatMachineComponent build(IMachineComponentManager manager) {
            return new HeatMachineComponent(manager, this.capacity, this.resistance, this.config);
        }
    }
}
