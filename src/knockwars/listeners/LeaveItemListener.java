package knockwars.listeners;

import knockwars.Main;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class LeaveItemListener implements Listener {

    private final Main plugin;

    public LeaveItemListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        // Ne réagir qu'à un usage volontaire (clic droit), pas à un clic gauche
        // (ex: taper un mob/bloc en tenant la porte en main ne doit pas faire quitter la partie)
        Action action = e.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = e.getItem();
        if (item == null) return;
        // Vérifier si c'est une porte
        if (item.getType() != Material.WOOD_DOOR) return;
        if (!item.hasItemMeta() || item.getItemMeta() == null || !item.getItemMeta().hasDisplayName()) return;

        Player p = e.getPlayer();
        // Ignorer si le joueur n'est pas en jeu (évite de vider l'inventaire de quelqu'un
        // qui tiendrait par hasard un item avec le même nom hors partie)
        if (!plugin.isPlayerInGame(p)) return;

        ConfigurationSection lang = plugin.getConfig().getConfigurationSection("lang");
        if (lang == null) return;
        String leaveName = lang.getString("leave_item_name", "Quitter la partie");

        if (!item.getItemMeta().getDisplayName().equals(leaveName)) return;

        e.setCancelled(true);

        // Appeler directement la méthode du plugin pour éviter les conflits
        plugin.playerLeaveGame(p);
    }
}
