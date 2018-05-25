package com.example.nikita.cardbase.View

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.nikita.cardbase.Contact.contact
import com.example.nikita.cardbase.DataBase.CardBase
import com.example.nikita.cardbase.DataBase.CardBaseManager
import com.example.nikita.cardbase.Presenter.Presenter
import com.example.nikita.cardbase.R
import com.example.nikita.cardbase.R.id.codeScreen
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_show_card.*

class ShowCardActivity : AppCompatActivity(), contact.ShowView {

    val presenter = Presenter()
    val dataBase: CardBase? = CardBaseManager.getDataBase(this)

    lateinit var cardShowName: TextView
    lateinit var cardShowNumber: TextView
    lateinit var cardShowAddDate: TextView
    lateinit var cardShowDiscription: TextView
    lateinit var codeImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_card)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init()
    }

    override fun init() {

        cardShowName = findViewById(R.id.cardName)
        cardShowNumber = findViewById(R.id.cardNumber)
        cardShowAddDate = findViewById(R.id.cardDataAdd)
        cardShowDiscription = findViewById(R.id.cardComment)
        codeImage = findViewById(R.id.codeScreen)

        presenter.initPresenter(this, dataBase!!)
        val idCard: Long = intent.getStringExtra("id").toLong()
        presenter.getCardInfo(idCard)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_show,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        presenter.onRedactClickShow(intent.getStringExtra("id").toLong())
        return super.onOptionsItemSelected(item)
    }
}
