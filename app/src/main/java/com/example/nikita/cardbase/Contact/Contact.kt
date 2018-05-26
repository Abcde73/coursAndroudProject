package com.example.nikita.cardbase.Contact

import android.content.ContentValues
import android.view.View
import com.example.nikita.cardbase.DataBase.Card
import com.example.nikita.cardbase.DataBase.CardBase
import com.example.nikita.cardbase.View.AddActivity
import com.example.nikita.cardbase.View.MainActivity
import com.example.nikita.cardbase.View.ShowCardActivity
import java.util.*

interface Contact {

    interface MainView {
        fun initView()
    }

    interface AddView {
        fun init()
    }

    interface ShowView {
        fun init()
    }

    interface Presenter {
        fun initPresenter(mainActivity: MainActivity, dataBase: CardBase)
        fun initPresenter(showActivity: ShowCardActivity, dataBase: CardBase)
        fun initOrder()
        fun onAddClick(v: View)
        fun onRedactClickShow (id: Long)
        fun onRedactClickMain(v: View, id: Long)
        fun onDeleteClick(v: View, position: Int)
        fun onShowClick(v: View, position: Int)
        fun getCardArr(): LinkedList<Card>
    }

    interface AddPresenter {

        fun initAddPresenter(addActivity: AddActivity, dataBase: CardBase)
        fun onAddClick()
        fun onFaceImageAddClick(v: View)
        fun onCodeImageClick(v: View)

    }

    interface Model {

        fun addNewCard(cv: ContentValues)
        fun deleteCard(id: Long)
        fun redactCard(id: Long, cv: ContentValues)
        fun getInfoCardShow(id: Long) : Card
        fun setDataCardBase(dataBase: CardBase)
        fun getSortCardArr(order: String?): LinkedList<Card>

    }


}
