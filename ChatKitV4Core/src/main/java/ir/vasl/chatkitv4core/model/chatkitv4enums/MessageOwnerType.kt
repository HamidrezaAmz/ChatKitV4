package ir.vasl.chatkitv4core.model.chatkitv4enums

enum class MessageOwnerType(key: String, value: Int) {

    UNDEFINED("UNDEFINED", -1),

    ALPHA("ALPHA", 0),
    ALPHA_TEXT("ALPHA_TEXT", 1),
    ALPHA_VOICE("ALPHA_VOICE", 2),
    ALPHA_VIDEO("ALPHA_VIDEO", 3),
    ALPHA_IMAGE("ALPHA_IMAGE", 4),

    BETA("BETA", 5),
    BETA_TEXT("BETA", 6),
    BETA_VOICE("BETA", 7),
    BETA_VIDEO("BETA", 8),
    BETA_IMAGE("BETA", 9),

    SYSTEM("SYSTEM", 10),
    SYSTEM_TEXT("SYSTEM_TEXT", 11),
    SYSTEM_VOICE("SYSTEM_VOICE", 12),
    SYSTEM_VIDEO("SYSTEM_VIDEO", 13),
    SYSTEM_IMAGE("SYSTEM_IMAGE", 14)

}