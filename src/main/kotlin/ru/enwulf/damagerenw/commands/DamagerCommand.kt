package ru.enwulf.damagerenw.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.java.JavaPlugin
import ru.enwulf.damagerenw.app


class DamagerCommand(
    private val plugin: JavaPlugin
) : CommandExecutor, TabCompleter {

    init {
        plugin.getCommand("damager")?.setExecutor(this)
        plugin.getCommand("damager")?.tabCompleter = this
    }


    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage("§f— §eDamagerENW §7[Help]")
            sender.sendMessage("§f/$label reload - §6reload plugin.")
            return true
        }

        when (args[0].lowercase()) {
            "reload" -> {
                if (sender.hasPermission("damagerenw.reload")) {
                    app.configFile.reload()
                    sender.sendMessage("§aDamagerENW Reloaded")
                } else {
                    sender.sendMessage("§cYou dont have permission.")
                }
            }

            else -> sender.sendMessage("§c[DamagerENW] Unknown sub-command")
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String> {
        if (args.size == 1) {
            return listOf("reload").filter { it.startsWith(args[0], true) }
        }
        return emptyList()
    }
}