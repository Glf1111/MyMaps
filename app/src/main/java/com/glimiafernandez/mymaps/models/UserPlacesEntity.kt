package com.glimiafernandez.mymaps.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
@Entity(tableName= "PLaces_Table" )
data class UserPlaces(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id : Int =0,
    @ColumnInfo(name = "title") @NotNull val title:String,
    @ColumnInfo(name = "description")  val description:String? = null ,
    @ColumnInfo(name = "latitude") @NotNull val latitude:Double,
    @ColumnInfo(name = "longitude") @NotNull val longitude:Double
    )




