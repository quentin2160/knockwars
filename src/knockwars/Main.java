package knockwars;

import knockwars.commands.KbCommand;
import knockwars.listeners.FallDamageListener;
import knockwars.listeners.ItemProtectionListener;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Main extends JavaPlugin {

    private static Main instance;
    private SpawnManager spawnManager;
    private Set<Player> playersInGame;
    // GameMode du joueur avant qu'il ne rejoigne la partie, pour le lui restaurer en sortant
    private final Map<UUID, GameMode> previousGameModes = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        playersInGame = Collections.synchronizedSet(new HashSet<>());

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
        getServer().getPluginManager().registerEvents(new ItemProtectionListener(this), this);

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
        instance = null;
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
     * Mémorise le GameMode d'un joueur avant qu'il ne rejoigne la partie,
     * pour pouvoir le lui restaurer quand il quitte.
     */
    public void rememberGameMode(Player p, GameMode mode) {
        previousGameModes.put(p.getUniqueId(), mode);
    }

    /**
     * Fait quitter un joueur du jeu KnockWars.
     * Ne fait rien si le joueur n'est pas actuellement en jeu, pour éviter
     * de vider l'inventaire d'un joueur qui n'a jamais rejoint la partie.
     */
    public void playerLeaveGame(Player p) {
        if (!isPlayerInGame(p)) {
            return;
        }

        World lobbyWorld = resolveLobbyWorld();
        p.teleport(lobbyWorld.getSpawnLocation());
        p.getInventory().clear();

        // Restaurer le GameMode que le joueur avait avant de rejoindre KnockWars
        GameMode previousGameMode = previousGameModes.remove(p.getUniqueId());
        p.setGameMode(previousGameMode != null ? previousGameMode : GameMode.SURVIVAL);

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

    /**
     * Détermine le monde "lobby" vers lequel renvoyer un joueur qui quitte KnockWars.
     * Utilise "lobby_world" dans la config si défini et chargé, sinon le premier
     * monde chargé du serveur (comportement historique).
     */
    private World resolveLobbyWorld() {
        String lobbyWorldName = getConfig().getString("lobby_world");
        if (lobbyWorldName != null && !lobbyWorldName.isEmpty()) {
            World configured = Bukkit.getWorld(lobbyWorldName);
            if (configured != null) {
                return configured;
            }
            getLogger().warning("⚠ lobby_world '" + lobbyWorldName + "' introuvable, utilisation du premier monde chargé.");
        }
        return Bukkit.getWorlds().get(0);
    }
}
