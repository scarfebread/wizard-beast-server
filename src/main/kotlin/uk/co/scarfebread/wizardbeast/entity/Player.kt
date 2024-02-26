package uk.co.scarfebread.wizardbeast.entity

import io.ktor.network.sockets.SocketAddress
import uk.co.scarfebread.wizardbeast.engine.state.publishable.PlayerState
import java.util.*

data class Player(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    var x: Int = 0,
    var y: Int = 0,
    val address: SocketAddress,
    var lastConfirmedState: Long = -1,
    var connected: Boolean = true
) {
    fun toState() = PlayerState(name, x, y)

    fun acknowledge(stateId: Long) {
        if (lastConfirmedState < stateId) {
            lastConfirmedState = stateId
        }
    }

    fun location() = Location(x, y)
}
