package uk.co.scarfebread.wizardbeast.entity

import io.ktor.network.sockets.SocketAddress
import uk.co.scarfebread.wizardbeast.engine.state.publishable.PlayerState
import java.util.*

data class Player(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val input: Input = Input(),
    val inputQueue: MutableMap<Long, Input> = mutableMapOf(),
    var x: Float = randomPosition(800 - 25),
    var y: Float = randomPosition(480 - 25),
    val address: SocketAddress,
    var lastConfirmedState: Long = -1,
    var connected: Boolean = true
) {
    fun toState() = PlayerState(id, name, x, y)

    fun acknowledge(stateId: Long) {
        if (lastConfirmedState < stateId) {
            lastConfirmedState = stateId
        }
    }

    fun location() = Location(x, y)

    fun consumeInput() =  inputQueue.toMap().also { inputQueue.clear() }

    fun move(speed: Float) {
        if (input.down && !input.up) y -= speed
        if (input.up && !input.down) y += speed
        if (input.left && !input.right) x -= speed
        if (input.right && !input.left) x += speed
    }

    companion object {
        private fun randomPosition(max: Int) = (0..max).random().toFloat()
    }
}
