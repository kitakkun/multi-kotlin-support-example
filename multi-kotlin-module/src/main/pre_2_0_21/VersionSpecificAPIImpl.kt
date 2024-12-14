object VersionSpecificAPIImpl: VersionSpecificAPI {
    override fun someVersionSpecificTask() {
        println("Kotlin pre 2.0.21!")
    }
}
