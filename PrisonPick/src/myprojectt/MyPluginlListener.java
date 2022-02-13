package myprojectt;



import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.cache.PlayerCachePlayerData;
import tech.mcprison.prison.spigot.api.PrisonSpigotAPI;
import tech.mcprison.prison.spigot.autofeatures.events.AutoManagerTokenEnchant;

public class MyPluginlListener implements Listener {
	
	private Plugin plugin;
	private Random random = new Random();
	public MyPluginlListener(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		event.setJoinMessage("your not welcome");
	}
	@EventHandler
	public void onhotbarchange(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		if(event.getPlayer().getInventory().getItem(event.getPreviousSlot()).getType() == null) {
			return;
		}
		//int cuslvl = getCustomEnchlvl(player,"Haste");
		else if(event.getPlayer().getInventory().getItem(event.getPreviousSlot()).getType() != Material.DIAMOND_PICKAXE) {
			player.sendMessage("no");
			if(event.getPlayer().hasPotionEffect(PotionEffectType.FAST_DIGGING))
				event.getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING);
		}else if(event.getPlayer().getInventory().getItem(event.getNewSlot()).getType() == Material.DIAMOND_PICKAXE) {
			player.sendMessage("yes");
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,Integer.MAX_VALUE, 2,true,true,true));
		}
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		ItemStack item = player.getInventory().getItemInMainHand();
		if(item.getType() == Material.DIAMOND_PICKAXE) {
			int cuslvl = getCustomEnchlvl(player, "Explosion");
			if(cuslvl > 0) {
				/*TNTPrimed primed = (TNTPrimed)player.getWorld().spawnEntity(block.getLocation(), EntityType.PRIMED_TNT);
				primed.setFuseTicks(-1);
				//primed.setSilent(true);
				primed.setYield(cuslvl/10);*/
				if(random.nextInt(2)== 1)player.getWorld().createExplosion(block.getLocation(), cuslvl/20);
			}
		}
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		Material pick = Material.DIAMOND_PICKAXE;
		if((item != null) && (item.getType() == pick)) {
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK||event.getAction() == Action.RIGHT_CLICK_AIR) {
				//player.setWindowProperty(null, 0);
				this.player1 = player;
				createGui(1,"Pickaxe-Upgrader");
				player.openInventory(inv);
			}
			
		}
	}
	private Inventory inv;

    public void createGui(int id,String name) {
        inv = Bukkit.createInventory(null, InventoryType.CHEST,name);
        initializeItems(id);
    }
    Player player1;
    // You can call this whenever you want to put the items in
    public void initializeItems(int id) {
    	if(id == 2) {
    		inv.setItem(0, createGuiItem(Material.ENCHANTED_BOOK, "§b§l[1] Efficiency", " ","§5Cost: §c" + 100 + " Tokens","§dCurrent-Level: §c" + getEnchantedLevel(player1, Enchantment.DIG_SPEED)," ","§6Max-Level: §c" + 33,"§4+ Left Click To Upgrade x1","§4+ Right Click To Upgrade x10"));
    		inv.setItem(1, createGuiItem(Material.FLINT, "§b§l[2] Fortune", " ","§5Cost: §c" + 100 + " Tokens","§dCurrent-Level: §c" + getEnchantedLevel(player1, Enchantment.LOOT_BONUS_BLOCKS)," ","§6Max-Level: §c" + 25,"§4+ Left Click To Upgrade x1","§4+ Right Click To Upgrade x10"));
    		inv.setItem(2, createGuiItem(Material.TNT, "§b§l[3] Explosion", " ","§5Cost: §c" + 100 + " Tokens","§dCurrent-Level: §c" + explolvl," ","§6Max-Level: §c" + 100,"§4+ Left Click To Upgrade x1","§4+ Right Click To Upgrade x10"));
    		inv.setItem(3, createGuiItem(Material.BEACON, "§b§l[4] Haste", " ","§5Cost: §c" + 100 + " Tokens","§dCurrent-Level: §c" + hastelvl," ","§6Max-Level: §c" + 10,"§4+ Left Click To Upgrade x1","§4+ Right Click To Upgrade x10"));
    		inv.setItem(3, createGuiItem(Material.LEATHER_BOOTS, "§b§l[5] Speed", " ","§5Cost: §c" + 100 + " Tokens","§dCurrent-Level: §c" + hastelvl," ","§6Max-Level: §c" + 10,"§4+ Left Click To Upgrade x1","§4+ Right Click To Upgrade x10"));
    		return;
    	}
    	for(int i = 0;i<inv.getSize();i++) {
        	inv.setItem(i,createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        	if(i == 11)inv.setItem(11,createGuiItem(Material.ENCHANTED_BOOK, "Token - Enchant", "§a§lClick Here To Enchant Your Pickaxe"));
    	}
    }

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        
        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);

        return item;
    }

    public void openInventory(HumanEntity ent) {
        ent.openInventory(inv);
    }
    int explolvl;
    int hastelvl;
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory() != inv &&e.getCurrentItem() == null&&e.getCurrentItem().getType().isAir()) return;
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        
        int eff = p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.DIG_SPEED);
        int fortune = p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        if(e.getRawSlot() == 11&&e.getView().getTitle().equalsIgnoreCase("Pickaxe-Upgrader")) {
        	createGui(2,"Token-Enchanter");
        	p.openInventory(inv);
        	return;
        }
        if(e.getCurrentItem().getType() == Material.ENCHANTED_BOOK) {
        	
        	//check the token then deduct from it how much i want	
        	p.getInventory().getItemInMainHand().addUnsafeEnchantment(enchant("efficiency"),getEnchantedLevel(p,enchant("efficiency"))+1);
        	if(e.isRightClick())p.getInventory().getItemInMainHand().addUnsafeEnchantment(enchant("efficiency"), getEnchantedLevel(p,enchant("efficiency"))+9);
        	p.sendMessage("§aYour Efficiency Level Is Upgraded To + "+getEnchantedLevel(p,enchant("efficiency"))+".");
            List<String> lore = p.getInventory().getItemInMainHand().getItemMeta().getLore();
            ItemStack is = p.getInventory().getItemInMainHand();
            ItemMeta im = is.getItemMeta();
            if(eff == 0)lore.add("§6Efficiency-"+getEnchantedLevel(p,enchant("efficiency")));//ChatColor.DARK_PURPLE
            else{
            	for(int i = 4;i<lore.size();i++) {
            		if(lore.get(i).contains("Efficiency")) {
            			lore.set(i, "§6Efficiency-"+getEnchantedLevel(p,enchant("efficiency")));
            		}
            	}
            }
            im.setLore(lore);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            is.setItemMeta(im);
            initializeItems(2);
        }else if(e.getCurrentItem().getType() == Material.FLINT) {
        	p.getInventory().getItemInMainHand().addUnsafeEnchantment(enchant("fortune"), getEnchantedLevel(p,enchant("fortune"))+1);
        	if(e.isRightClick())p.getInventory().getItemInMainHand().addUnsafeEnchantment(enchant("fortune"), getEnchantedLevel(p,enchant("fortune"))+9);
        	p.sendMessage("§aYour Fortune Level Is Upgraded To + "+getEnchantedLevel(p,enchant("fortune"))+".");
        	List<String> lore = p.getInventory().getItemInMainHand().getItemMeta().getLore();
            ItemStack is = p.getInventory().getItemInMainHand();
            ItemMeta im = is.getItemMeta();
            if(fortune == 0)lore.add("§6Fortune-"+getEnchantedLevel(p,enchant("fortune")));//ChatColor.DARK_PURPLE
            else {
            	for(int i = 4;i<lore.size();i++) {
            		if(lore.get(i).contains("Fortune")) 
            			lore.set(i, "§6Fortune-"+getEnchantedLevel(p,enchant("fortune")));
            	}
            }
            im.setLore(lore);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            is.setItemMeta(im);
            initializeItems(2);
        }else if(e.getCurrentItem().getType() == Material.TNT) {
        	List<String> lore = p.getInventory().getItemInMainHand().getItemMeta().getLore();
        	ItemStack is = p.getInventory().getItemInMainHand();
            ItemMeta im = is.getItemMeta();
        	for(int i = 4;i<lore.size();i++) {
        		if(lore.get(i).contains("Explosion")) {
        			explolvl = Integer.parseInt(lore.get(i).substring(12));
        			if(explolvl<100)explolvl++;
        			lore.set(i,"§6Explosion-"+ explolvl);
        			if(e.isRightClick()) {
        				if(explolvl<90)explolvl = explolvl + 9;
        				lore.set(i,"§6Explosion-"+explolvl);
        			}
        			break;
        		} else if(i == lore.size()-1) {
        			explolvl = 0;
        			explolvl++;
        			if(e.isRightClick())explolvl = explolvl + 9;
        			lore.add("§6Explosion-"+ explolvl);
        			break;
        		}
        	}
        	p.sendMessage("§aYour Explosion Level Is Upgraded To + "+explolvl+".");
            im.setLore(lore);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            is.setItemMeta(im);
            initializeItems(2);
        }else if(e.getCurrentItem().getType() == Material.BEACON) {
        	List<String> lore = p.getInventory().getItemInMainHand().getItemMeta().getLore();
        	ItemStack is = p.getInventory().getItemInMainHand();
            ItemMeta im = is.getItemMeta();
        	for(int i = 4;i<lore.size();i++) {
        		if(lore.get(i).contains("Haste")) {
        			hastelvl = Integer.parseInt(lore.get(i).substring(7+1));
        			if(hastelvl<10)hastelvl++;
        			lore.set(i,"§6Haste-"+ hastelvl);
        			if(e.isRightClick()) {
        				if(explolvl<0)hastelvl = hastelvl + 9;
        				lore.set(i,"§6Haste-"+hastelvl);
        			}
        			break;
        		} else if(i == lore.size()-1) {
        			hastelvl = 0;
        			hastelvl++;
        			if(e.isRightClick())hastelvl = hastelvl + 9;
        			lore.add("§6Haste-"+ hastelvl);
        			break;
        		}
        	}
        	p.sendMessage("§aYour Haste Level Is Upgraded To + "+hastelvl+".");
            im.setLore(lore);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            is.setItemMeta(im);
            initializeItems(2);
        }
    }
    @EventHandler	
    public void onInventoryClick(InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
          e.setCancelled(true);
        }
    }
    public int getEnchantedLevel(Player p,Enchantment ench) {
		return p.getInventory().getItemInMainHand().getEnchantmentLevel(ench);
    }
    public Enchantment enchant(String enchant) {
    	if(enchant == "efficiency") return Enchantment.DIG_SPEED;
    	if(enchant == "fortune") return Enchantment.LOOT_BONUS_BLOCKS;
		return null;
    }
    //player must me holding lored pickaxe
    public int getCustomEnchlvl(Player p,String size) {
    	List<String> lore = p.getInventory().getItemInMainHand().getItemMeta().getLore();
    	for(int i = 4;i<lore.size();i++) {
    		if(lore.get(i).contains(size)) return Integer.parseInt(lore.get(i).substring(size.length()+3));
    		else if(i == lore.size()-1){return -1;}
    	}
    	return - 1;
    }
}
