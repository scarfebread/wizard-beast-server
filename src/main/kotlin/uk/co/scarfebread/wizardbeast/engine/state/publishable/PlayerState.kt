package uk.co.scarfebread.wizardbeast.engine.state.publishable

import kotlinx.serialization.Serializable

@Serializable
data class PlayerState(
    val name: String,
    var x: String = "",
    var y: String = "",
)
