package knockwars.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        // getItemMeta() peut retourner null pour certains matériaux (ex: AIR) ;
        // les méthodes ci-dessous vérifient donc systématiquement meta avant de l'utiliser.
        this.meta = item.getItemMeta();
    }

    public ItemBuilder setName(String name) {
        if (meta != null) meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        if (meta != null) meta.setLore(lore);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment ench, int level, boolean ignoreLevelRestriction) {
        if (meta != null) meta.addEnchant(ench, level, ignoreLevelRestriction);
        return this;
    }

    public ItemBuilder addFlag(ItemFlag flag) {
        if (meta != null) meta.addItemFlags(flag);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}