package daibutz.minecraft.eventpusher;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class EventPusher extends JavaPlugin {
	
	private Server server;
	private EventEncoder eventEncoder;
	
	@Override
	public void onEnable() {
		server = new Server();
		server.startListening();
		
		eventEncoder = new EventEncoder(this);
		this.getServer().getPluginManager().registerEvents(eventEncoder, this);
	}
	
	@Override
	public void onDisable() {
		HandlerList.unregisterAll(eventEncoder);
		server.stopListening();
	}
	
	public void push(String jsonMessage) {
		if (getConfig().getBoolean("debug.print-to-console"))
			getLogger().info(jsonMessage);
		server.sendToAllClients(jsonMessage);
	}
}
