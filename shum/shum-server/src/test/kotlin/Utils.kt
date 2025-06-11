object Utils {

    fun _readTextResource(path: String): String {
        return this::class.java.getResource(path)?.readText()
            ?: throw IllegalArgumentException("Resource not found: $path")
    }

}

fun readTextResource(path: String): String {
    return Utils._readTextResource(path)
}