package me.bungeecore.coinsapi.listener;

import me.bungeecore.coinsapi.CoinsAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerLoginListener implements Listener {

    @EventHandler
    public void onLogin(PlayerJoinEvent e) {
        CoinsAPI.getInstance().registerPlayer(e.getPlayer().getName(), e.getPlayer().getUniqueId());
    }
}
