interface VersionSpecificAPI {
    companion object {
        lateinit var INSTANCE: VersionSpecificAPI
    }

    fun someVersionSpecificTask()
}
