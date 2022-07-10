package dansplugins.factionsystem.command

import dansplugins.factionsystem.MedievalFactions
import org.bukkit.ChatColor.RED
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.util.*

class MedievalFactionsCommand(private val plugin: MedievalFactions) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(plugin.language["MedievalFactionsTitle", plugin.description.version])
            sender.sendMessage(plugin.language["DeveloperList", plugin.description.authors.joinToString()])
            sender.sendMessage(plugin.language["WikiLink"])
            sender.sendMessage(plugin.language["CurrentLanguage", plugin.config.getString("language") ?: "en_US"])
            sender.sendMessage(plugin.language["SupportedLanguageList", Locale.getAvailableLocales().filter {
                try {
                    ResourceBundle.getBundle("lang", it)
                    true
                } catch(_: MissingResourceException) {
                    false
                }
            }.joinToString { it.toString() }])
            return true
        }
        return when (args[0].lowercase()) {
            else -> {
                sender.sendMessage("$RED${plugin.language["CommandMfUsage"]}")
                true
            }
        }
    }
}