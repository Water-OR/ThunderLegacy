@file:JvmName("JsonUtil")

package net.llvg.thunder.legacy.util

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlin.internal.InlineOnly

val JsonParser = JsonParser()

@InlineOnly
inline operator fun JsonObject.contains(key: String) = has(key)