package daibutz.minecraft.eventpusher;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventEncoder implements Listener {
	
	private EventPusher eventPusher;
	
	public EventEncoder(EventPusher eventPusher) {
		this.eventPusher = eventPusher;
	}

	/* Event Handlers */
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent e) {
		eventPusher.push("{\"event\":\"" + e.getEventName() + "\"," 
				+ "\"player\":\"" + e.getPlayer().getName() + "\","
				+ "\"join-message\":\"" + e.getJoinMessage() +"\""
				+ "}");
	}
}
