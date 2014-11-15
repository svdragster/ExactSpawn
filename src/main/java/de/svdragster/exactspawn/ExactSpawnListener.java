package de.svdragster.exactspawn;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.position.Vector3D;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.ConnectionHook;
import net.canarymod.hook.player.PlayerRespawnedHook;
import net.canarymod.hook.player.PlayerRespawningHook;
import net.canarymod.plugin.PluginListener;

public class ExactSpawnListener implements PluginListener {

	@HookHandler
	public void onPlayerRespawning(PlayerRespawningHook hook) {
		Player player = hook.getPlayer();
		if (!hook.isBedSpawn()) {
			hook.setRespawnLocation(player.getWorld().getSpawnLocation());
		}
	}
	
	@HookHandler
	public void onPlayerRespawned(PlayerRespawnedHook hook) {
		Player player = hook.getPlayer();
		Vector3D current = new Vector3D(hook.getLocation());
		Vector3D worldSpawn = new Vector3D(player.getWorld().getSpawnLocation());
		current.setY(0);
		worldSpawn.setY(0);
		double distance = current.getDistance(worldSpawn);
		if (distance <= 18) {
			player.teleportTo(player.getWorld().getSpawnLocation());
		}
	}
	
	@HookHandler
	public void onLogin(ConnectionHook hook) {
		if (hook.isFirstConnection()) {
			Player player = hook.getPlayer();
			player.teleportTo(player.getWorld().getSpawnLocation());
		}
	}	
}
