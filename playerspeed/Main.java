package me.senpaiofficial.playerspeed;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	ItemStack on = MakeItem(Material.GLOWSTONE_DUST, 1, (short) 0, ChatColor.GREEN + "Speed Enabled");
	ItemStack off = MakeItem(Material.REDSTONE, 1, (short) 0, ChatColor.RED + "Speed Disabled");

	ArrayList<Player> spammers = new ArrayList<>();

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		lessSpeed(p);
		Inventory pinv = p.getInventory();
		pinv.setItem(3, off);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (p.getItemInHand().equals(on) || p.getItemInHand().equals(off)) {
				e.setCancelled(true);
				if (spammers.contains(p)) {
					p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Please wait before toggling!");
					return;
				}

				switch (p.getItemInHand().getType()) {
				default: {
					break;
				}
				case REDSTONE: {
					moreSpeed(p);
					spammers.add(p);
					p.setItemInHand(on);

					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
						public void run() {
							spammers.remove(p);
						}
					}, 100L);
					break;
				}
				case GLOWSTONE_DUST: {
					lessSpeed(p);
					spammers.add(p);
					p.setItemInHand(off);
					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
						public void run() {
							spammers.remove(p);
						}
					}, 100L);
					break;
				}
				}

			}
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onCLick(InventoryClickEvent e) throws NullPointerException {
		e.setCancelled(true);
	}

	public void moreSpeed(Player p) {
		p.setWalkSpeed(0.5f);
	}

	public void lessSpeed(Player p) {
		p.setWalkSpeed(0.2f);
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

}
