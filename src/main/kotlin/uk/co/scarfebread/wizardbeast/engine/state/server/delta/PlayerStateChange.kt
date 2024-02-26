package uk.co.scarfebread.wizardbeast.engine.state.server.delta

import uk.co.scarfebread.wizardbeast.entity.Player

data class PlayerStateChange(val previous: Player?, val current: Player?)
