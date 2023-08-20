@file:Suppress("DEPRECATION")

package com.github.Ringoame196

import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.TeamData
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.NameTagVisibility

@Suppress("DEPRECATION")
class Team {
    fun chest(player: Player, team_name: String) {
        player.playSound(player, Sound.BLOCK_CHEST_OPEN, 1f, 1f)
        player.openInventory(Data.DataManager.teamDataMap.getOrPut(team_name) { TeamData() }.chest)
    }
    fun fastbreaklevel(team_name: String, player: Player, item_name: String) {
        val set_time = Data.DataManager.teamDataMap.getOrPut(team_name) { TeamData() }.blockTime - 1
        Data.DataManager.teamDataMap[team_name]?.blockTime = set_time
        GUI().villagerlevelup(player.openInventory.topInventory, player)
        PlayerSend().TeamGiveEffect(player, item_name, null, null, 6 - set_time, 0)
    }

    fun inAndout(player: Player) {
        val ParticipatingPlayer = Data.DataManager.gameData.ParticipatingPlayer
        if (GET().status()) {
            PlayerSend().errormessage("ゲームが終わるまでしばらくお待ち下さい", player)
            return
        }
        val message: String = if (ParticipatingPlayer.contains(player)) {
            ParticipatingPlayer.remove(player)
            "退出"
        } else {
            ParticipatingPlayer.add(player)
            "参加"
        }
        val size = "(参加人数:${ParticipatingPlayer.size}人)"
        PlayerSend().participantmessage("${ChatColor.AQUA}[$message] ${player.name}$size")
        player.sendTitle("", "${ChatColor.YELLOW}[${message}しました]")
        Sign().Numberdisplay("(参加中:${ParticipatingPlayer.size}人)")
    }
    fun make(name: String, color: ChatColor) {
        Bukkit.getScoreboardManager()?.mainScoreboard?.registerNewTeam(name)
        Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam(name)?.let {
            it.setAllowFriendlyFire(false)
            it.color = color
            it.nameTagVisibility = NameTagVisibility.HIDE_FOR_OTHER_TEAMS
        }
    }
    fun delete() {
        Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam("red")?.unregister()
        Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam("blue")?.unregister()
        Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam("kansen")?.unregister()
    }
    fun division() {
        val redTeam = Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam("red")
        val blueTeam = Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam("blue")
        var team = true
        var blueCount = 0
        for (loopPlayer in Data.DataManager.gameData.ParticipatingPlayer) {
            if (team) {
                redTeam?.addPlayer(loopPlayer)
                loopPlayer.teleport(Data.DataManager.LocationData.redspawn!!)
            } else {
                blueTeam?.addPlayer(loopPlayer)
                loopPlayer.teleport(Data.DataManager.LocationData.bluespawn!!)
                blueCount++
            }
            loopPlayer.scoreboardTags.remove("pvpjoin")
            loopPlayer.addPotionEffect(PotionEffect(PotionEffectType.SATURATION, Int.MAX_VALUE, 100, true, false))
            Equipment().Initial(loopPlayer)
            loopPlayer.gameMode = GameMode.SURVIVAL
            loopPlayer.health = 20.0
            if (loopPlayer.isOp) {
                loopPlayer.inventory.addItem(Give().GameSetting())
            }
            team = !team
        }
        if (!team) {
            Data.DataManager.gameData.shortage = true
            if (blueCount >= 2) {
                val blocktime = Data.DataManager.teamDataMap.getOrPut("blue") { TeamData() }.blockTime
                Data.DataManager.teamDataMap.getOrPut("blue") { TeamData() }.blockTime = blocktime - 1
            }
        }
    }
    fun respawn(player: Player, plugin: Plugin) {
        PlayerSend().participantmessage("${ChatColor.RED}[DEATH] ${player.name}")
        player.health = 20.0
        player.gameMode = GameMode.SPECTATOR
        var c = 6
        object : BukkitRunnable() {
            override fun run() {
                if (c> 0) {
                    c--
                    player.sendTitle("", "${ChatColor.GREEN}復活まで${c}秒")
                    player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f)
                } else {
                    player.gameMode = GameMode.SURVIVAL
                    respawnTP(player)
                    this.cancel()
                }
            }
        }.runTaskTimer(plugin, 0, 20)
    }
    fun respawnTP(player: Player) {
        when (GET().TeamName(player)) {
            "red" -> player.teleport(Data.DataManager.LocationData.redspawn!!)
            "blue" -> player.teleport(Data.DataManager.LocationData.bluespawn!!)
            else -> player.teleport(player.world.spawnLocation)
        }
    }
}
