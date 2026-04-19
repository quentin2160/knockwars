package knockwars.listeners;

import knockwars.Main;
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
        String spawnWorldName = plugin.getConfig().getString("spawn.world");

        // Si le joueur est en jeu
        if (plugin.isPlayerInGame(p)) {
            // Si le joueur quitte le monde du spawn KnockWars
            if (spawnWorldName != null && !p.getWorld().getName().equals(spawnWorldName)) {
                // Le retirer automatiquement de la partie
                plugin.playerLeaveGame(p);
            }
        }
    }
}
