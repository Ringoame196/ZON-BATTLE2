package com.github.Ringoame196.Game

import org.bukkit.Bukkit

class Scoreboard {
    val scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard
    fun make(id: String, name: String) {
        scoreboard?.registerNewObjective(id, "dummy", name)
    }
    fun set(score: String, name: String, value: Int) {
        val objective = scoreboard?.getObjective(score)
        val score = objective?.getScore(name)
        score?.score = value
    }
    fun delete(score: String) {
        val objective = scoreboard?.getObjective(score)
        objective?.unregister()
    }
}
