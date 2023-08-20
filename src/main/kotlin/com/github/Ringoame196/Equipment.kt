package com.github.Ringoame196

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta

class Equipment {
    fun Initial(player: Player) {
        player.inventory.clear()
        hat(player)
        player.inventory.chestplate = Unbreakable(ItemStack(Material.LEATHER_CHESTPLATE))
        player.inventory.leggings = Unbreakable(ItemStack(Material.LEATHER_LEGGINGS))
        player.inventory.boots = Unbreakable(ItemStack(Material.LEATHER_BOOTS))
        player.inventory.addItem(Unbreakable(ItemStack(Material.WOODEN_SWORD)))
        val pickaxe = Unbreakable(ItemStack(Material.WOODEN_PICKAXE))
        pickaxe.addEnchantment(Enchantment.DIG_SPEED, 1)
        player.inventory.addItem(pickaxe)
    }
    fun hat(player: Player) {
        val hat = ItemStack(Material.LEATHER_HELMET)
        val meta = hat.itemMeta as LeatherArmorMeta
        when (GET().TeamName(player)) {
            "red" -> meta.setColor(Color.RED)
            "blue" -> meta.setColor(Color.BLUE)
        }
        hat.setItemMeta(meta)
        hat.addEnchantment(Enchantment.BINDING_CURSE, 1)
        player.inventory.helmet = Unbreakable(hat)
    }
    fun Unbreakable(Item: ItemStack): ItemStack {
        val meta = Item.itemMeta
        meta?.isUnbreakable = true
        Item.setItemMeta(meta)
        return Item
    }
}
