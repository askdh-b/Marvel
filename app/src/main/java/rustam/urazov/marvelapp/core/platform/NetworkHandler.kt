package rustam.urazov.marvelapp.core.platform

import javax.inject.Singleton

@Singleton
interface NetworkHandler {

    fun isNetworkAvailable(): Boolean

}