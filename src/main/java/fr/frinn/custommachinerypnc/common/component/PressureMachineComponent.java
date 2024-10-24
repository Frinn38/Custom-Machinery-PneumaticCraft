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
import fr.frinn.custommachinery.common.network.syncable.FloatSyncable;
import fr.frinn.custommachinery.common.network.syncable.ToggleSideConfigSyncable;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import fr.frinn.custommachinery.impl.component.config.ToggleSideConfig;
import fr.frinn.custommachinerypnc.common.Registration;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.pressure.PressureTier;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;

import java.util.Arrays;
import java.util.function.Consumer;

public class PressureMachineComponent extends AbstractMachineComponent implements ISerializableComponent, ITickableComponent, ISideConfigComponent, ISyncableStuff {

    private final IAirHandlerMachine handler;
    private final ToggleSideConfig config;
    private int prevAir = 0;

    public PressureMachineComponent(IMachineComponentManager manager, int volume, float danger, float critical, ToggleSideConfig.Template config) {
        super(manager, ComponentIOMode.BOTH);
        this.handler = PneumaticRegistry.getInstance().getAirHandlerMachineFactory().createAirHandler(new CustomPressureTier(danger, critical), volume);
        this.config = config.build(this);
        this.config.setCallback((side, oldMode, newMode) -> this.refreshConnectableFaces());
    }

    private void refreshConnectableFaces() {
        this.handler.setConnectableFaces(Arrays.stream(Direction.values()).filter(side -> this.config.getSideMode(side).isEnabled()).toList());
        this.getManager().getTile().invalidateCapabilities();
        this.getManager().getLevel().updateNeighborsAt(this.getManager().getTile().getBlockPos(), this.getManager().getTile().getBlockState().getBlock());
    }

    public IAirHandlerMachine getHandler() {
        return this.handler;
    }

    @Override
    public MachineComponentType<PressureMachineComponent> getType() {
        return Registration.PRESSURE_COMPONENT.get();
    }


    @Override
    public void serverTick() {
        this.handler.tick(this.getManager().getTile());
        if(this.prevAir != this.handler.getAir())
            this.getManager().markDirty();
        this.prevAir = this.handler.getAir();
    }

    @Override
    public void clientTick() {
        this.handler.tick(this.getManager().getTile());
    }

    @Override
    public void serialize(CompoundTag nbt, Provider registries) {
        nbt.put("pressure", this.handler.serializeNBT());
    }

    @Override
    public void deserialize(CompoundTag nbt, Provider registries) {
        if(nbt.contains("pressure", CompoundTag.TAG_COMPOUND))
            this.handler.deserializeNBT(nbt.getCompound("pressure"));
    }

    @Override
    public ToggleSideConfig getConfig() {
        return this.config;
    }

    @Override
    public String getId() {
        return "pressure";
    }

    @Override
    public void getStuffToSync(Consumer<ISyncable<?, ?>> container) {
        container.accept(FloatSyncable.create(this.handler::getPressure, this.handler::setPressure));
        container.accept(ToggleSideConfigSyncable.create(this::getConfig, this.config::set));
    }

    public record Template(int volume, float danger, float critical, ToggleSideConfig.Template config) implements IMachineComponentTemplate<PressureMachineComponent> {

        public static final NamedCodec<Template> CODEC = NamedCodec.record(templateInstance ->
                templateInstance.group(
                        NamedCodec.INT.fieldOf("volume").forGetter(Template::volume),
                        NamedCodec.floatRange(0.0f, 25.0f).fieldOf("danger").forGetter(Template::danger),
                        NamedCodec.floatRange(0.0f, 25.0f).fieldOf("critical").forGetter(Template::critical),
                        ToggleSideConfig.Template.CODEC.optionalFieldOf("config", ToggleSideConfig.Template.DEFAULT_ALL_ENABLED).forGetter(Template::config)
                ).apply(templateInstance, Template::new), "Pressure machine component"
        );

        @Override
        public MachineComponentType<PressureMachineComponent> getType() {
            return Registration.PRESSURE_COMPONENT.get();
        }

        @Override
        public String getId() {
            return "pressure";
        }

        @Override
        public boolean canAccept(Object ingredient, boolean isInput, IMachineComponentManager manager) {
            return false;
        }

        @Override
        public PressureMachineComponent build(IMachineComponentManager manager) {
            return new PressureMachineComponent(manager, this.volume, this.danger, this.critical, this.config);
        }
    }

    public record CustomPressureTier(float danger, float critical) implements PressureTier {

        @Override
        public float getDangerPressure() {
            return this.danger;
        }

        @Override
        public float getCriticalPressure() {
            return this.critical;
        }
    }
}
