package me.senpaiofficial.hubcmds;

import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin{
	
	@Override
	public void onEnable(){
		getLogger().info("/hub enabled");
		getLogger().info("/lobby enabled");
		getProxy().getPluginManager().registerCommand(this, new HubCommands(this));

	}

}
