package com.biggestnerd.radarjammer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;

public class RadarJammer extends JavaPlugin {
	
	private VisibilityManager visManager;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		reloadConfig();
		initializeVisibilityManager();
		ProtocolLibrary.getProtocolManager().addPacketListener(new PlayerListManager(this));
	}
	
	@Override
	public void onDisable() {
		getLogger().info("RadarJammer shutting down!");
	}
	
	private void initializeVisibilityManager() {
		FileConfiguration config = getConfig();
		int minCheck = config.getInt("minCheck", 14);
		String maxCheckString = config.getString("maxCheck", "auto");
		int maxCheck = 0;
		if(maxCheckString.equals("auto")) {
			maxCheck = getServer().getViewDistance() * 16 + 8;
		} else {
			maxCheck = config.getInt("maxCheck");
		}
		double vFov = config.getDouble("vFov", 35.0);
		double hFov = config.getDouble("hFov", 60.0);
		double maxFov = Math.sqrt((vFov * vFov) + (hFov * hFov));
		
		boolean showCombatTagged = config.getBoolean("showCombatTagged", true);
		boolean trueInvis = config.getBoolean("trueInvis", true);
		boolean timing = config.getBoolean("timing", false);
		float maxSpin = config.getInt("maxSpin", 500);
		long flagTime = config.getInt("flagTime", 55) * 1000l;
		int maxFlags = config.getInt("maxFlags", 100);
		int blindDuration = config.getInt("blindDuration", 3);
		
		visManager = new VisibilityManager(this, minCheck, maxCheck, maxFov, showCombatTagged, trueInvis, timing, maxSpin, flagTime, maxFlags, blindDuration);
		getServer().getPluginManager().registerEvents(visManager, this);
	}
}