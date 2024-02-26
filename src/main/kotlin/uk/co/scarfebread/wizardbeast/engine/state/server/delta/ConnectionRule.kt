package uk.co.scarfebread.wizardbeast.engine.state.server.delta

import uk.co.scarfebread.wizardbeast.engine.state.publishable.action.ConnectAction
import uk.co.scarfebread.wizardbeast.engine.state.publishable.action.NoAction
import uk.co.scarfebread.wizardbeast.engine.state.publishable.action.PlayerAction

class ConnectionRule : PublishRule {
    override fun apply(
        playerStateChange: PlayerStateChange,
        otherPlayerStateChange: PlayerStateChange
    ): PlayerAction {
        return if (otherPlayerStateChange.previous == null && otherPlayerStateChange.current != null) {
            ConnectAction(
                otherPlayerStateChange.current.name,
                otherPlayerStateChange.current.x,
                otherPlayerStateChange.current.y
            )
        } else {
            NoAction
        }
    }
}
