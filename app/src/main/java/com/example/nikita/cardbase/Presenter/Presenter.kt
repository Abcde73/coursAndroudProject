package com.example.nikita.cardbase.Presenter

import android.content.Intent
import android.net.Uri
import android.view.View
import com.example.nikita.cardbase.Contact.Contact
import com.example.nikita.cardbase.DataBase.Card
import com.example.nikita.cardbase.DataBase.CardBase
import com.example.nikita.cardbase.Model.Model
import com.example.nikita.cardbase.View.AddActivity
import com.example.nikita.cardbase.View.MainActivity
import com.example.nikita.cardbase.View.ShowCardActivity
import com.squareup.picasso.Picasso
import java.util.*

class Presenter : Contact.Presenter {

    val model = Model()
    lateinit var dataBase: CardBase
    lateinit var mainView: MainActivity
    lateinit var showView: ShowCardActivity

    companion object {
        var sortOrder: String? = null
    }

    override fun initPresenter(mainActivity: MainActivity, dataBase: CardBase) {

        this.dataBase = dataBase
        mainView = mainActivity
        model.setDataCardBase(this.dataBase)
        if (sortOrder == null) {
            initOrder()
        }
    }

    override fun initPresenter(showActivity: ShowCardActivity, dataBase: CardBase) {

        this.dataBase = dataBase
        showView = showActivity
        model.setDataCardBase(this.dataBase)


    }

    override fun initOrder() {
        sortOrder = dataBase!!.KEY_ADD_DATA
    }

    override fun onShowClick(v: View, position: Int) {

        val intentStartShowActivity: Intent = Intent(mainView, ShowCardActivity::class.java)
        intentStartShowActivity.putExtra("id", mainView.cardsArr[position].getCardIdUser().toString())
        mainView.startActivity(intentStartShowActivity)
    }

    override fun onAddClick(v: View) {
        val intentStartAddActivity: Intent = Intent(mainView, AddActivity::class.java)
        mainView.startActivity(intentStartAddActivity)
    }

    override fun onDeleteClick(v: View, position: Int) {

        model.deleteCard(mainView.cardsArr[position].getCardIdUser())

        mainView.contentResolver.delete(Uri.parse(mainView.cardsArr[position].getCardFaceImageUser()), null, null)
        mainView.contentResolver.delete(Uri.parse(mainView.cardsArr[position].getCardQrCodeImageUser()), null, null)
        mainView.cardsArr.removeAt(position)
        mainView.adapter.notifyDataSetChanged()
        mainView.choseDialog.cancel()
    }

    override fun onRedactClickMain(v: View, id: Long) {
        val intentStartAddActivity: Intent = Intent(mainView, AddActivity::class.java)
        intentStartAddActivity.putExtra("id", id.toString())
        mainView.startActivity(intentStartAddActivity)
        mainView.choseDialog.cancel()
    }

    override fun onRedactClickShow(id: Long) {
        val intentStartAddActivity: Intent = Intent(showView, AddActivity::class.java)
        intentStartAddActivity.putExtra("id", id.toString())
        showView.startActivity(intentStartAddActivity)
        showView.finish()
    }

    override fun getCardArr(): LinkedList<Card> {

        val cardArr = model.getSortCardArr(sortOrder)
        return cardArr
    }

    fun getCardInfo(id: Long) {
        val card = model.getInfoCardShow(id)
        showView.cardShowName.setText(card.getCardNameUser())
        showView.cardShowNumber.setText(card.getCardNumberUser())
        showView.cardShowAddDate.setText(card.getCardAddDataUser())
        showView.cardShowDiscription.setText(card.getCardDescriptionUser())
        Picasso.get().load(card.getCardQrCodeImageUser()).into(showView.codeImage)
    }

    fun sortByDate(): LinkedList<Card> {
        sortOrder = dataBase!!.KEY_ADD_DATA
        val cardArr = model.getSortCardArr(sortOrder)
        return cardArr
    }

    fun sortByName(): LinkedList<Card> {
        sortOrder = dataBase!!.KEY_NAME
        val cardArr = model.getSortCardArr(sortOrder)
        return cardArr
    }

}