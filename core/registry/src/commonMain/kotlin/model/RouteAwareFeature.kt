package model

interface RouteAwareFeature {
    fun canHandleRoute(route: String): Boolean
}