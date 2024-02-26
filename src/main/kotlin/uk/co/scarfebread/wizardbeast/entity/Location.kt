package uk.co.scarfebread.wizardbeast.entity

data class Location(val x: Float?, val y: Float?) {
    fun isCloseTo(location: Location): Boolean {
        return if (x == null || y == null) {
            false
        } else if (location.x == null || location.y == null) {
            false
        } else {
            val xDistance = x - location.x
            val yDistance = y - location.y

            if (xDistance < X_DISTANCE_LIMIT && xDistance > -X_DISTANCE_LIMIT) {
                true
            } else {
                yDistance < Y_DISTANCE_LIMIT && yDistance > -Y_DISTANCE_LIMIT
            }
        }
    }

    companion object {
        private const val Y_DISTANCE_LIMIT = 100
        private const val X_DISTANCE_LIMIT = 100
    }
}
