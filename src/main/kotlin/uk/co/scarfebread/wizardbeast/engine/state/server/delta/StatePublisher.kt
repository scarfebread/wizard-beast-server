package uk.co.scarfebread.wizardbeast.engine.state.server.delta

import uk.co.scarfebread.wizardbeast.engine.state.server.GameState
import uk.co.scarfebread.wizardbeast.entity.Player

class StatePublisher {
    fun shouldPublish() {
    }
    fun buildDelta(player: Player, currentState: GameState, lastConfirmedState: GameState) {
    }
}

/**
 * game state contains everything
 * this service can compare the new state with the last acknowledged state
 *  * filtering done after this step
 *
 *
 * 1 - ack
 * 2 - ack
 * 3
 * 4 - ?
 * 5
 * 6
 * 7
 * 8
 * 9
 * 10
 */
