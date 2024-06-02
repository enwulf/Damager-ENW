package ru.enwulf.damagerenw.animations.impl

import org.bukkit.Bukkit
import org.bukkit.entity.TextDisplay
import ru.enwulf.damagerenw.animations.Animation
import ru.enwulf.damagerenw.app
import ru.enwulf.damagerenw.config.Config
import ru.enwulf.damagerenw.display.DamageDisplay.scale


class ScaleAnimation(
    private val textDisplay: TextDisplay,
    private val animationType: AnimationType,
) : Animation {
    override fun play(delay: Long) {
        val (scale, taskDelay) = when (animationType) {
            AnimationType.INCREASE -> Config.DISPLAY_SCALE to 3L
            AnimationType.DECREASE -> 0.1f to (Config.TOTAL_DISPLAY_DURATION - Config.ANIMATIONS_DECREASE_DELAY)
        }

        Bukkit.getScheduler().runTaskLater(app, Runnable {
            textDisplay.apply {
                interpolationDelay = -1
                interpolationDuration = delay.toInt()
                scale(scale.toDouble())
            }
        }, taskDelay)
    }


    enum class AnimationType {
        INCREASE,
        DECREASE
    }
}