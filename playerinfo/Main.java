package me.senpaiofficial.playerinfo;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Main extends JavaPlugin implements PluginMessageListener, Listener {
	public static Economy econ = null;

	public static Economy getEcononomy() {
		return econ;
	}

	String servername;

	public void onEnable() {
		setupEconomy();
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("GetServer");

		// Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
		// Else, specify them
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

		player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		servername = in.readUTF();
	}

	private HashMap<Player, Inventory> pInv = new HashMap<Player, Inventory>();

	public int getPing(Player who) {
		try {
			// Building the version of the server in such a form we can use it
			// in NMS code.
			String bukkitversion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
			Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + bukkitversion + ".entity.CraftPlayer");
			// Invoking method getHandle() for the player
			Object handle = craftPlayer.getMethod("getHandle").invoke(who);
			// Getting field "ping" that holds player's ping obviously
			Integer ping = (Integer) handle.getClass().getDeclaredField("ping").get(handle);
			// Returning the ping
			return ping.intValue();
		} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			// Handle exceptions however you like, i chose to return value of
			// -1; since player's ping can't be -1.
			return -1;
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Inventory inv = Bukkit.getServer().createInventory(e.getPlayer(), 27,
				ChatColor.GREEN + "" + ChatColor.BOLD + "Player Statistics");

		pInv.put(e.getPlayer(), inv);
		makeInventory(e.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		pInv.remove(e.getPlayer());
	}

	public void makeInventory(Player p) {
		Inventory inv = pInv.get(p);

		if (inv == null) {
			return;
		}

		PermissionUser user = PermissionsEx.getUser(p);
		List<String> groups = user.getParentIdentifiers();
		for (int i = 0; i < 10; i++) {
			inv.setItem(i,
					MakeItem(Material.STAINED_GLASS_PANE, 1, (short) 7,
							ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "----" + ChatColor.GOLD + " ShadowLabs "
									+ ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "----"));
		}
		for (int i = 0; i < 27; i++) {
			inv.setItem(i,
					MakeItem(Material.STAINED_GLASS_PANE, 1, (short) 7,
							ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "----" + ChatColor.GOLD + " ShadowLabs "
									+ ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "----"));
		}

		inv.setItem(11, MakeItem(Material.COMMAND, 1, (short) 0, ChatColor.GREEN + "Server Info",
				ChatColor.YELLOW + "You are playing on:", ChatColor.BLUE + "" + ChatColor.BOLD + servername));

		inv.setItem(12, MakeItem(Material.ENDER_PEARL, 1, (short) 0, ChatColor.YELLOW + "Ping",
				ChatColor.RED + "" + getPing(p)));
		inv.setItem(13,
				MakeItem(Material.SKULL_ITEM, 1, (short) 3, ChatColor.AQUA + "" + ChatColor.BOLD + "Online Players",
						ChatColor.GREEN + "" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers()));
		inv.setItem(14, MakeItem(Material.GOLD_INGOT, 1, (short) 0, ChatColor.GREEN + "Balance",
				ChatColor.YELLOW + "$" + econ.getBalance(p)));
		inv.setItem(15,
				MakeItem(Material.SIGN, 1, (short) 0, ChatColor.BLUE + "Rank", ChatColor.YELLOW + groups.get(0)));
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		if (!(cmd.getName().equalsIgnoreCase("info"))) {
			return true;
		}
		if (sender instanceof Player) {
			Player p = (Player) sender;
			Inventory inv = pInv.get(p);
			if (inv == null) {
				p.sendMessage(ChatColor.RED + "An error has occoured. Relog to fix this.");
				return true;
			}
			p.playSound(p.getLocation(), Sound.NOTE_PLING, 10, 10);
			makeInventory(p);
			p.openInventory(inv);

		} else {
			sender.sendMessage("no");
		}

		return false;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Inventory inv = pInv.get(e.getWhoClicked());
		if (e.getInventory().equals(inv)
				|| e.getInventory().getName().equals(ChatColor.stripColor("Player Statistics"))) {
			e.setCancelled(true);
		}
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

	private void setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return;
		}
		econ = rsp.getProvider();
		return;
	}

}
