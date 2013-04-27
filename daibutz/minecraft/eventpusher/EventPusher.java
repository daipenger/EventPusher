package daibutz.minecraft.eventpusher;

import org.bukkit.plugin.java.JavaPlugin;

public class EventPusher extends JavaPlugin {
	
	private Server server;
	
	@Override
	public void onEnable() {
		server = new Server();
		server.startListening();
	}
	
	@Override
	public void onDisable() {
		server.stopListening();
	}
}
