package uk.co.scarfebread.wizardbeast.engine.state.publishable.action

import kotlinx.serialization.Serializable

@Serializable
data class ConnectAction(
    val name: String,
    val x: String,
    val y: String,
    val action: String = "connect",
) : PlayerAction
