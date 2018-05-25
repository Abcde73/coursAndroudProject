package com.example.nikita.cardbase.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Adapter
import com.example.nikita.cardbase.Contact.contact
import com.example.nikita.cardbase.DataBase.Card
import com.example.nikita.cardbase.R
import java.util.*
import kotlin.collections.ArrayList
import android.R.attr.fragment
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.View
import android.widget.Toast
import com.example.nikita.cardbase.DataBase.CardBase
import com.example.nikita.cardbase.DataBase.CardBaseManager
import com.example.nikita.cardbase.Presenter.Presenter
import android.R.attr.name
import android.R.attr.name
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView


class MainActivity : AppCompatActivity(), contact.MainView {


    lateinit var fab: FloatingActionButton

    lateinit var recyclerView: RecyclerView
    var dataBase: CardBase? = null
    val presenter: Presenter = Presenter()
    lateinit var cardsArr: LinkedList<Card>
    lateinit var adapter: CardAdapter
    lateinit var choseDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        supportActionBar!!.setTitle("Мои карты")
        initView()
    }

    override fun initView() {

        dataBase = CardBaseManager.getDataBase(applicationContext)
        presenter.initPresenter(this, dataBase!!)

        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.floatingActionButton)

        cardsArr = presenter.getCardArr()
        adapter = CardAdapter(cardsArr, this)

        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = adapter

        choseDialog = Dialog(this)
        choseDialog.setContentView(R.layout.chose_menu)

        fab.setOnClickListener { v -> presenter.onAddClick(v) }

        val clickListener = object : CardAdapter.onItemTouchListener {

            override fun onItemClick(itemView: View, position: Int) {
                presenter.onShowClick(itemView, position)
            }

            override fun onItemTouch(itemView: View, position: Int) {

                val dialogCardName: TextView = choseDialog.findViewById(R.id.nameCardFrag)
                val dialogRedactButton: Button = choseDialog.findViewById(R.id.RedactButtonFrag)
                val dialogDeleteButton: Button = choseDialog.findViewById(R.id.DeleteButtonFrag)

                dialogCardName.setText(cardsArr[position].getCardNameUser())

                dialogRedactButton.setOnClickListener({v -> presenter.onRedactClickMain(v, cardsArr[position].getCardIdUser(), position)})
                dialogDeleteButton.setOnClickListener({ v -> presenter.onDeleteClick(v, position)})

                choseDialog.show()
            }
        }
        adapter.setOnItemClickListener(clickListener)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_sort,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.date -> {
                cardsArr = presenter.sortByDate()
                adapter = CardAdapter(cardsArr, this)
                adapter.notifyDataSetChanged()
                recyclerView.adapter = adapter
            }
            R.id.name -> {
                cardsArr = presenter.sortByName()
                adapter = CardAdapter(cardsArr, this)
                adapter.notifyDataSetChanged()
                recyclerView.adapter = adapter
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        cardsArr = presenter.getCardArr()
        adapter = CardAdapter(cardsArr, this)
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter
    }

}
