package knockwars.listeners;

import knockwars.Main;
import knockwars.managers.SpawnManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChangeListener implements Listener {

    private final Main plugin;

    public WorldChangeListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();

        // Si le joueur est en jeu et qu'il quitte le monde du spawn KnockWars,
        // le retirer automatiquement de la partie
        if (plugin.isPlayerInGame(p) && !plugin.getSpawnManager().isSpawnWorld(p.getWorld())) {
            plugin.playerLeaveGame(p);
        }
    }
}
