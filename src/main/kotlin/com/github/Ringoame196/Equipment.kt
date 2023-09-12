package com.github.Ringoame196

import com.github.Ringoame196.Game.Scoreboard
import com.github.Ringoame196.data.GET
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta

class Equipment {
    fun initial(player: Player) {
        player.inventory.clear()
        hat(player)
        player.inventory.chestplate = unbreakable(ItemStack(Material.LEATHER_CHESTPLATE))
        player.inventory.leggings = unbreakable(ItemStack(Material.LEATHER_LEGGINGS))
        player.inventory.boots = unbreakable(ItemStack(Material.LEATHER_BOOTS))
        player.inventory.addItem(unbreakable(ItemStack(Material.WOODEN_SWORD)))
        if (Scoreboard().getValue("gameData", "map") == 3 && GET().teamName(player) == "blue") {
            player.inventory.addItem(unbreakable(ItemStack(Material.IRON_PICKAXE)))
        } else {
            val pickaxe = unbreakable(ItemStack(Material.WOODEN_PICKAXE))
            pickaxe.addEnchantment(Enchantment.DIG_SPEED, 1)
            player.inventory.addItem(pickaxe)
            player.inventory.addItem(Give().chatBook())
        }
    }
    fun hat(player: Player) {
        val hat = ItemStack(Material.LEATHER_HELMET)
        val meta = hat.itemMeta as LeatherArmorMeta
        when (GET().teamName(player)) {
            "red" -> meta.setColor(Color.RED)
            "blue" -> meta.setColor(Color.BLUE)
        }
        hat.setItemMeta(meta)
        hat.addEnchantment(Enchantment.BINDING_CURSE, 1)
        player.inventory.helmet = unbreakable(hat)
    }
    fun unbreakable(item: ItemStack): ItemStack {
        val meta = item.itemMeta
        meta?.isUnbreakable = true
        item.setItemMeta(meta)
        return item
    }
}
