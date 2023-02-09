package com.example.pal.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "pets_tablee")
data class Pet(val age: String,
               val animal: String,
               val breed: String,
               val description: String,
               val name: String,
               val pic: String,
               val sex: String,
               @PrimaryKey
                val id:Int) {
    constructor(): this("", "", "", "", "", "", "",0)
}

