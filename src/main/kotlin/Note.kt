class Note(
        override val name: String,
        val body: String
) : File {
    override fun toString(): String {
        return "$name\n$body"
    }
}