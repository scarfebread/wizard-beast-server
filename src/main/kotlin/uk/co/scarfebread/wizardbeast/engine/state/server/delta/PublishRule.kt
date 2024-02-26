package uk.co.scarfebread.wizardbeast.engine.state.server.delta

import uk.co.scarfebread.wizardbeast.engine.state.publishable.action.PlayerAction

interface PublishRule {
    fun apply(
        playerStateChange: PlayerStateChange,
        otherPlayerStateChange: PlayerStateChange
    ): PlayerAction
}
