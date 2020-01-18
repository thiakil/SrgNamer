package com.thiakil.intellij.names

/**
 * Created by Thiakil on 11/01/2020.
 */
enum class MemberType {
    METHOD,
    FIELD,
    PARAM;

    companion object {
        fun fromName(srgName: String): MemberType? {
            return when {
                srgName.startsWith("func_") -> METHOD
                srgName.startsWith("field_") -> FIELD
                srgName.startsWith("p_") -> PARAM
                else -> null
            }
        }
    }
}