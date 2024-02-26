package uk.co.scarfebread.wizardbeast.engine.state.server.delta

import uk.co.scarfebread.wizardbeast.engine.state.publishable.action.DisconnectAction
import uk.co.scarfebread.wizardbeast.engine.state.publishable.action.NoAction
import uk.co.scarfebread.wizardbeast.engine.state.publishable.action.PlayerAction

class DisconnectionRule : PublishRule {
    override fun apply(
        playerStateChange: PlayerStateChange,
        otherPlayerStateChange: PlayerStateChange
    ): PlayerAction {
        return if (otherPlayerStateChange.current == null && otherPlayerStateChange.previous != null) {
            DisconnectAction(otherPlayerStateChange.previous.name)
        } else {
            NoAction
        }
    }
}
