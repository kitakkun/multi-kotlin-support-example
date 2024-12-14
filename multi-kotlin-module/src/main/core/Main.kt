fun main() {
    VersionSpecificAPI.INSTANCE = VersionSpecificAPIImpl
    VersionSpecificAPI.INSTANCE.someVersionSpecificTask()
}
