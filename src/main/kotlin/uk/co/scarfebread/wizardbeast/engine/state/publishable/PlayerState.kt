package uk.co.scarfebread.wizardbeast.engine.state.publishable

import kotlinx.serialization.Serializable

@Serializable
data class PlayerState(
    val id: String,
    val name: String,
    var x: Float = 0f,
    var y: Float = 0f,
)
