package at.mcnetwork.lausi;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;



public class HideAndCustomPlugins extends JavaPlugin implements Listener {
	
	
	ProtocolManager protocolManager;
	public ArrayList<String> plugins = new ArrayList();
	private Logger log;
	String version;
	String name;
	
	public void onEnable() {
		version = getDescription().getVersion();
		name = getDescription().getName();
		saveDefaultConfig();
		loadConfig();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	    this.protocolManager = ProtocolLibrary.getProtocolManager();
	    this.protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Client.TAB_COMPLETE })
	    {
	      public void onPacketReceiving(PacketEvent event) {
	        if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE)
	          try {
	            if (event.getPlayer().hasPermission("hideandcustomplugins.bypass"))
	            {
	              return;
	            }PacketContainer packet = event.getPacket();
	            String message = ((String)packet.getSpecificModifier(String.class).read(0)).toLowerCase();

	            if (((message.startsWith("/")) && (!message.contains(" "))) 
	            		|| ((message.startsWith("/ver")) && (!message.contains("  "))) 
	            		|| ((message.startsWith("/version")) && (!message.contains("  "))) 
	            		|| ((message.startsWith("/?")) && (!message.contains("  "))) 
	            		|| ((message.startsWith("/about")) && (!message.contains("  "))) 
	            		|| ((message.startsWith("/help")) && (!message.contains("  "))))
	            {
	              event.setCancelled(true);
	            }
	          } catch (FieldAccessException e) { HideAndCustomPlugins.this.getLogger().log(Level.SEVERE, "Couldn't access field.", e); }

	      }
	    });
	    for (String s : getConfig().getString("HideAndCustomPlugins.plugins").split(", ")) {
		      this.plugins.add(s);
		    }
		Logger.getLogger("Minecraft").info("[" + name + "] Version: " + version + " Plugin has been activated successfully.");
	}
	
	public void onDisable() {
		this.saveConfig();
		Logger.getLogger("Minecraft").info("[" + name + "] Version: " + version + " Plugin was disabled successfully.");
	}
	
	private void loadConfig() {
		FileConfiguration cfg = this.getConfig();
		cfg.options().copyDefaults(true);
		this.saveConfig();
		Logger.getLogger("Minecraft").info("[" + name + "] Version: " + version+ " Successfully loaded config.yml");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	  public void onCommand(PlayerCommandPreprocessEvent event) {
	    boolean plugins = event.getMessage().equalsIgnoreCase("/plugins");
	    boolean pl = event.getMessage().equalsIgnoreCase("/pl");
	    boolean gc = event.getMessage().equalsIgnoreCase("/gc");
	    boolean unknown = event.getMessage().equalsIgnoreCase("/?");
	    boolean version = event.getMessage().equalsIgnoreCase("/version");
	    boolean ver = event.getMessage().equalsIgnoreCase("/ver");
	    boolean bukkitplugin = event.getMessage().equalsIgnoreCase("/bukkit:plugins");
	    boolean about = event.getMessage().equalsIgnoreCase("/about");
	    boolean a = event.getMessage().equalsIgnoreCase("/a");
	    boolean bukkitversion = event.getMessage().equalsIgnoreCase("/bukkit:version");
	    Player player = event.getPlayer();
	    if ((plugins) || (pl) ||  (unknown) ||  (bukkitplugin)) {
	    	if(!player.hasPermission("hideandcustomplugins.bypass")){
	      event.setCancelled(true);
	      String defaultMessage = "§a";
	      for (String plugin : this.plugins) {
	        defaultMessage = defaultMessage + plugin + ", ";
	      }
	      defaultMessage = defaultMessage.substring(0, defaultMessage.lastIndexOf(", "));
	      player.sendMessage(ChatColor.WHITE + "Plugins(" + this.plugins.size() + "): " + ChatColor.GREEN + defaultMessage.replaceAll(", ", new StringBuilder().append(ChatColor.WHITE).append(", ").append(ChatColor.GREEN).toString()));
	    }
	    }
	  
	
	if ((version) || (ver) ||  (gc) ||  (a) ||  (about) ||  (bukkitversion)) {
    	if(!player.hasPermission("hideandcustomplugins.bypass")){
    		Player p = event.getPlayer();
      event.setCancelled(true);
      p.sendMessage(getConfig().getString("HideAndCustomPlugins.hideversion").replaceAll("&", "§"));
    }
    }
  }
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	  {
	    Player p = null;
	    if ((sender instanceof Player)) {
	      p = (Player)sender;
	    }
	    
	    if (cmd.getName().equalsIgnoreCase("hcpreload")) {
		      if (p != null) {
		        if (p.hasPermission("hideandcustomplugins.reload")) {
		          reloadConfig();
		          p.sendMessage(ChatColor.GREEN + "Reloaded " + getDescription().getName() + " config.yml!");
		          return true;
		        }
		      }
		      else {
		        sender.sendMessage("This command is not supported for the console.");
		      }
		      return true;
		    }
	
	if (cmd.getName().equalsIgnoreCase("hcp"))
    {
      if (p != null)
      {
        p.sendMessage("§e==========[ HideAndCustomPlugins Help Version: " + ChatColor.YELLOW + version + " §e]==========");
        p.sendMessage(ChatColor.GREEN + "Hy " + p.getDisplayName() + ChatColor.GREEN + "!");
        p.sendMessage("§9/help HideAndCustomPlugins §aShows you all the commands that are available.\n");
        p.sendMessage("§aHideAndCustomPlugins protects the server against pluginthieves");
        p.sendMessage("§5Version: " + ChatColor.DARK_PURPLE + version);
        p.sendMessage("§5Created by: " + ChatColor.DARK_PURPLE + "lausi1793");
        p.sendMessage("§e==========[ HideAndCustomPlugins Help Version: " + ChatColor.YELLOW + version + " §e]==========");

        return true;
      }else{
      sender.sendMessage("This command is not supported for the console.");
      }
    }
	
	return false;
	 }

	

}