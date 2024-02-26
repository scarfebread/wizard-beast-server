package uk.co.scarfebread.wizardbeast.engine.game

import uk.co.scarfebread.wizardbeast.entity.Input
import uk.co.scarfebread.wizardbeast.entity.PlayerRepository
import uk.co.scarfebread.wizardbeast.server.request.PlayerActionRequest.Action
import java.lang.System.currentTimeMillis

class GameSimulator(private val playerRepository: PlayerRepository) {
    fun handleInput(playerId: String, actions: List<Action>) {
        playerRepository.getPlayer(playerId)?.let { player ->
            player.inputQueue[currentTimeMillis()] = player.input.copy().apply {
                actions.forEach {
                    when(it.key) {
                        DOWN -> down = it.currentlyPressed
                        UP -> up = it.currentlyPressed
                        LEFT -> left = it.currentlyPressed
                        RIGHT -> right = it.currentlyPressed
                    }
                }
            }
        }
    }

    /**
     * TODO
     * - the server is missing 10ms of movement compared with the client
     */
    fun processMovement(lastTick: Long, tickLength: Float) {
        playerRepository.getPlayers().forEach { player ->
            player.consumeInput().let { inputQueue ->
                if (inputQueue.isEmpty()) {
                    player.move(SPEED)
                } else {
                    val iterator = inputQueue.toSortedMap().iterator()
                    var alreadyMoved = 0

                    while(iterator.hasNext()) {
                        val iterable = iterator.next()
                        val timestamp = iterable.key
                        val input = iterable.value

                        (timestamp - lastTick - alreadyMoved).let { movementDuration ->
                            player.move(SPEED * movementDuration / tickLength)
                            alreadyMoved = (alreadyMoved + movementDuration).toInt()
                        }

                        player.input.up = input.up
                        player.input.down = input.down
                        player.input.left = input.left
                        player.input.right = input.right

                        if (!iterator.hasNext()) {
                            (tickLength - alreadyMoved).let { movementDuration ->
                                player.move(SPEED * (movementDuration / tickLength))
                                alreadyMoved = (alreadyMoved + movementDuration).toInt()
                            }
                        }

                        iterator.remove()
                    }

                    if (alreadyMoved > tickLength) {
                        println("WARN: Moved $alreadyMoved during a tick of $tickLength")
                    }
                }
            }
        }
    }

    companion object {
        private const val DOWN = 20
        private const val LEFT = 21
        private const val RIGHT = 22
        private const val UP = 19
        private const val SPEED = 1.67f
    }
}
