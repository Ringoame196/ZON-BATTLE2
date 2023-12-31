package com.github.Ringoame196.Game

import org.bukkit.Bukkit
import org.bukkit.ChatColor

class Scoreboard {
    val scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard
    fun make(id: String, name: String) {
        if (scoreboard?.getObjective(id) != null) {
            delete(id)
        }
        scoreboard?.registerNewObjective(id, "dummy", name)
    }
    fun set(scoreName: String, name: String, value: Int) {
        val objective = scoreboard?.getObjective(scoreName) ?: return
        val score = objective.getScore(name)
        score.score = value
    }
    fun add(scoreName: String, name: String, add: Int) {
        val value = getValue(scoreName, name) + add
        set(scoreName, name, value)
    }
    fun remove(scoreName: String, name: String, remove: Int) {
        val value = getValue(scoreName, name) - remove
        set(scoreName, name, value)
    }
    fun getSize(scoreName: String): Int {
        val objective = scoreboard?.getObjective(scoreName) ?: return 0
        var scoreCount = 0
        for (entry in scoreboard.entries) {
            val score = objective.getScore(entry)
            if (Scoreboard().getValue(scoreName, entry) == 0) { continue }
            if (score.isScoreSet) {
                scoreCount++
            }
        }
        return scoreCount
    }
    fun getValue(score: String, name: String): Int {
        val objective = scoreboard?.getObjective(score) ?: return 0
        val scoreObject = objective.getScore(name)
        return scoreObject.score
    }
    fun delete(score: String) {
        val objective = scoreboard?.getObjective(score) ?: return
        objective.unregister()
    }
    fun setTeamScore() {
        Scoreboard().make("RedTeam", "${ChatColor.RED}赤チーム")
        Scoreboard().make("BlueTeam", "${ChatColor.BLUE}青チーム")
        Scoreboard().set("RedTeam", "赤チーム(自陣)", 100)
        Scoreboard().set("RedTeam", "青チーム", 100)
        Scoreboard().set("BlueTeam", "赤チーム", 100)
        Scoreboard().set("BlueTeam", "青チーム(自陣)", 100)
        Scoreboard().set("RedTeam", "${ChatColor.AQUA}----------", 0)
        Scoreboard().set("BlueTeam", "${ChatColor.AQUA}----------", 0)
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard objectives setdisplay sidebar.team.red RedTeam")
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard objectives setdisplay sidebar.team.blue BlueTeam")
        Scoreboard().make("RedTeamSystem", "RedSystem")
        Scoreboard().make("BlueTeamSystem", "BlueSystem")
        commonToTeams("revivalTime", 5)
        commonToTeams("magnification", 1)
        commonToTeams("fountainpoint", 10)
    }
    fun commonToTeams(name: String, value: Int) {
        Scoreboard().set("RedTeamSystem", name, value)
        Scoreboard().set("BlueTeamSystem", name, value)
    }
}
