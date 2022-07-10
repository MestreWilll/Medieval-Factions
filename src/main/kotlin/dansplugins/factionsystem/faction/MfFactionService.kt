package dansplugins.factionsystem.faction

import dansplugins.factionsystem.failure.OptimisticLockingFailureException
import dansplugins.factionsystem.failure.ServiceFailure
import dansplugins.factionsystem.failure.ServiceFailureType
import dansplugins.factionsystem.failure.ServiceFailureType.CONFLICT
import dansplugins.factionsystem.failure.ServiceFailureType.GENERAL
import dansplugins.factionsystem.player.MfPlayerId
import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.mapFailure
import dev.forkhandles.result4k.resultFrom

class MfFactionService(private val repository: MfFactionRepository) {

    fun getFaction(name: String): MfFaction? = repository.getFaction(name)
    fun getFaction(playerId: MfPlayerId): MfFaction? = repository.getFaction(playerId)
    fun save(faction: MfFaction): Result4k<MfFaction, ServiceFailure> = resultFrom {
        repository.upsert(faction)
    }.mapFailure { exception ->
        ServiceFailure(exception.toServiceFailureType(), "Service error: ${exception.message}", exception)
    }

    private fun Exception.toServiceFailureType(): ServiceFailureType {
        return when (this) {
            is OptimisticLockingFailureException -> CONFLICT
            else -> GENERAL
        }
    }

}