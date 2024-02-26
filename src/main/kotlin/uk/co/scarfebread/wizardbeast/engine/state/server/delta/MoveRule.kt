package uk.co.scarfebread.wizardbeast.engine.state.server.delta

import uk.co.scarfebread.wizardbeast.engine.state.publishable.action.MoveAction
import uk.co.scarfebread.wizardbeast.engine.state.publishable.action.NoAction
import uk.co.scarfebread.wizardbeast.engine.state.publishable.action.PlayerAction
import uk.co.scarfebread.wizardbeast.entity.Location
import uk.co.scarfebread.wizardbeast.entity.Player

class MoveRule : PublishRule {
    override fun apply(
        playerStateChange: PlayerStateChange,
        otherPlayerStateChange: PlayerStateChange
    ): PlayerAction {
        val playerLocation = playerStateChange.current.location()
        val previousPlayerLocation = playerStateChange.previous.location()
        val previousOtherPlayerLocation = otherPlayerStateChange.previous.location()
        val otherPlayerLocation = otherPlayerStateChange.current.location()

        return if (
            otherPlayerStateChange.isComplete() &&
            (previousOtherPlayerLocation.x != otherPlayerLocation.x ||
            previousOtherPlayerLocation.y != otherPlayerLocation.y)
        ) {
            if (
                playerLocation.isCloseTo(otherPlayerLocation) ||
                previousPlayerLocation.isCloseTo(previousOtherPlayerLocation)
            ) {
                otherPlayerStateChange.current!!.let {
                    MoveAction(
                        player = it.id,
                        x = it.x,
                        y = it.y,
                        input = it.input
                    )
                }
            } else {
                NoAction
            }
        } else {
            NoAction
        }
    }

    private fun Player?.location() = this?.location() ?: Location(null, null)
}
