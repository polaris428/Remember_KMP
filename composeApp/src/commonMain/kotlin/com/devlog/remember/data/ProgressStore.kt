// composeApp/src/commonMain/kotlin/com/devlog/remember/data/ProgressStore.kt
package com.devlog.remember.data

import com.russhwolf.settings.Settings
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object ProgressStore {
    private val s = Settings()


    private const val KEY_INDEX = "progress.index"
    private const val KEY_TOTAL = "progress.total"


    private const val KEY_TODAY_DATE = "progress.today.date"
    private const val KEY_TODAY_CNT  = "progress.today.count"


    private const val KEY_DAILY_GOAL = "progress.daily.goal"

    private fun todayStr(): String =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()


    fun ensureToday() {
        val t = todayStr()
        if (s.getStringOrNull(KEY_TODAY_DATE) != t) {
            s.putString(KEY_TODAY_DATE, t)
            s.putInt(KEY_TODAY_CNT, 0)
        }
    }


    fun loadIndex(): Int = s.getInt(KEY_INDEX, 0)
    fun saveIndex(v: Int) = s.putInt(KEY_INDEX, v)


    fun loadTotal(): Int = s.getInt(KEY_TOTAL, 0)
    fun saveTotal(v: Int) = s.putInt(KEY_TOTAL, v)


    fun loadTodayCount(): Int { ensureToday(); return s.getInt(KEY_TODAY_CNT, 0) }
    private fun saveTodayCount(v: Int) { ensureToday(); s.putInt(KEY_TODAY_CNT, v) }


    fun loadDailyGoal(): Int = s.getInt(KEY_DAILY_GOAL, 10)
    fun saveDailyGoal(v: Int) = s.putInt(KEY_DAILY_GOAL, v.coerceAtLeast(1))


    fun completeSession(countLearned: Int, newIndex: Int) {
        ensureToday()
        saveIndex(newIndex)
        saveTotal(loadTotal() + countLearned)
        saveTodayCount(loadTodayCount() + countLearned)
    }
}
