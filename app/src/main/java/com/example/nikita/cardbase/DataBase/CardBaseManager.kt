package com.example.nikita.cardbase.DataBase

import android.content.Context


class CardBaseManager() {

    companion object {

        private var dataCardBase: CardBase? = null

        fun getDataBase(context: Context): CardBase? {
            if (dataCardBase == null) {
                dataCardBase = CardBase(context)
            }
            return dataCardBase
        }
    }
}