package ir.vasl.chatkitv4core.model.chatkitv4enums

enum class MessageOwnerType(key: String, value: Int) {

    UNDEFINED("UNDEFINED", -1),

    ALPHA("ALPHA", 100),
    ALPHA_TEXT("ALPHA_TEXT", 101),
    ALPHA_VOICE("ALPHA_VOICE", 102),
    ALPHA_VIDEO("ALPHA_VIDEO", 103),
    ALPHA_IMAGE("ALPHA_IMAGE", 104),
    ALPHA_DOCUMENT("ALPHA_DOCUMENT", 105),

    BETA("BETA", 200),
    BETA_TEXT("BETA_TEXT", 201),
    BETA_VOICE("BETA_VOICE", 202),
    BETA_VIDEO("BETA_VIDEO", 203),
    BETA_IMAGE("BETA_IMAGE", 204),
    BETA_DOCUMENT("BETA_DOCUMENT", 205),

    SYSTEM("SYSTEM", 300),
    SYSTEM_TEXT("SYSTEM_TEXT", 301),
    SYSTEM_VOICE("SYSTEM_VOICE", 302),
    SYSTEM_VIDEO("SYSTEM_VIDEO", 303),
    SYSTEM_IMAGE("SYSTEM_IMAGE", 304),
    SYSTEM_DOCUMENT("SYSTEM_DOCUMENT", 305)

}