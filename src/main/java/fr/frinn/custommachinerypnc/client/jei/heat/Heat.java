package fr.frinn.custommachinerypnc.client.jei.heat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Heat(int amount) {
    public static final Codec<Heat> CODEC = RecordCodecBuilder.create(heatInstance ->
            heatInstance.group(
                    Codec.intRange(1, Integer.MAX_VALUE).fieldOf("amount").forGetter(Heat::amount)
            ).apply(heatInstance, Heat::new)
    );
}
