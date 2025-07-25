package net.llvg.thunder.legacy.render

data class GLProgramLinkError(
    @JvmField
    val infoLog: String
) : Error()