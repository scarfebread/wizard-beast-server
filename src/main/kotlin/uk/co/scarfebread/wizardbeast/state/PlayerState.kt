package uk.co.scarfebread.wizardbeast.state

class PlayerState {
    private val players = mutableListOf<Player>()

    fun addPlayer(player: Player): Player {
        players.add(player)
        return player
    }

    fun removePlayer(userId: String) = players.removeIf { it.id == userId }

    fun getPlayers() = players.toList()

    fun getPlayer(id: String) = getPlayers().firstOrNull { it.id == id }
}
