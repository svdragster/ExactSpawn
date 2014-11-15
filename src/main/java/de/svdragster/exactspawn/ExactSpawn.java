package de.svdragster.exactspawn;

import net.canarymod.Canary;
import net.canarymod.plugin.Plugin;

public class ExactSpawn extends Plugin {

	public void disable() {

	}

	public boolean enable() {
		Canary.hooks().registerListener(new ExactSpawnListener(), this);
		return true;
	}

}
