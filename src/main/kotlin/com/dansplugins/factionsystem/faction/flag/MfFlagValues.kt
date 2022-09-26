package com.dansplugins.factionsystem.faction.flag

import com.dansplugins.factionsystem.MedievalFactions
import org.bukkit.Bukkit
import org.bukkit.configuration.serialization.ConfigurationSerializable

class MfFlagValues(private val values: Map<MfFlag<out Any>, Any?> = mutableMapOf()) : ConfigurationSerializable {
    operator fun <T: Any> get(flag: MfFlag<T>): T = values[flag] as? T ?: flag.defaultValue
    operator fun <T: Any> plus(flagValue: Pair<MfFlag<T>, T>): MfFlagValues = MfFlagValues(values + flagValue)
    override fun serialize(): Map<String, Any?> {
        return values.mapKeys { (flag, _) -> flag.name }
    }
    companion object {
        @JvmStatic
        fun deserialize(serialized: Map<String, Any?>): MfFlagValues {
            val plugin = Bukkit.getPluginManager().getPlugin("MedievalFactions") as MedievalFactions
            return MfFlagValues(serialized.mapNotNull { (flagName: String, flagValue: Any?) ->
                val flag: MfFlag<out Any>? = plugin.flags[flagName]
                if (flag == null) {
                    plugin.logger.warning("Missing flag definition: $flagName")
                }
                flagValue?.let { value -> flag?.let { flag to value } }
            }.toMap())
        }
    }
}