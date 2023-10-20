package com.example.birthdayapp.models


import java.io.Serializable
import java.time.LocalDate


data class Friend(
    val id: Int,
    val userId: String,
    val name: String,
    val birthYear: Int,
    val birthMonth: Int,
    val birthDayOfMonth: Int,
    val age: Int,

    ) : Serializable {
    constructor(userId: String, name: String, birthYear: Int, birthMonth: Int, birthDayOfMonth: Int)
            : this(-1, userId, name, birthYear, birthMonth, birthDayOfMonth, 1)

    override fun toString(): String {
        return "$id, $name, $birthDayOfMonth/$birthMonth-$birthYear, $age"
    }
}
