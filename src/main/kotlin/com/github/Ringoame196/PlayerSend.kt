package com.github.Ringoame196

import com.github.Ringoame196.data.Data
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class PlayerSend {
    fun participantmessage(message: String) {
        for (loopPlayer in Data.DataManager.gameData.ParticipatingPlayer) {
            loopPlayer.sendMessage(message)
        }
    }

    fun errormessage(message: String, player: Player) {
        player.sendMessage("${ChatColor.RED}$message")
        player.closeInventory()
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
    }

    fun TeamGiveEffect(
        player: Player,
        itemName: String,
        effect1: PotionEffectType? = null,
        effect2: PotionEffectType? = null,
        level: Int,
        time: Int
    ) {
        val playerName = player.name
        val playerTeamName = GET().TeamName(player)
        var effectTeamName = GET().TeamName(player)
        if (itemName.contains("[妨害]")) {
            // 反対チーム名にする
            effectTeamName = GET().OpposingTeamname(playerTeamName!!)
        }
        for (loopPlayer in Data.DataManager.gameData.ParticipatingPlayer) {
            val loopPlayerTeam = GET().TeamName(loopPlayer)

            if (loopPlayerTeam == playerTeamName) {
                loopPlayer.sendMessage("${ChatColor.AQUA}[チーム]${playerName}さんが${itemName}${ChatColor.AQUA}を発動しました(レベル$level)")
                if (loopPlayerTeam == effectTeamName) {
                    effect1?.let { loopPlayer.addPotionEffect(PotionEffect(it, time * 20, level - 1)) }
                    effect2?.let {
                        loopPlayer.addPotionEffect(PotionEffect(it, time * 20, level - 1))
                        loopPlayer.playSound(loopPlayer.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
                    }
                }
            } else if (loopPlayerTeam == effectTeamName) {
                loopPlayer.sendMessage("${ChatColor.RED}[妨害]${playerTeamName}チームが${itemName}${ChatColor.RED}を発動しました(レベル $level)")
                effect1?.let { loopPlayer.addPotionEffect(PotionEffect(it, time * 20, level - 1)) }
                effect2?.let {
                    loopPlayer.addPotionEffect(PotionEffect(it, time * 20, level - 1))
                    loopPlayer.playSound(loopPlayer.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
                }
            }
        }
    }

    fun participantplaysound(sound: Sound) {
        for (player in Data.DataManager.gameData.ParticipatingPlayer) {
            player.playSound(player.location, sound, 1f, 1f)
        }
    }
}
