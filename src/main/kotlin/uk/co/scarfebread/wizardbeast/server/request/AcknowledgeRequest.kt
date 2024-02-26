package uk.co.scarfebread.wizardbeast.server.request

import kotlinx.serialization.Serializable

@Serializable
data class AcknowledgeRequest(val stateId: Long)
