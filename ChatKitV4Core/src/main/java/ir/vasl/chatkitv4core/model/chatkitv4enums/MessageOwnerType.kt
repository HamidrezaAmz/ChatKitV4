package ir.vasl.chatkitv4core.model.chatkitv4enums

enum class MessageOwnerType(key: String, value: Int) {

    UNDEFINED("UNDEFINED", -1),

    SELF("SELF", 0),
    SELF_TEXT("SELF_TEXT", 1),
    SELF_VOICE("SELF_VOICE", 2),
    SELF_VIDEO("SELF_VIDEO", 3),
    SELF_IMAGE("SELF_IMAGE", 4),

    OTHER("OTHER", 5),
    OTHER_TEXT("OTHER", 6),
    OTHER_VOICE("OTHER", 7),
    OTHER_VIDEO("OTHER", 8),
    OTHER_IMAGE("OTHER", 9),

    SYSTEM("SYSTEM", 10),
    SYSTEM_TEXT("SYSTEM", 11),
    SYSTEM_VOICE("SYSTEM", 12),
    SYSTEM_VIDEO("SYSTEM", 13),
    SYSTEM_IMAGE("SYSTEM", 14)

}