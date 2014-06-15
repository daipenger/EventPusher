package daibutz.minecraft.eventpusher;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class EventPusher extends JavaPlugin {

	private Server server;
	private EventEncoder eventEncoder;

	public EventPusher() {
		// Save the predefined and default config into the plugin folder if it is not there
		saveDefaultConfig();

		FileConfiguration config = super.getConfig();
		server = new Server(config.getInt("port", 7112), config.getInt("max-clients", 1));
		eventEncoder = new EventEncoder(this);
	}

	@Override
	public void onEnable() {
		server.startListening();
		super.getServer().getPluginManager().registerEvents(eventEncoder, this);
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
