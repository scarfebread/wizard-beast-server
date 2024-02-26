package uk.co.scarfebread.wizardbeast.engine.state.publishable.action

import kotlinx.serialization.Serializable

@Serializable
data class MoveAction(
    val name: String,
    val x: String,
    val y: String,
    val action: String = "move",
) : PlayerAction
