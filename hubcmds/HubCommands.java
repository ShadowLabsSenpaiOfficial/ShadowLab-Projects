package me.senpaiofficial.hubcmds;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HubCommands extends Command {

	private Main pl;

	ArrayList<ProxiedPlayer> pls = new ArrayList<>();

	public HubCommands(Main plugin) {
		super("lobby", null, "hub");
		this.pl = plugin;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(message("&eOnly players can do this."));
		}
		ProxiedPlayer p = (ProxiedPlayer) sender;
		if (pls.contains(p)) {
			pls.remove(p);
			p.sendMessage(message("&c&lTeleportation canceled."));
			return;
		} else {
			pls.add(p);
			p.sendMessage(message("&a&lPlease wait 5 seconds before you get teleported."));
			p.sendMessage(message(""));
			p.sendMessage(message("&e&lType this command once more to cancel this."));
		}

		ProxyServer.getInstance().getScheduler().schedule(pl, new Runnable() {
			@Override
			public void run() {
				if (pls.contains(p)) {
					if (!p.getServer().getInfo().getName().equalsIgnoreCase("lobby")) {
						ServerInfo target = ProxyServer.getInstance().getServerInfo("lobby");
						pls.remove(p);
						p.connect(target);
					} else {
						p.sendMessage(new ComponentBuilder("You are already connected to the Lobby!")
								.color(ChatColor.RED).create());
						pls.remove(p);
						return;
					}

				}
			}
		}, 5, TimeUnit.SECONDS);

	}

	private BaseComponent[] message(String txt) {
		return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', txt)).create();
	}
}
