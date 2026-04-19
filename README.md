# 🎮 KnockWars - Plugin Spigot/Bukkit

Un plugin de mini-jeu PVP pour Bukkit/Spigot basé sur le mode **KnockBack** (KB), où les joueurs s'affrontent en utilisant le recul des coups pour éliminer leurs adversaires.

## 📋 Informations

- **Auteur** : Quentin_60
- **Version** : 1.0
- **Version Bukkit** : 1.8.8+
- **Repository** : https://github.com/quentin2160/knockwars

---

## 🎯 Caractéristiques

- ✅ Mode de jeu PVP basé sur le knockback
- ✅ Configuration complète du niveau de recul (KB) et du punch de l'arc
- ✅ Système de spawn personnalisable
- ✅ Gestion des chutes dans le vide
- ✅ Système de mort et respawn
- ✅ Item de sortie du jeu
- ✅ Messages entièrement en français et personnalisables
- ✅ Système de permissions

---

## 📥 Installation

### Compilation

1. Clonez le repository :
   ```bash
   git clone https://github.com/quentin2160/knockwars.git
   cd knockwars
   ```

2. Compilez le code avec votre IDE (Eclipse, IntelliJ, etc.) ou avec Maven/Gradle

3. Le fichier JAR généré se trouve dans le dossier `bin/` ou `target/`

4. Placez le JAR dans le dossier `plugins/` de votre serveur

5. Redémarrez le serveur ou utilisez `/reload`

6. Le plugin créera automatiquement le fichier `config.yml` au démarrage

---

## 🎮 Guide d'Utilisation

### Commandes

#### `/kb help`
Affiche l'aide générale du plugin avec la liste de toutes les commandes.

```
/kb help
```

---

#### `/kb join`
Permet à un joueur de rejoindre la partie KnockWars. Le joueur sera téléporté au spawn avec l'équipement approprié.

```
/kb join
```

**Équipement initial** :
- Slot 0 : Arc avec Infinité et Punch (si configuré)
- Slot 1 : Bâton KB avec Knockback (si configuré)
- Slot 8 : Porte "Quitter la partie"
- Slot 10 : Flèches (pour l'infinité)

---

#### `/kb leave`
Permet à un joueur de quitter la partie KnockWars.

```
/kb leave
```

**Alternative** : Cliquer sur la porte "Quitter la partie" dans l'inventaire (slot 8)

---

#### `/kb setspawn` — Permission: `knockwars.setspawn` (Op)
Définit la localisation du spawn KnockWars à la position du joueur.

```
/kb setspawn
```

---

#### `/kb setkb <kb|punch> <niveau>` — Permission: `knockwars.admin` (Op)
Configure les niveaux de recul du jeu.

```
/kb setkb kb 1        # Configure le KB du bâton à niveau 1
/kb setkb punch 2     # Configure le punch de l'arc à niveau 2
/kb setkb kb 0        # Désactive le recul du bâton
```

**Paramètres** :
- `<kb|punch>` : Type d'enchantement (`kb` pour le bâton ou `punch` pour l'arc)
- `<niveau>` : Niveau d'enchantement (0 = aucun recul, 1+ = Knockback I, II, etc.)

---

#### `/kb reload` — Permission: `knockwars.admin` (Op)
Recharge la configuration du plugin sans redémarrer le serveur.

```
/kb reload
```

---

## 📁 Fichier de Configuration

Le fichier `config.yml` est automatiquement créé au démarrage du plugin. Voici les options disponibles :

### Spawn

```yaml
spawn:
  world: kb           # Monde où se trouve le spawn KnockWars
  x: 8                # Coordonnée X
  y: 21               # Coordonnée Y
  z: 8                # Coordonnée Z
```

**Utilisation** : Utilisez `/kb setspawn` pour définir le spawn depuis le jeu (plus facile).

### Niveaux de Recul

```yaml
kb_level: 1           # Niveau de knockback du bâton (0 = aucun)
punch_level: 1        # Niveau de punch de l'arc (0 = aucun)
```

**Options** :
- `0` : Aucun recul
- `1` : Knockback I / Punch I
- `2` : Knockback II / Punch II
- `3+` : Niveaux supérieurs

**Modification** : Utilisez `/kb setkb <kb|punch> <niveau>` pour changer ces valeurs en jeu.

### Messages (Localisation)

```yaml
lang:
  # Préfixe des messages
  prefix: "§6[§eKnockWars§6] §r"
  
  # Messages d'erreur
  no_permission: "§cVous n'avez pas la permission."
  not_player: "§cCette commande est réservée aux joueurs."
  
  # Aide
  help_header: "§eListe des commandes KnockWars :"
  help_setspawn: "§6/kb setspawn §7- Définit la zone de spawn du jeu"
  help_setkb: "§6/kb setkb <kb|punch> <niveau> §7- Configure le niveau de recul"
  help_reload: "§6/kb reload §7- Recharge la configuration"
  help_join: "§6/kb join §7- Rejoindre la partie"
  help_leave: "§6/kb leave §7- Quitter la partie"
  help_help: "§6/kb help §7- Affiche cette aide"
  
  # Spawn
  spawn_set: "§aLe spawn KnockWars a été défini ici."
  
  # Configuration KB
  setkb_kb_set: "§aLe niveau de recul du bâton a été défini à {level}."
  setkb_punch_set: "§aLe niveau de punch de l'arc a été défini à {level}."
  setkb_invalid_type: "§cType invalide ! Utilisez 'kb' ou 'punch'."
  setkb_invalid_level: "§cNiveau invalide ! Veuillez entrer un nombre positif."
  setkb_usage: "§cUtilisation : /kb setkb <kb|punch> <niveau>"
  
  # Rechargement
  reload_success: "§aConfiguration rechargée avec succès !"
  
  # Rejoindre
  join_teleport: "§aVous avez rejoint KnockWars !"
  join_inventory_set: "§7Votre inventaire a été configuré."
  
  # Quitter
  leave_message: "§cVous avez quitté KnockWars."
  leave_item_name: "§cQuitter la partie"
  
  # Événements
  void_respawn: "§cVous êtes tombé !"
  player_death_by_player: "§cVous avez été tué par {killer} !"
  player_death_other: "§cVous êtes mort !"
```

**Codes couleur** : Voir la [documentation Minecraft des codes de formatage](https://minecraft.fandom.com/wiki/Formatting_codes)

---

## 🔐 Permissions

- **knockwars.setspawn** — Permet de définir le spawn KnockWars (Op)
- **knockwars.admin.setkb** — Permet de configurer les niveaux de recul (Op)
- **knockwars.admin** — Permission admin générale, inclut toutes les autres (Op)

---

## 📞 Support

Pour toute question, bug ou suggestion, veuillez ouvrir une **issue** sur GitHub :
https://github.com/quentin2160/knockwars/issues

---

## 📄 Licence

Ce plugin est fourni tel quel pour un usage personnel sur votre serveur Minecraft.

---

**Bon jeu ! 🎮**
