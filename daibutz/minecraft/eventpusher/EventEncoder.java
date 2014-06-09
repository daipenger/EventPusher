package daibutz.minecraft.eventpusher;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonArray;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonObject;

public class EventEncoder implements Listener {
	
	private EventPusher eventPusher;
	
	public EventEncoder(EventPusher eventPusher) {
		this.eventPusher = eventPusher;
	}
	
	/* Helpers */
	
	public static JsonObject encodePlayer(Player p) {
		JsonObject j = new JsonObject();
		j.addProperty("name", p.getName());
		j.addProperty("display-name", p.getDisplayName());
		j.addProperty("is-op", p.isOp());
		j.addProperty("gamemode", p.getGameMode().name());
		j.addProperty("level", p.getLevel());
		return j;
	}
	
	public static JsonObject encodeBlock(Block b) {
		JsonObject j = new JsonObject();
		j.addProperty("x", b.getX());
		j.addProperty("y", b.getY());
		j.addProperty("z", b.getZ());
		
		j.addProperty("type", b.getType().toString());
		return j;
	}

	/* Event Handlers */
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent e) {
		JsonObject j = new JsonObject();
		j.addProperty("event", e.getEventName());
		j.add("player", encodePlayer(e.getPlayer()));
		j.addProperty("message", e.getJoinMessage());
		
		eventPusher.push(j.toString());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent e) {
		JsonObject j = new JsonObject();
		j.addProperty("event", e.getEventName());
		j.add("player", encodePlayer(e.getPlayer()));
		j.addProperty("message", e.getQuitMessage());
		
		eventPusher.push(j.toString());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		JsonObject j = new JsonObject();
		j.addProperty("event", e.getEventName());
		j.add("player", encodePlayer(e.getPlayer()));
		JsonArray recipients = new JsonArray();
		for (Player p : e.getRecipients()) 
			recipients.add(encodePlayer(p));
		j.add("recipients", recipients);
		j.addProperty("format", e.getFormat());
		j.addProperty("message", e.getMessage());
		
		eventPusher.push(j.toString());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent e) {
		JsonObject j = new JsonObject();
		j.addProperty("event", e.getEventName());
		j.add("player", encodePlayer(e.getPlayer()));
		j.add("block", encodeBlock(e.getBlock()));
		
		eventPusher.push(j.toString());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerVelocity(PlayerVelocityEvent e) {
		JsonObject j = new JsonObject();
		j.addProperty("event", e.getEventName());
		j.add("player", encodePlayer(e.getPlayer()));
		j.addProperty("velocity", e.getVelocity().toString());
		
		eventPusher.push(j.toString());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent e) {
		JsonObject j = new JsonObject();
		j.addProperty("event", e.getEventName());
		j.add("player", encodePlayer(e.getEntity()));
		j.addProperty("message", e.getDeathMessage());
		
		eventPusher.push(j.toString());
	}
}