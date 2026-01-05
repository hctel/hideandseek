package be.hctel.api.protocol.adapters;

import java.util.UUID;

import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_21_R6.block.data.CraftBlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;

import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;

public class AdapterPacketPlayOutSpawnEntity {
	PacketContainer packet;
	public AdapterPacketPlayOutSpawnEntity(PacketContainer packet) {
		this.packet = packet;
	}
	
	public AdapterPacketPlayOutSpawnEntity(Entity entity) {
		this.packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY);
		setUniqueId(entity.getUniqueId());
		setEntityType(entity.getType());
		setEntityId(entity.getEntityId());
		setX(entity.getLocation().getX());
		setY(entity.getLocation().getY());
		setZ(entity.getLocation().getZ());
		switch(entity.getType()) {
		case ITEM_FRAME: {
			ItemFrame frame = (ItemFrame) entity;
			setEntityData(getFacing(frame.getFacing()));
		}
		case PAINTING: {
			Painting painting = (Painting) entity;
			setEntityData(getFacing(painting.getFacing()));
			break;
		}
		case FALLING_BLOCK: {
			FallingBlock block = (FallingBlock) entity;
			setEntityData(net.minecraft.world.level.block.Block.j(((CraftBlockData) block.getBlockData()).getState()));
			break;
		}
		case ARROW, EXPERIENCE_BOTTLE, EGG, ENDER_PEARL, EYE_OF_ENDER, FIREBALL, FIREWORK_ROCKET, LINGERING_POTION, LLAMA_SPIT, SMALL_FIREBALL, SNOWBALL, SPECTRAL_ARROW, SPLASH_POTION, TRIDENT, WIND_CHARGE, FISHING_BOBBER : {
			Projectile prj = (Projectile) entity;
			ProjectileSource prjSrc = prj.getShooter();
			if(prjSrc instanceof Entity) {
				Entity prjScrEnt = (Entity) prjSrc;
				setEntityData(prjScrEnt.getEntityId());
			}
			break;
		}
		default: break;
		}
		
	}
	
	public int getEntityId() {
		return packet.getIntegers().getValues().get(0);
	}
	
	public int getEntityData() {
		return packet.getIntegers().getValues().get(1);
	}
	
	public double getX() {
		return packet.getDoubles().getValues().get(0);
	}
	
	public double getY() {
		return packet.getDoubles().getValues().get(1);
	}
	
	public double getZ() {
		return packet.getDoubles().getValues().get(2);
	}
	
	public UUID getUniqueId() {
		return packet.getUUIDs().getValues().get(0);
	}
	
	public EntityType getEntityType() {
		return packet.getEntityTypeModifier().getValues().get(0);
	}
	
	public void setEntityId(int id) {
		packet.getIntegers().write(0, id);
	}
	
	public void setEntityData(int data) {
		packet.getIntegers().write(1, data);
	}
	
	public void setX(double x) {
		packet.getDoubles().write(0, x);
	}
	
	public void setY(double y) {
		packet.getDoubles().write(1, y);
	}
	
	public void setZ(double z) {
		packet.getDoubles().write(2, z);
	}
	
	public void setUniqueId(UUID uuid) {
		packet.getUUIDs().write(0, uuid);
	}
	
	public void setEntityType(EntityType type) {
		packet.getEntityTypeModifier().write(0, type);
	}
	
	public PacketContainer getPacket() {
		return packet;
	}
	
	public PacketPlayOutSpawnEntity getHandle() {
		return (PacketPlayOutSpawnEntity) packet.getHandle();
	}
	
	private int getFacing(BlockFace face) {
		switch(face) {
		case DOWN:
			return 0;
		case EAST:
			return 5;
		case NORTH:
			return 2;
		case SOUTH:
			return 3;
		case UP:
			return 1;
		case WEST:
			return 4;
		default:
			return 2;		
		}
	}
}
