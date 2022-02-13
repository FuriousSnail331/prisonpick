package myprojectt;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.cache.PlayerCachePlayerData;

public class Plugin extends JavaPlugin{

    private static Economy econ = null;

	@Override
	public void onEnable() {
		if (!setupEconomy() ) {
			System.out.println("no ecooooooooooooooooooooooooooooooooooooo found :/");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		getCommand("token").setExecutor(new Tokens());
		econ.createBank("", Bukkit.getServer().getPlayer("w"));
	    //Bukkit.getPluginManager().registerEvents(new BreakBlockListener(), this);

		Bukkit.getPluginManager().registerEvents(new MyPluginlListener(this), this); 
	} 
	 private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	@Override
	public void onDisable() {
		
	}
	PluginManager p = Bukkit.getPluginManager();
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String cname = command.getName();
		return super.onCommand(sender, command, label, args);
			/*if(sender instanceof Player) {
				Player player = (Player)sender;
			
			}*/
	}
	public static Economy getEconomy() {
        return econ;
    }

}
