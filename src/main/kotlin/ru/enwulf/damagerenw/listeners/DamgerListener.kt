package ru.enwulf.damagerenw.listeners

import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier
import ru.enwulf.damagerenw.config.Config
import ru.enwulf.damagerenw.display.DamageDisplay
import ru.enwulf.damagerenw.trackers.KillTracker
import java.util.*


class DamageListener : Listener {

    private val killTracker = KillTracker()

    @EventHandler(ignoreCancelled = true)
    fun EntityDamageByEntityEvent.handle() {
        val damagedEntity = entity as? LivingEntity ?: return

        if (Config.SHOW_ONLY_PLAYER_DAMAGE && damagedEntity !is Player) return
        if (Config.IGNORE_ZERO_DAMAGE && finalDamage <= 0) return

        val damager = damager
        if (damager is Projectile && Config.PROJECTILE_DAMAGE_IGNORE.contains(damager.type.name)) return
        val displayLocation = getImpactLocation(damager.location, damagedEntity)

        val calculatedDamage = calculateDamage(this)

        when (damager) {
            is Projectile -> (damager.shooter as? Player)?.let {
                handlePlayerDamage(this, it, damagedEntity, displayLocation, calculatedDamage)
            }

            is Player -> handlePlayerDamage(this, damager, damagedEntity, displayLocation, calculatedDamage)
        }
    }



    private fun calculateDamage(event: EntityDamageByEntityEvent): String {
        var dmg = 0.0
        for (modifier in DamageModifier.entries) {
            if (modifier != DamageModifier.ABSORPTION) {
                dmg += event.getDamage(modifier)
            }
        }
        return String.format(Locale.US, "%.1f", dmg)
    }



    private fun getImpactLocation(damagerLocation: Location, damagedEntity: LivingEntity): Location {
        val damagedLocation = damagedEntity.location

        val directionVector = damagedLocation.toVector()
            .subtract(damagerLocation.toVector())
            .normalize()

        val offset = directionVector.multiply(-0.6)

        val impactLocation = damagedLocation.add(offset)
        return when (Config.DAMAGE_TEXT_LOCATION) {
            "head" -> impactLocation.add(0.0, damagedEntity.eyeHeight + 0.5, 0.0)
            "eye" -> impactLocation.add(0.0, damagedEntity.eyeHeight - 0.2, 0.0)
            "body" -> impactLocation.add(0.0, damagedEntity.height / 2, 0.0)
            else -> impactLocation.add(0.0, damagedEntity.eyeHeight - 0.2, 0.0) // По дефолту eye
        }
    }


    private fun handlePlayerDamage(
        event: EntityDamageByEntityEvent,
        player: Player,
        damagedEntity: LivingEntity,
        displayLocation: Location,
        damage: String
    ) {

        if (!player.hasPermission("damagerenw.see")) return

        if (Config.KILL_TEXT_ENABLED && damagedEntity.health - event.finalDamage <= 0) {
            val killMessage = killTracker.getKillMessage(player.uniqueId)
            DamageDisplay.show(player, displayLocation, killMessage, mapOf("victim" to damagedEntity.name))

            if (!Config.SHOW_DAMAGE_ON_KILL)
                return
            else displayLocation.add(0.0, 0.15, 0.0)
        }

        DamageDisplay.show(
            player,
            displayLocation,
            Config.DAMAGE_TEXT ?: "",
            mapOf("damage" to damage, "health" to damagedEntity.health.toInt().toString())
        )
    }
}