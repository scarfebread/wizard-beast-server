package uk.co.scarfebread.wizardbeast.state

class PlayerState {
    private val players = mutableListOf<Player>()

    fun addPlayer(player: Player) = players.add(player)

    fun removePlayer(userId: String) = players.removeIf { it.id == userId }

    fun getPlayers() = players.toList()
}
