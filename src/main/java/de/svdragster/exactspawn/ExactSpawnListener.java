package de.svdragster.exactspawn;

import java.util.ArrayList;
import java.util.List;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.position.Vector3D;
import net.canarymod.chat.ChatFormat;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.command.PlayerCommandHook;
import net.canarymod.hook.player.ConnectionHook;
import net.canarymod.hook.player.PlayerRespawnedHook;
import net.canarymod.hook.player.PlayerRespawningHook;
import net.canarymod.plugin.PluginListener;

public class ExactSpawnListener implements PluginListener {

	List<Player> dead = new ArrayList<Player>();
	
	public boolean isBedSpawn(Player player) {
		Location spawnLocation = player.getSpawnPosition();
		spawnLocation.setY(0);
		Location worldSpawnLocation = player.getWorld().getSpawnLocation();
		worldSpawnLocation.setY(0);
		Vector3D v = new Vector3D(spawnLocation);
		if (v.getDistance(worldSpawnLocation) > 16) {
			return true;
		}
		return false;
	}
	
	@HookHandler
	public void onPlayerRespawning(PlayerRespawningHook hook) {
		Player player = hook.getPlayer();
		dead.add(player);
	}
	
	@HookHandler
	public void onPlayerRespawned(PlayerRespawnedHook hook) {
		Player player = hook.getPlayer();
		if (dead.contains(player)) {
			dead.remove(player);
			if (isBedSpawn(player)) {
				player.teleportTo(player.getSpawnPosition());
			} else if (player.hasHome()) {
				player.teleportTo(player.getHome());
			} else {
				player.teleportTo(player.getWorld().getSpawnLocation());
			}
		}
	}
	
	@HookHandler
	public void onLogin(ConnectionHook hook) {
		if (hook.isFirstConnection()) {
			Player player = hook.getPlayer();
			player.teleportTo(player.getWorld().getSpawnLocation());
		}
	}
	
	@HookHandler
	public void onCommand(PlayerCommandHook hook) {
		Player player = hook.getPlayer();
		String[] message = hook.getCommand();
		String p0 = message[0];
		if (p0.equalsIgnoreCase("/spawn")) {
			if (message.length >= 2) {
				String strWorld = message[1];
				if (message.length >= 3) {
					String strOtherPlayer = message[2];
					Player otherPlayer = Canary.getServer().matchPlayer(strOtherPlayer);
					if (otherPlayer != null) {
						if (strWorld.equalsIgnoreCase(otherPlayer.getWorld().getName())) {
							otherPlayer.teleportTo(player.getWorld().getSpawnLocation());
							otherPlayer.message(ChatFormat.YELLOW + "You are now at the spawn");
							player.message(ChatFormat.YELLOW + otherPlayer.getName() + " is now at the spawn");
							hook.setCanceled();
						}
					} else {
						player.notice(strOtherPlayer + "is not online.");
						hook.setCanceled();
					}
				} else {
					if (strWorld.equalsIgnoreCase(player.getWorld().getName())) {
						player.teleportTo(player.getWorld().getSpawnLocation());
						player.message(ChatFormat.YELLOW + "You are now at the spawn");
						hook.setCanceled();
					}
				}
			} else {
				player.teleportTo(player.getWorld().getSpawnLocation());
				player.message(ChatFormat.YELLOW + "You are now at the spawn");
				hook.setCanceled();
			}
		}
	}
}
