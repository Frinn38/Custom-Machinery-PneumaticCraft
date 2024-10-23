package fr.frinn.custommachinerypnc.client.jei.pressure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Pressure(float min, float max, int volume) {
    public static final Codec<Pressure> CODEC = RecordCodecBuilder.create(pressureInstance ->
            pressureInstance.group(
                    Codec.FLOAT.fieldOf("min").forGetter(Pressure::min),
                    Codec.FLOAT.fieldOf("max").forGetter(Pressure::max),
                    Codec.INT.fieldOf("volume").forGetter(Pressure::volume)
            ).apply(pressureInstance, Pressure::new)
    );
}
