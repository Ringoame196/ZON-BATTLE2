package com.github.Ringoame196

import com.github.Ringoame196.data.GET
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class SelectGUI {
    fun system(player: Player, item: ItemStack) {
        val gui = Bukkit.createInventory(null, 27, "${ChatColor.DARK_GREEN}ショップ[BATTLEGUI]")
        when (item.itemMeta?.displayName) {
            "金床" -> Anvil().set(player)
            "買い物" -> Shop().gui(player)
            "チームチェスト" -> Team().chest(player, GET().teamName(player).toString())
            "強化" -> GUI().strengthen(player)
            "チーム強化" -> GUI().potionshop(gui, player)
            "村人強化" -> GUI().villagerlevelup(gui, player)
        }
    }
}
