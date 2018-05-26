package com.example.nikita.cardbase.DataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

val tableCardName: String = "Card"

class CardBase(context: Context) : SQLiteOpenHelper(context, tableCardName, null, 1) {

    val KEY_TABLE_NAME = tableCardName
    val KEY_ID: String = "_id"
    val KEY_NAME: String = "NAME"
    val KEY_NUMBER: String = "NUMBER"
    val KEY_COMMENT: String = "COMMENT"
    val KEY_IMAGE_FACE: String = "FACE_PATH"
    val KEY_IMAGE_CODE: String = "CODE_PATH"
    val KEY_COLOR: String = "COLOR"
    val KEY_ADD_DATA: String = "DATE"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table " + tableCardName +
                "(" + KEY_ID + " integer primary key," +
                KEY_NAME + " text," +
                KEY_NUMBER + " text," +
                KEY_COMMENT + " text," +
                KEY_IMAGE_FACE + " text," +
                KEY_IMAGE_CODE + " text, " +
                KEY_COLOR + " text," +
                KEY_ADD_DATA + " text" + ")")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}