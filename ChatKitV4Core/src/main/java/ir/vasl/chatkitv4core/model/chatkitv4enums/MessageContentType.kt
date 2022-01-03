package ir.vasl.chatkitv4core.model.chatkitv4enums

enum class MessageContentType(key: String, value: Int) {
    UNDEFINED("UNDEFINED", -1),
    TEXT("TEXT", 0),
    IMAGE("IMAGE", 1),
    VIDEO("SYSTEM", 2),
    VOICE("VOICE", 3),
    DOCUMENT("DOCUMENT", 4)
}