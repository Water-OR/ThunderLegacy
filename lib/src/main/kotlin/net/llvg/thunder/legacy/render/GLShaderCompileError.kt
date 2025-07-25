package net.llvg.thunder.legacy.render

data class GLShaderCompileError(
    @JvmField
    val type: GLShaderType,
    @JvmField
    val code: String,
    @JvmField
    val infoLog: String,
) : Error()