package be.hctel.renaissance.hideandseek.nongame.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.comphenix.packetwrapper.WrapperPlayServerNamedSoundEffect;
import com.comphenix.packetwrapper.WrapperPlayServerNamedSoundEffect.NamedSoundEffects;

import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_12_R1.SoundCategory;
import net.minecraft.server.v1_12_R1.SoundEffect;

public class PackettedUtils {
	public static void sendSound(Player player, Location loc, Sound sound, float volume, float pitch) {
		WrapperPlayServerNamedSoundEffect packet = new WrapperPlayServerNamedSoundEffect();
		packet.setEffectPositionX(loc.getX());
		packet.setEffectPositionY(loc.getY());
		packet.setEffectPositionZ(loc.getZ());
		packet.setVolume(volume);
		packet.setPitch(pitch);
		packet.setSoundName(getNamedSoundEffectFromSound(sound));
		((CraftPlayer) player).getHandle().playerConnection.sendPacket((PacketPlayOutNamedSoundEffect) packet.getHandle().getHandle());
	}
	private static String getNamedSoundEffectFromSound(Sound sound) {
		for(String S : NamedSoundEffects.values()) {
			if(sound.toString() == S) {
				return S;
			}
		}
		return null;
	}
	public static void sendSound(Player player, Location loc, String soundName, float volume, float pitch) {
		PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(new SoundEffect(new MinecraftKey(soundName)), SoundCategory.AMBIENT, (int) loc.getX()*8, (int) loc.getY()*8, (int) loc.getZ()*8, volume, pitch);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
}
