package com.github.Ringoame196

import com.github.Ringoame196.Entity.Wolf
import com.github.Ringoame196.Game.GameSystem
import com.github.Ringoame196.data.GET
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffectType

class GUIClick {
    fun system(plugin: Plugin, e: InventoryClickEvent, player: Player, guiName: String, item: ItemStack) {
        if (guiName == "${ChatColor.DARK_GREEN}金床") {
            Anvil().click(e)
            return
        }
        if (!guiName.contains("[BATTLEGUI]")) { return }
        e.isCancelled = true
        player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f)
        when (guiName.replace("[BATTLEGUI]", "")) {
            "${ChatColor.BLUE}攻防戦ショップ" -> homeShop(player, item)
            "${ChatColor.DARK_GREEN}ショップ" -> Shop().system(item, player)
            "${ChatColor.DARK_GREEN}設定画面" -> GameSystem().system(plugin, player, item, e)
            "${ChatColor.DARK_GREEN}参加プレイヤー" -> item.itemMeta?.let { GameSystem().playersJoin(it.displayName, player) }
            "${ChatColor.DARK_GREEN}選択画面" -> GUI().selectGUI(player, item)
        }
    }

    fun homeShop(player: Player, item: ItemStack) {
        val teamName = GET().teamName(player) ?: return
        val itemName = item.itemMeta?.displayName ?: return
        val shop: Inventory = Bukkit.createInventory(null, 36, "${ChatColor.DARK_GREEN}ショップ[BATTLEGUI]")
        when (itemName) {
            "${ChatColor.YELLOW}共通チェスト" -> Team().chest(player, teamName)
            "${ChatColor.YELLOW}ツール" -> GUI().pickaxeshop(shop, player)
            "${ChatColor.YELLOW}武器" -> GUI().weaponshop(shop, player)
            "${ChatColor.YELLOW}防具" -> GUI().equipmentshop(shop)
            "${ChatColor.YELLOW}金床" -> Anvil().set(player)
            "${ChatColor.YELLOW}村人強化" -> GUI().villagerlevelup(shop, player)
            "${ChatColor.YELLOW}その他" -> GUI().generalMerchandiseshop(shop, player)
            "${ChatColor.YELLOW}お邪魔アイテム" -> GUI().disturbshop(shop)
            "${ChatColor.YELLOW}ゾンビ" -> {
                GUI().zombieshop(player)
                return
            }
            "${ChatColor.YELLOW}ペット" -> GUI().petShop(shop)
            else -> return
        }
        shop.getItem(0) ?: return
        player.openInventory(shop)
    }

    fun clickInvocation(player: Player, itemName: String, teamName: String) {
        val checkName = itemName
            .replace("${ChatColor.YELLOW}★", "")
            .replace("チーム全員に", "")
        when (checkName) {
            "ショップ解放" -> Shop().release(player, teamName, itemName)
            "攻撃力UP(3分)" -> Team().GiveEffect(player, itemName, PotionEffectType.INCREASE_DAMAGE, null, 0, 180)
            "再生UP(3分)" -> Team().GiveEffect(player, itemName, PotionEffectType.REGENERATION, null, 2, 180)
            "採掘速度UP(5分)" -> Team().GiveEffect(player, itemName, PotionEffectType.FAST_DIGGING, null, 2, 300)
            "耐性(3分)" -> Team().GiveEffect(player, itemName, PotionEffectType.DAMAGE_RESISTANCE, null, 1, 180)
            "移動速度UP(3分)" -> Team().GiveEffect(player, itemName, PotionEffectType.SPEED, null, 1, 180)
            "攻撃力UP&再生(3分)" -> Team().GiveEffect(player, itemName, PotionEffectType.REGENERATION, PotionEffectType.INCREASE_DAMAGE, 0, 180)
            "鉱石復活速度UP" -> Team().fastbreaklevel(teamName, player, itemName)
            "村人体力増加" -> Shop().teamMaxHPadd(teamName, player, itemName, 10)
            "盲目(10秒)[妨害]" -> Team().GiveEffect(player, itemName, PotionEffectType.BLINDNESS, null, 255, 10)
            "弱体化(10秒)[妨害]" -> Team().GiveEffect(player, itemName, PotionEffectType.WEAKNESS, null, 255, 10)
            "採掘速度低下(10秒)[妨害]" -> Team().GiveEffect(player, itemName, PotionEffectType.SLOW_DIGGING, null, 255, 10)
            "狼召喚" -> Wolf().summon(player)
            "村人耐久1(3分)" -> Shop().effect(player, itemName, PotionEffectType.DAMAGE_RESISTANCE, 180, 1)
            "村人再生1(3分)" -> Shop().effect(player, itemName, PotionEffectType.REGENERATION, 180, 1)
        }
    }
}
