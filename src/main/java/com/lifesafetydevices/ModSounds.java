package com.lifesafetydevices;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds {
    // bg-12 activation
    public static final Identifier BG12_ACTIVATION_ID = new Identifier("lifesafetydevices", "bg-12_activation");
    public static final SoundEvent BG12_ACTIVATION = new SoundEvent(BG12_ACTIVATION_ID);
    //bg-12 reset
    public static final Identifier BG12_RESET_ID = new Identifier("lifesafetydevices", "bg-12_reset");
    public static final SoundEvent BG12_RESET = new SoundEvent(BG12_RESET_ID);

    public static void registerSounds() {
        Registry.register(Registry.SOUND_EVENT, BG12_ACTIVATION_ID, BG12_ACTIVATION);
        Registry.register(Registry.SOUND_EVENT, BG12_RESET_ID, BG12_RESET);
    }
}
