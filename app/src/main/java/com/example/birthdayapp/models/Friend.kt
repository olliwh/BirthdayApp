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


/*
    val area: Int // property type is optional since it can be inferred from the getter's return type
        get() = this.birthYear
    val birthDaystr: String = "$birthYear-$birthMonth-$birthDayOfMonth"
    @RequiresApi(Build.VERSION_CODES.O)
    val birthDay = LocalDate.parse(birthDaystr)*/
    //val birthDay: Date = Date(birthYear, birthMonth, birthDayOfMonth)
        //LocalDate.of(birthYear, birthMonth, birthDayOfMonth)
    override fun toString(): String {
        return "$id, $name, $birthDayOfMonth/$birthMonth-$birthYear, $age"
    }
}
