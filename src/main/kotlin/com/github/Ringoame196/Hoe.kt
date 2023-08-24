package com.github.Ringoame196

import com.github.Ringoame196.Entity.Zombie
import com.github.Ringoame196.data.Data
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class Hoe {
    fun system(player: Player, e: PlayerInteractEvent) {
        val shift = player.isSneaking
        val action = e.action
        val gui = Data.DataManager.playerDataMap[player.uniqueId]?.gui
        var select = Data.DataManager.playerDataMap[player.uniqueId]?.hoeselect ?: 0
        e.isCancelled = true
        if (shift) {
            gui?.let { player.openInventory(it) }
            return
        }
        if (action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) {
            select = (select + 1) % 9
            Data.DataManager.playerDataMap[player.uniqueId]?.hoeselect = select
            val zombieName = gui?.getItem(select)?.itemMeta?.displayName ?: "未設定"
            player.sendTitle("", "[$select]$zombieName")
        } else {
            if (player.location.subtract(0.0, 1.0, 0.0).block.type != Material.GLASS) {
                Player().errormessage("${ChatColor.RED}ガラスの上で実行してください(ガラスの上に行くには、自陣の後ろにあるボタンをクリック)", player)
                return
            }

            val zombie = gui?.getItem(select)
            if (zombie == null) {
                Player().errormessage("${ChatColor.RED}このスロットにはゾンビがいません", player)
                return
            }

            Zombie().summonSystem(player, zombie)
            durable(player)

            // アイテムの数を1減らす
            if (zombie.amount > 1) {
                zombie.amount -= 1
                gui.setItem(select, zombie)
            } else {
                gui.setItem(select, null)
            }
        }
    }
    fun durable(player: Player) {
        val itemInHand = player.inventory.itemInMainHand
        val damageableMeta = itemInHand.itemMeta as org.bukkit.inventory.meta.Damageable
        val currentDamage = damageableMeta.damage
        if (currentDamage < itemInHand.type.maxDurability) {
            damageableMeta.damage = currentDamage + 1
            itemInHand.itemMeta = damageableMeta

            // 耐久値が最大になったらアイテムを削除
            if (currentDamage + 1 >= itemInHand.type.maxDurability) {
                player.inventory.setItemInMainHand(null)
                player.playSound(player.location, Sound.ITEM_SHIELD_BREAK, 1f, 1f)
            }
        }
    }
    fun clickGUI(player: Player, inventory: Inventory) {
        for (i in 0..8) {
            val item = inventory.getItem(i)
            val itemName = item?.itemMeta?.displayName
            if (itemName?.contains("${ChatColor.YELLOW}[ゾンビ召喚]") == false) {
                player.inventory.addItem(item)
                inventory.setItem(i, ItemStack(Material.AIR))
            }
        }
    }
}
