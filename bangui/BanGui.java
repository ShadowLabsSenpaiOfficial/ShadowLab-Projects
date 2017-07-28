package me.senpaiofficial.bangui;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class BanGui extends JavaPlugin implements Listener {
	HashMap<Player, Player> info = new HashMap<Player, Player>();

	GuiMaker gui = new GuiMaker();
	Inventory inv;

	public void onEnable() {

		gui.setupInv();
		inv = gui.getInventory();
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("As much as you wish to ban that person, you need to be in game to do so.");
			return true;
		}

		if (command.getName().equalsIgnoreCase("tb")) {
			Player p = (Player) sender;
			if (!p.hasPermission("bans.temp")) {
				p.sendMessage(ChatColor.RED + "You can't do this!");
				return true;
			}
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "<> = required");
				sender.sendMessage(ChatColor.RED + "/tb <player>");
			}
			if (args.length == 1) {

				Player other = Bukkit.getServer().getPlayer(args[0]);

				if (other == null) {
					p.sendMessage(ChatColor.RED + "Error: Player " + ChatColor.YELLOW + args[0] + ChatColor.RED
							+ " is not online");
					return true;
				}
				p.openInventory(inv);
				info.put(p, other);
			}

		}

		return true;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (!e.getInventory().equals(inv)) {
			return;
		}
		Player p = (Player) e.getWhoClicked();
		Player other = info.get(p);

		if (other == null) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED + "An error has occoured! Please try again.");
			p.closeInventory();
		}
		////////
		Material mat = e.getCurrentItem().getType();
		switch (mat) {
		default: {
			p.closeInventory();
			break;
		}
		case DIAMOND: {
			Bukkit.dispatchCommand(p, "tempban " + other.getName() + " 30 d &cYou were caught hacking using X-Ray.");
			p.closeInventory();
			break;
		}
		case FEATHER: {
			Bukkit.dispatchCommand(p, "tempban " + other.getName()
					+ " 45 d &cYou were caught hacking using movement hacking (Fly|Speed|Jesus)");
			p.closeInventory();
			break;
		}
		case DIAMOND_SWORD: {
			Bukkit.dispatchCommand(p, "tempban " + other.getName()
					+ " 45 d &cYou were caught hacking using combat related hacking (Killaura|Mobaura)");
			p.closeInventory();
			break;
		}
		case SKULL_ITEM: {
			Bukkit.dispatchCommand(p, "tempban " + other.getName()
					+ "	7 d &cYou have been banned for disrespect towards the community of our server.");
			p.closeInventory();
			break;
		}
		case FURNACE: {
			Bukkit.dispatchCommand(p, "tempban " + other.getName()
					+ " 15 d &cYou have been banned for 20 days (decreased time) for admitting to rule-breaking.");
		}
		case ENDER_CHEST: {
			Bukkit.dispatchCommand(p, "tempban " + other.getName()
					+ " 20 d &cYou have been banned for exploiting bugs/glitches found on ShadowLab");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco reset " + other.getName());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "clear " + other.getName());
			other.getInventory().setContents(null);
			other.getInventory().setArmorContents(null);
			other.getEnderChest().setContents(null);
			p.closeInventory();
			break;
		}
		case BED: {
			Bukkit.dispatchCommand(p,
					"tempban " + other.getName() + " 45 d &cYou have been banned for refusing to screenshare");
			p.closeInventory();
			break;
		}
		case RAW_FISH: {
			Bukkit.dispatchCommand(p, "tempban " +  other.getName() + " 45 d &cYou have been banned for blacklisted modifications (Screenshared)");
			p.closeInventory();
			break;
		}
		case TNT: {
			Bukkit.dispatchCommand(p, "tempban " +  other.getName() + "5 d &cYou have been banned for griefing.");
			p.closeInventory();
			break;
		}
		}
		p.closeInventory();
		info.remove(p);
		////////

	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e){
		if(e.getInventory().equals(inv)){
			info.remove(e.getPlayer());
		}
	}

}
