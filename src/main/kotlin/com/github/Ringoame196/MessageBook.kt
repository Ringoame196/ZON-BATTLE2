package com.github.Ringoame196

import com.github.Ringoame196.data.GET
import org.bukkit.entity.Player

class MessageBook {
    fun send(player: Player, message: String) {
        Team().sendMessage("<${player.displayName}> $message", GET().teamName(player).toString())
        player.closeInventory()
    }
}
