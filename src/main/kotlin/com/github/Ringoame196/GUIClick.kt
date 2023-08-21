package com.github.Ringoame196

import com.github.Ringoame196.Entity.Wolf
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
    fun system(plugin: Plugin, e: InventoryClickEvent, player: Player, GUI_name: String, item: ItemStack) {
        if (GUI_name == "${ChatColor.DARK_GREEN}金床") {
            anvil().click(e)
            return
        }
        if (!GUI_name.contains("[BATTLEGUI]")) { return }
        e.isCancelled = true
        player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f)
        when (GUI_name.replace("[BATTLEGUI]", "")) {
            "${ChatColor.BLUE}攻防戦ショップ" -> homeshop(player, item)
            "${ChatColor.DARK_GREEN}ショップ" -> shop().system(item, player)
            "${ChatColor.DARK_GREEN}設定画面" -> GameSystem().system(plugin, player, item, e)
            "${ChatColor.DARK_GREEN}参加プレイヤー" -> item.itemMeta?.let { GameSystem().playersJoin(it.displayName, player) }
            "${ChatColor.DARK_GREEN}選択画面" -> SelectGUI().system(player, item)
        }
    }

    fun homeshop(player: Player, item: ItemStack) {
        val team_name = GET().TeamName(player) ?: return
        val item_name = item.itemMeta?.displayName ?: return
        val shop: Inventory = Bukkit.createInventory(null, 36, "${ChatColor.DARK_GREEN}ショップ[BATTLEGUI]")
        when (item_name) {
            "${ChatColor.YELLOW}共通チェスト" -> Team().chest(player, team_name)
            "${ChatColor.YELLOW}ツール" -> GUI().pickaxeshop(shop, player)
            "${ChatColor.YELLOW}武器" -> GUI().weaponshop(shop, player)
            "${ChatColor.YELLOW}防具" -> GUI().equipmentshop(shop)
            "${ChatColor.YELLOW}金床" -> anvil().set(player)
            "${ChatColor.YELLOW}村人強化" -> GUI().villagerlevelup(shop, player)
            "${ChatColor.YELLOW}その他" -> GUI().general_merchandiseshop(shop, player)
            "${ChatColor.YELLOW}お邪魔アイテム" -> GUI().disturbshop(shop)
            "${ChatColor.YELLOW}ゾンビ" -> {
                GUI().zombieshop(player)
                return
            }
            "${ChatColor.YELLOW}ペット" -> GUI().petshop(shop)
            else -> return
        }
        shop.getItem(0) ?: return
        player.openInventory(shop)
    }

    fun click_invocation(player: Player, item_name: String, team_name: String) {
        val check_name = item_name
            .replace("${ChatColor.YELLOW}★", "")
            .replace("チーム全員に", "")
        when (check_name) {
            "ショップ解放" -> shop().release(player, team_name, item_name)
            "攻撃力UP(3分)" -> PlayerSend().TeamGiveEffect(player, item_name, PotionEffectType.INCREASE_DAMAGE, null, 0, 180)
            "再生UP(3分)" -> PlayerSend().TeamGiveEffect(player, item_name, PotionEffectType.REGENERATION, null, 2, 180)
            "採掘速度UP(5分)" -> PlayerSend().TeamGiveEffect(player, item_name, PotionEffectType.FAST_DIGGING, null, 2, 300)
            "耐性(3分)" -> PlayerSend().TeamGiveEffect(player, item_name, PotionEffectType.DAMAGE_RESISTANCE, null, 1, 180)
            "移動速度UP(3分)" -> PlayerSend().TeamGiveEffect(player, item_name, PotionEffectType.SPEED, null, 1, 180)
            "攻撃力UP&再生(3分)" -> PlayerSend().TeamGiveEffect(player, item_name, PotionEffectType.REGENERATION, PotionEffectType.INCREASE_DAMAGE, 0, 180)
            "鉱石復活速度UP" -> Team().fastbreaklevel(team_name, player, item_name)
            "村人体力増加" -> shop().TeamMaxHPadd(team_name, player, item_name, 10)
            "盲目(10秒)[妨害]" -> PlayerSend().TeamGiveEffect(player, item_name, PotionEffectType.BLINDNESS, null, 255, 10)
            "弱体化(10秒)[妨害]" -> PlayerSend().TeamGiveEffect(player, item_name, PotionEffectType.WEAKNESS, null, 255, 10)
            "採掘速度低下(10秒)[妨害]" -> PlayerSend().TeamGiveEffect(player, item_name, PotionEffectType.SLOW_DIGGING, null, 255, 10)
            "狼召喚" -> Wolf().summon(player)
            "村人耐久1(3分)" -> shop().effect(player, item_name, PotionEffectType.DAMAGE_RESISTANCE, 180, 1)
            "村人再生1(3分)" -> shop().effect(player, item_name, PotionEffectType.REGENERATION, 180, 1)
        }
    }
}
