package knockwars.commands;

import knockwars.Main;
import knockwars.managers.SpawnManager;
import knockwars.utils.ItemBuilder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.PlayerInventory;


public class KbCommand implements CommandExecutor {

    private final Main plugin;

    public KbCommand(Main plugin) {
        this.plugin = plugin;
    }

    private String lang(String path) {
        ConfigurationSection lang = plugin.getConfig().getConfigurationSection("lang");
        if (lang == null) return ChatColor.RED + "Lang manquant.";
        String prefix = lang.getString("prefix", "");
        return prefix + lang.getString(path, "Message manquant: " + path);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(lang("help_header"));
            sender.sendMessage(lang("help_setspawn"));
            sender.sendMessage(lang("help_setkb"));
            sender.sendMessage(lang("help_reload"));
            sender.sendMessage(lang("help_join"));
            sender.sendMessage(lang("help_leave"));
            sender.sendMessage(lang("help_help"));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(lang("not_player"));
            return true;
        }

        Player p = (Player) sender;
        SpawnManager spawnManager = plugin.getSpawnManager();

        switch (args[0].toLowerCase()) {

            case "setspawn":
                if (!p.hasPermission("knockwars.setspawn")) {
                    p.sendMessage(lang("no_permission"));
                    return true;
                }
                spawnManager.setSpawn(p.getLocation());
                p.sendMessage(lang("spawn_set"));
                return true;

            case "join":
                if (!spawnManager.hasSpawn()) {
                    p.sendMessage(ChatColor.RED + "Le spawn KnockWars n'est pas défini.");
                    return true;
                }

                // Sauvegarder le GameMode précédent
                GameMode previousGameMode = p.getGameMode();
                p.setGameMode(GameMode.ADVENTURE);
                p.getInventory().clear();
                p.getInventory().setArmorContents(null);

                int kbLevel = plugin.getConfig().getInt("kb_level", 1);
                int punchLevel = plugin.getConfig().getInt("punch_level", 0);

                // Arc slot 0
                ItemBuilder bowBuilder = new ItemBuilder(Material.BOW)
                        .setName(ChatColor.GOLD + "Arc KnockWars")
                        .addEnchant(Enchantment.ARROW_INFINITE, 1, true)
                        .addFlag(ItemFlag.HIDE_ENCHANTS);
                if (punchLevel > 0) {
                    bowBuilder.addEnchant(Enchantment.ARROW_KNOCKBACK, punchLevel, true);
                }

                // Bâton KB slot 1
                ItemBuilder stickBuilder = new ItemBuilder(Material.STICK)
                        .setName(ChatColor.GOLD + "Bâton KB")
                        .addFlag(ItemFlag.HIDE_ENCHANTS);
                if (kbLevel > 0) {
                    stickBuilder.addEnchant(Enchantment.KNOCKBACK, kbLevel, true);
                }

                // Flèche dans l'inventaire caché (obligatoire pour infinité)
                ItemBuilder arrowBuilder = new ItemBuilder(Material.ARROW);

                // Porte Quitter slot 8
                ConfigurationSection langSection = plugin.getConfig().getConfigurationSection("lang");
                String leaveName = (langSection != null) ? langSection.getString("leave_item_name", "Quitter la partie") : "Quitter la partie";
                ItemBuilder leaveBuilder = new ItemBuilder(Material.WOOD_DOOR)
                        .setName(leaveName);

                PlayerInventory inv = p.getInventory();
                inv.setItem(0, bowBuilder.build());
                inv.setItem(1, stickBuilder.build());
                inv.setItem(8, leaveBuilder.build());
                inv.setItem(10, arrowBuilder.build()); // Flèche dans l'inventaire caché

                // Ajouter le joueur à la liste des joueurs en jeu
                plugin.addPlayerInGame(p);

                p.teleport(spawnManager.getSpawn());
                p.sendMessage(lang("join_teleport"));
                p.sendMessage(lang("join_inventory_set"));
                return true;

            case "leave":
                plugin.playerLeaveGame(p);
                return true;

            case "reload":
                if (!p.hasPermission("knockwars.admin")) {
                    p.sendMessage(lang("no_permission"));
                    return true;
                }

                // Recharger la configuration
                plugin.reloadConfig();
                // Recharger le spawn
                spawnManager.loadSpawn();

                p.sendMessage(lang("reload_success"));
                plugin.getLogger().info("Configuration rechargée par " + p.getName());
                return true;

            case "setkb":
                if (!p.hasPermission("knockwars.admin")) {
                    p.sendMessage(lang("no_permission"));
                    return true;
                }

                if (args.length < 3) {
                    p.sendMessage(lang("setkb_usage"));
                    return true;
                }

                String type = args[1].toLowerCase();
                String levelStr = args[2];
                int level;

                // Vérifier que le type est valide
                if (!type.equals("kb") && !type.equals("punch")) {
                    p.sendMessage(lang("setkb_invalid_type"));
                    return true;
                }

                // Vérifier que le niveau est valide
                try {
                    level = Integer.parseInt(levelStr);
                    if (level < 0) {
                        p.sendMessage(lang("setkb_invalid_level"));
                        return true;
                    }
                } catch (NumberFormatException e) {
                    p.sendMessage(lang("setkb_invalid_level"));
                    return true;
                }

                // Définir la valeur dans la configuration
                if (type.equals("kb")) {
                    plugin.getConfig().set("kb_level", level);
                    p.sendMessage(lang("setkb_kb_set").replace("{level}", String.valueOf(level)));
                } else {
                    plugin.getConfig().set("punch_level", level);
                    p.sendMessage(lang("setkb_punch_set").replace("{level}", String.valueOf(level)));
                }

                // Sauvegarder et recharger la configuration
                plugin.saveConfig();
                plugin.reloadConfig();
                plugin.getLogger().info("Configuration mise à jour : " + type + "_level = " + level + " par " + p.getName());
                return true;

            default:
                sender.sendMessage(lang("help_header"));
                return true;
        }
    }
}
