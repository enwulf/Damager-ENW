package ru.enwulf.damagerenw.display


import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.Display
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay
import org.bukkit.util.Transformation
import org.joml.Vector3f
import ru.enwulf.damagerenw.animations.AnimationManager.animate
import ru.enwulf.damagerenw.app
import ru.enwulf.damagerenw.config.Config


object DamageDisplay  {

    fun show(viewer: Player, location: Location, message: String, replacements: Map<String, String>? = null) {
        val textDisplay = create(
            location.clone().add(Config.DAMAGE_TEXT_LOCATION_OFFSET_X, Config.DAMAGE_TEXT_LOCATION_OFFSET_Y, Config.DAMAGE_TEXT_LOCATION_OFFSET_Z),
            message,
            replacements
        )

        configure(textDisplay)

        textDisplay.showTo(viewer)
        textDisplay.animate()
        textDisplay.scheduleDestroy(Config.TOTAL_DISPLAY_DURATION)
    }


    private fun create(location: Location, message: String, replacements: Map<String, String>?): TextDisplay {
        val textDisplay = location.world.spawnEntity(location, EntityType.TEXT_DISPLAY) as TextDisplay
        val formattedText = replacements?.let {
            Config.getFormattedText(message, it)
        } ?: Component.text(message)
        textDisplay.text(formattedText)
        return textDisplay
    }

    private fun configure(textDisplay: TextDisplay) {
        with(textDisplay) {
            isVisibleByDefault = false
            scale(if (Config.ANIMATIONS_INCREASE_ENABLED) 0.1 else Config.DISPLAY_SCALE)
            isSeeThrough = Config.DISPLAY_SHOW_THROUGH_WALLS
            textOpacity = Config.DISPLAY_OPACITY
            teleportDuration = 2
            backgroundColor = Color.fromARGB(0, 0, 0, 0)
            isShadowed = Config.DISPLAY_SHADOWED
            billboard = Display.Billboard.CENTER
        }
    }

    fun TextDisplay.scale(factor: Double) {
        transformation = Transformation(
            Vector3f(0f, 0f, 0f),
            transformation.leftRotation,
            Vector3f(factor.toFloat(), factor.toFloat(), factor.toFloat()),
            transformation.rightRotation
        )
    }

    private fun TextDisplay.showTo(player: Player) {
        player.showEntity(app, this)
    }

    private fun TextDisplay.scheduleDestroy(delay: Long) {
        Bukkit.getScheduler().runTaskLater(app, Runnable { remove() }, delay)
    }
}

