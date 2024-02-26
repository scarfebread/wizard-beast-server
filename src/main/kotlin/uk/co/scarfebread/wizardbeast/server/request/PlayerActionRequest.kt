package uk.co.scarfebread.wizardbeast.server.request

import kotlinx.serialization.Serializable

@Serializable
data class PlayerActionRequest(val id: String, val actions: List<Action>) {
    @Serializable
    class Action(val action: String, val value: String)
}
