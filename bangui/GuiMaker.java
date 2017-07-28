package me.senpaiofficial.bangui;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiMaker {

	Inventory inv;

	public void setupInv() {
		inv = Bukkit.createInventory(null, 45, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "ShadowBans");

		ItemStack gray = MakeItem(Material.STAINED_GLASS_PANE, 1, (short) 7, " ", " ");
		ItemStack black = MakeItem(Material.STAINED_GLASS_PANE, 1, (short) 15, " ", "");
		for (int i = 0; i < 10; i++) {
			inv.setItem(i, gray);
		}
		inv.setItem(17, gray);
		inv.setItem(18, gray);
		inv.setItem(26, gray);
		inv.setItem(27, gray);
		for (int i = 35; i < 45; i++) {
			inv.setItem(i, gray);
		}
		for (int i = 19; i < 26; i++) {
			inv.setItem(i, black);
		}
		inv.setItem(11, black);
		inv.setItem(13, black);
		inv.setItem(15, black);
		inv.setItem(29, black);
		inv.setItem(31, black);
		inv.setItem(33, black);
		inv.setItem(10,
				MakeItem(Material.DIAMOND, 1, (short) 0, 
				ChatColor.RED + "X-Ray Hacking", 
				ChatColor.YELLOW + "30 Days",
				ChatColor.GRAY + "/tempban {player} 30 d &cYou were caught hacking using X-Ray"));

		inv.setItem(12, 
				MakeItem(Material.FEATHER, 1, (short) 0, 
				ChatColor.AQUA + "Fly|Speed|Jesus Hacking",
				ChatColor.YELLOW + "45 Days", 
				ChatColor.GRAY + "/tempban {player} 45 d &cYou were caught hacking ",
				ChatColor.GRAY + "using Movement Hacking (Fly|Speed|Jesus)"));

		inv.setItem(14,
				MakeItem(Material.DIAMOND_SWORD, 1, (short) 0, 
				ChatColor.GOLD + "Combat Related Hacking",
				ChatColor.YELLOW + "45 Days", 
				ChatColor.GRAY + "/tempban {player} 45 d &cYou were caught",
				ChatColor.GRAY + "hacking using Combat Related Hacking (KillAura|MobAura)"));

		inv.setItem(16,
				MakeItem(Material.SKULL_ITEM, 1, (short) 4, 
				ChatColor.DARK_PURPLE + "Player/Community Disrespect",
				ChatColor.YELLOW + "7 Days", 
				ChatColor.GRAY + "/tempban {player} 7 d &cYou have been",
				ChatColor.GRAY + "banned for disrespect towards the community of our server."));

		inv.setItem(22,
				MakeItem(Material.FURNACE, 1, (short) 0, 
				ChatColor.DARK_AQUA + "Hacking Admitance",
				ChatColor.YELLOW + "20 Days", 
				ChatColor.GRAY + "/tempban {player} 20 d &cYou have been banned for",
				ChatColor.GRAY + "20 days (decreased time) for admitting to rule breaking."));
		
		inv.setItem(28,
				MakeItem(Material.ENDER_CHEST, 1, (short) 0, 
				ChatColor.DARK_GREEN + "Exploiting",
				ChatColor.YELLOW + "20 Days", 
				ChatColor.GRAY + "/tempban {player} 20 d &cYou have been",
				ChatColor.GRAY + "banned for exploiting bugs/glitches found",
				ChatColor.GRAY	+ "in ShadowLabs."));
		
		inv.setItem(30,
				MakeItem(Material.BED, 1, (short) 0, 
				ChatColor.DARK_RED + "Screenshare Refusal",
				ChatColor.YELLOW + "45 Days", 
				ChatColor.GRAY + "/tempban {player} 45 d &cYou have been",
				ChatColor.GRAY + "banned for refusing to screenshare."));
		
		inv.setItem(32,
				MakeItem(Material.RAW_FISH, 1, (short) 0, 
				ChatColor.DARK_GREEN + "Screenshared - Hacking",
				ChatColor.YELLOW + "45 Days", 
				ChatColor.GRAY + "/tempban {player} 45 d &cYou have been banned for",
				ChatColor.GRAY + "blacklisted modifications (Screenshared)"));
		
		inv.setItem(34,
				MakeItem(Material.TNT, 1, (short) 0, 
				ChatColor.RED + "Griefing",
				ChatColor.YELLOW + "5 Days", 
				ChatColor.GRAY + "/tempban {player} 5 d &cYou have been",
				ChatColor.GRAY + "banned for griefing "));
	}

	public static ItemStack MakeItem(Material mat, int amt, short shrt, String name, String... lore) {
		ItemStack item = new ItemStack(mat, amt, (short) shrt);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		item.setItemMeta(itemMeta);
		itemMeta.setLore(Arrays.asList(lore));
		item.setItemMeta(itemMeta);
		return item;
	}

	public Inventory getInventory() {
		return inv;
	}

}
