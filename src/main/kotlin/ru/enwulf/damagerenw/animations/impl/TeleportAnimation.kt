package ru.enwulf.damagerenw.animations.impl

import org.bukkit.Bukkit
import org.bukkit.entity.TextDisplay
import org.bukkit.scheduler.BukkitRunnable
import ru.enwulf.damagerenw.animations.Animation
import ru.enwulf.damagerenw.app
import ru.enwulf.damagerenw.config.Config

class TeleportAnimation(
    private val textDisplay: TextDisplay,
    private val yOffset: Double,
    private val useMaxHeight: Boolean
) : Animation {
    override fun play(delay: Long) {
        val maxHeight = Config.ANIMATIONS_UPWARD_MAX_HEIGHT

        object : BukkitRunnable() {
            var height = 0
            override fun run() {
                textDisplay.teleport(textDisplay.location.add(0.0, yOffset, 0.0))
                if (useMaxHeight && ++height == maxHeight) {
                    cancel()
                }

                Bukkit.getScheduler().runTaskLater(app, Runnable {
                    cancel()
                }, Config.TOTAL_DISPLAY_DURATION)

            }
        }.runTaskTimer(app, delay, 0)
    }
}