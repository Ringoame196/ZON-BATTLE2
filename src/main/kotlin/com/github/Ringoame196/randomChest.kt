package com.github.Ringoame196

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Chest
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.random.Random

class randomChest {
    fun setLocation(player: Player) {
        val location = player.location
        if (location.block.type != Material.CHEST) {
            player.sendMessage("${ChatColor.RED}チェストの上で実行してください")
            return
        }
        com.github.Ringoame196.data.Data.DataManager.LocationData.randomChest = player.location
    }
    @Suppress("NAME_SHADOWING")
    fun set() {
        val location = com.github.Ringoame196.data.Data.DataManager.LocationData.randomChest
        val items: MutableList<ItemStack> = setList()
        val random = Random
        val randomNumber = random.nextInt(0, items.size)
        val chest = location?.block
        if (chest?.type == Material.CHEST) {
            val chest = chest.state as Chest
            chest.inventory.addItem(items[randomNumber])
            PlayerSend().participantmessage("${ChatColor.YELLOW}チェストを補充しました")
            PlayerSend().participantplaysound(Sound.BLOCK_CHEST_OPEN)
        }
    }
    @Suppress("NAME_SHADOWING")
    fun reset() {
        val location = com.github.Ringoame196.data.Data.DataManager.LocationData.randomChest
        val chest = location?.block
        if (chest?.type == Material.CHEST) {
            val chest = chest.state as Chest
            chest.inventory.clear()
        }
    }
    fun setItem(Material: Material, name: String): ItemStack {
        val item = ItemStack(Material)
        val meta = item.itemMeta
        meta?.setDisplayName(name)
        item.setItemMeta(meta)
        return item
    }
    fun set_potionGUIitem(Material: Material, typePotion: PotionEffectType, level: Int, time: Int): ItemStack {
        // GUIにポーションを楽にセットする
        val itemStack = ItemStack(Material)
        val potionMeta = itemStack.itemMeta as PotionMeta

        val regenerationEffect = PotionEffect(typePotion, time * 20, level)
        potionMeta.addCustomEffect(regenerationEffect, true)
        potionMeta.setDisplayName("スプラッシュポーション")
        itemStack.setItemMeta(potionMeta)
        return itemStack
    }
    fun setList(): MutableList<ItemStack> {
        val item: MutableList<ItemStack> = mutableListOf()
        item.add(setItem(Material.NETHERITE_CHESTPLATE, ""))
        item.add(setItem(Material.NETHERITE_LEGGINGS, ""))
        item.add(setItem(Material.NETHERITE_BOOTS, ""))
        item.add(setItem(Material.NETHERITE_SWORD, ""))
        item.add(set_enchant_GUIitem(Material.NETHERITE_PICKAXE, Enchantment.DIG_SPEED, 3))
        item.add(setItem(Material.STONE_SWORD, ""))
        item.add(setItem(Material.STONE_PICKAXE, ""))
        item.add(setItem(Material.ENCHANTED_GOLDEN_APPLE, ""))
        item.add(setItem(Material.FISHING_ROD, ""))
        item.add(setItem(Material.SLIME_BALL, "${ChatColor.YELLOW}[召喚]ネザーライトゾンビ"))
        item.add(setItem(Material.NETHERITE_BLOCK, "ネザライトゴーレム"))
        item.add(set_potionGUIitem(Material.SPLASH_POTION, PotionEffectType.HEAL, 2, 1))
        return item
    }
    fun set_enchant_GUIitem(item: Material, enchant: Enchantment, level: Int): ItemStack {
        // GUIにエンチャント本を楽にセットする
        val item = ItemStack(item)
        val itemMeta: ItemMeta = item.itemMeta!!
        if (itemMeta is EnchantmentStorageMeta) {
            itemMeta.addStoredEnchant(enchant, level, true)
        }
        item.setItemMeta(itemMeta)
        return item
    }
}
