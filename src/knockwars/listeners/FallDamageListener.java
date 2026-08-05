package knockwars.listeners;

import knockwars.Main;
import knockwars.managers.SpawnManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class FallDamageListener implements Listener {

    private final Main plugin;

    public FallDamageListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFall(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            Player p = (Player) e.getEntity();
            // Désactiver les dégâts de chute uniquement pour les joueurs en jeu
            // ET sur le monde du spawn KnockWars
            SpawnManager spawnManager = plugin.getSpawnManager();
            if (plugin.isPlayerInGame(p) && spawnManager.isSpawnWorld(p.getWorld())) {
                e.setCancelled(true);
            }
        }
    }
}
