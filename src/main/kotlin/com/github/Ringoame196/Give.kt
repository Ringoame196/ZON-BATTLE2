package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.SkullMeta

class Give {
    fun gameSetting(): ItemStack {
        val gameSetting = ItemStack(Material.COMMAND_BLOCK)
        val meta = gameSetting.itemMeta
        meta?.setDisplayName("ゲーム設定")
        gameSetting.setItemMeta(meta)
        return gameSetting
    }
    fun equipment(player: Player, item: ItemStack) {
        val type = item.type.toString()
        if (type.contains("HELMET")) {
            player.inventory.helmet = item
        } else if (type.contains("CHESTPLATE")) {
            player.inventory.chestplate = item
        } else if (type.contains("CHESTPLATE")) {
            player.inventory.chestplate = item
        } else if (type.contains("LEGGINGS")) {
            player.inventory.leggings = item
        } else if (type.contains("BOOTS")) {
            player.inventory.boots = item
        }
    }
    fun sword(player: Player) {
        player.inventory.remove(Material.WOODEN_SWORD)
        player.inventory.remove(Material.STONE_SWORD)
        player.inventory.remove(Material.IRON_SWORD)
        player.inventory.remove(Material.DIAMOND_SWORD)
    }
    fun pickaxe(player: Player) {
        player.inventory.remove(Material.WOODEN_PICKAXE)
        player.inventory.remove(Material.STONE_PICKAXE)
        player.inventory.remove(Material.IRON_PICKAXE)
        player.inventory.remove(Material.DIAMOND_PICKAXE)
    }
    fun coin(): ItemStack {
        val coin = ItemStack(Material.GOLD_INGOT)
        val meta = coin.itemMeta
        meta?.setDisplayName("${org.bukkit.ChatColor.YELLOW}コイン")
        coin.setItemMeta(meta)
        return coin
    }
    fun axe(player: Player) {
        player.inventory.remove(Material.WOODEN_AXE)
        player.inventory.remove(Material.STONE_AXE)
        player.inventory.remove(Material.IRON_AXE)
        player.inventory.remove(Material.DIAMOND_AXE)
    }
    fun colorLEATHER(material: Material, color: String): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta as LeatherArmorMeta
        when (color) {
            "GREEN" -> meta.setColor(Color.GREEN)
            "AQUA" -> meta.setColor(Color.AQUA)
            "BLACK" -> meta.setColor(Color.BLACK)
        }
        item.setItemMeta(meta)
        return item
    }
    fun chatBook(): ItemStack {
        val chatBook = ItemStack(Material.BOOK)
        val meta = chatBook.itemMeta
        meta?.setDisplayName("${ChatColor.YELLOW}チャット")
        chatBook.setItemMeta(meta)
        return chatBook
    }
    fun point(price: Int): ItemStack {
        val item = ItemStack(Material.EMERALD)
        val meta = item.itemMeta
        meta?.setDisplayName("${ChatColor.GREEN}${price}p")
        item.setItemMeta(meta)
        return item
    }
    fun removelore(item: ItemStack): ItemStack {
        val meta = item.itemMeta
        meta?.lore = null
        item.setItemMeta(meta)
        return item
    }
    fun playerHead(name: String): ItemStack {
        val item = ItemStack(Material.PLAYER_HEAD)
        val itemMeta = item.itemMeta as SkullMeta
        itemMeta.owningPlayer = Bukkit.getOfflinePlayer(name) // プレイヤー名で設定
        itemMeta.isUnbreakable = true // 不破壊
        item.setItemMeta(itemMeta)
        return item
    }
    fun zombieAmulet(): ItemStack {
        val item = ItemStack(Material.MOSSY_COBBLESTONE_WALL)
        val meta = item.itemMeta
        meta?.setDisplayName("${ChatColor.GREEN}ゾンビのお守り")
        item.setItemMeta(meta)
        return item
    }
}
