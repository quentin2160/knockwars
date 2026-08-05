package knockwars.listeners;

import knockwars.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final Main plugin;

    public PlayerDeathListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        
        // Vérifier si le joueur est en jeu
        if (!plugin.isPlayerInGame(p)) {
            return;
        }

        // Vérifier que le joueur est sur le monde du spawn
        String spawnWorldName = plugin.getConfig().getString("spawn.world");
        if (spawnWorldName == null || !p.getWorld().getName().equals(spawnWorldName)) {
            return;
        }

        // Supprime le message de mort par défaut
        e.setDeathMessage(null);

        // Récupérer le killer
        Player killer = p.getKiller();

        // Construire le message de mort
        ConfigurationSection lang = plugin.getConfig().getConfigurationSection("lang");
        String prefix = (lang != null) ? lang.getString("prefix", "§6[§eKnockWars§6] §r") : "§6[§eKnockWars§6] §r";
        String deathMessage;
        
        if (killer != null) {
            String template = (lang != null) ? lang.getString("player_death_by_player", "§cVous avez été tué par {killer} !") : "§cVous avez été tué par {killer} !";
            deathMessage = template.replace("{killer}", killer.getName());
        } else {
            deathMessage = (lang != null) ? lang.getString("player_death_other", "§cVous êtes mort !") : "§cVous êtes mort !";
        }

        // Envoyer le message immédiatement
        p.sendMessage(prefix + deathMessage);
        
        // Envoyer un message au tueur (si existe)
        if (killer != null) {
            killer.sendMessage(prefix + "§aVous avez tué " + p.getName() + " !");
        }
        
        // Broadcaster à tous les joueurs en jeu dans le monde
        for (Player player : p.getWorld().getPlayers()) {
            if (player != p && player != killer && plugin.isPlayerInGame(player)) {
                player.sendMessage(prefix + "§7" + p.getName() + " a été tué par " + (killer != null ? killer.getName() : "l'environnement") + ".");
            }
        }
    }
}
