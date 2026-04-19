package knockwars;

import knockwars.commands.KbCommand;
import knockwars.listeners.FallDamageListener;
import knockwars.listeners.LeaveItemListener;
import knockwars.listeners.PlayerDeathListener;
import knockwars.listeners.VoidListener;
import knockwars.listeners.WorldChangeListener;
import knockwars.managers.SpawnManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashSet;
import java.util.Set;

public class Main extends JavaPlugin {

    private static Main instance;
    private SpawnManager spawnManager;
    private Set<Player> playersInGame;

    @Override
    public void onEnable() {
        instance = this;
        playersInGame = new HashSet<>();

        // Créer le dossier du plugin s'il n'existe pas
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Charger/créer la configuration par défaut
        saveDefaultConfig();
        reloadConfig();
        
        spawnManager = new SpawnManager(this);

        getCommand("kb").setExecutor(new KbCommand(this));

        getServer().getPluginManager().registerEvents(new VoidListener(this), this);
        getServer().getPluginManager().registerEvents(new LeaveItemListener(this), this);
        getServer().getPluginManager().registerEvents(new FallDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldChangeListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);

        getLogger().info("KnockWars est en fonctionnement.");
        
        // Recharger le spawn après une courte attente pour s'assurer que tous les mondes sont chargés
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            spawnManager.loadSpawn();
            if (spawnManager.hasSpawn()) {
                getLogger().info("✓ Spawn KnockWars chargé avec succès !");
            } else {
                getLogger().warning("⚠ Attention : Spawn KnockWars non configuré. Utilisez /kb setspawn");
            }
        }, 10L);
    }

    @Override
    public void onDisable() {
        getLogger().info("KnockWars est désactivé.");
    }

    public static Main getInstance() {
        return instance;
    }

    public SpawnManager getSpawnManager() {
        return spawnManager;
    }

    public Set<Player> getPlayersInGame() {
        return playersInGame;
    }

    public boolean isPlayerInGame(Player p) {
        return playersInGame.contains(p);
    }

    public void addPlayerInGame(Player p) {
        playersInGame.add(p);
    }

    public void removePlayerInGame(Player p) {
        playersInGame.remove(p);
    }

    /**
     * Fait quitter un joueur du jeu KnockWars
     */
    public void playerLeaveGame(Player p) {
        World mainWorld = Bukkit.getWorlds().get(0);
        p.teleport(mainWorld.getSpawnLocation());
        p.getInventory().clear();
        p.setGameMode(GameMode.SURVIVAL);
        removePlayerInGame(p);
        
        ConfigurationSection lang = getConfig().getConfigurationSection("lang");
        if (lang != null) {
            String prefix = lang.getString("prefix", "");
            String message = lang.getString("leave_message", "§cVous avez quitté KnockWars.");
            p.sendMessage(prefix + message);
        } else {
            p.sendMessage("§cVous avez quitté KnockWars.");
        }
    }
}
