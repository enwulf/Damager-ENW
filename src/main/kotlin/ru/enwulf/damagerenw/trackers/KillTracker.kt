package ru.enwulf.damagerenw.trackers

import ru.enwulf.damagerenw.config.Config
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class KillTracker {

    private val killCounts = ConcurrentHashMap<UUID, KillData>()

    fun getKillMessage(playerUUID: UUID): String {
        val killData = killCounts.compute(playerUUID) { _, data ->
            val now = System.currentTimeMillis()
            if (data == null || now - data.lastKillTime > Config.KILL_TEXT_SEQUENTIAL_RESET) {
                KillData(0, now)
            } else {
                data.increment(now)
            }
        }!!

        return when (Config.KILL_TEXT_TYPE) {
            "sequential" -> {
                val index = killData.count
                if (index >= Config.KILL_TEXT_MESSAGES.size) {
                    Config.KILL_TEXT_MESSAGES.last()
                } else {
                    Config.KILL_TEXT_MESSAGES[index]
                }
            }
            else -> Config.KILL_TEXT_MESSAGES.random()
        }
    }


    private data class KillData(var count: Int, var lastKillTime: Long) {
        fun increment(now: Long): KillData {
            count++
            lastKillTime = now
            return this
        }
    }
}