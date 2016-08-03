package de.lergin.sponge.test.deathCanon;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.effect.sound.SoundCategories;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Collection;

public class DeathCanon {
    private final SoundType sound;
    private final double volume;
    private final double pitch;
    private final double minVolume;

    public DeathCanon() {
        this.sound = SoundTypes.ENTITY_GENERIC_EXPLODE;
        this.volume = 100;
        this.pitch = 1;
        this.minVolume = 1;
    }

    public DeathCanon(SoundType sound) {
        this.sound = sound;
        this.volume = 100;
        this.pitch = 1;
        this.minVolume = 1;
    }

    public DeathCanon(SoundType sound, double volume, double pitch, double minVolume) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
        this.minVolume = minVolume;
    }

    public void shoot(Player target, Collection<Player> players){
        final Vector3d pos = target.getLocation().getPosition();
        for (Player player : players) {
            player.playSound(sound, SoundCategories.PLAYER, pos, volume, pitch, minVolume);
        }
    }
}
