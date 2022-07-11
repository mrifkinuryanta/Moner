package com.mrndevs.moner.enum

enum class EnumRecordType(val type: String) {
    EXPENSE("expense"), INCOME("income");

    companion object {
        fun state(type: String) = values().first { it.type == type }
    }
}