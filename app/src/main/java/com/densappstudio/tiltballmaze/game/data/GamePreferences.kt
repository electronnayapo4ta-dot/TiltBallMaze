package com.densappstudio.tiltballmaze.game.data

import android.content.Context

object GamePreferences {

    private const val PREFS = "game_prefs"

    fun setLevelUnlocked(context: Context, level: Int) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("level_$level", true).apply()
    }

    fun isLevelUnlocked(context: Context, level: Int): Boolean {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getBoolean("level_$level", level == 1) // Level 1 unlocked by default
    }

    fun setLevelCompleted(context: Context, level: Int) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("level_completed_$level", true).apply()
    }

    fun isLevelCompleted(context: Context, level: Int): Boolean {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getBoolean("level_completed_$level", false)
    }

    fun saveBestTime(context: Context, level: Int, timeMs: Long) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val best = prefs.getLong("best_time_$level", Long.MAX_VALUE)
        if (timeMs < best) {
            prefs.edit().putLong("best_time_$level", timeMs).apply()
        }
    }

    fun getBestTime(context: Context, level: Int): Long {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getLong("best_time_$level", Long.MAX_VALUE)
    }

    fun setTrackProgression(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("track_progression", enabled).apply()
    }

    fun isTrackProgression(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getBoolean("track_progression", true)
    }

    fun setTrackTimer(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("track_timer", enabled).apply()
    }

    fun isTrackTimer(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getBoolean("track_timer", true)
    }
}

