package com.example.nikita.cardbase.View

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.nikita.cardbase.DataBase.Card
import com.example.nikita.cardbase.R
import kotlinx.android.synthetic.main.card.view.*

class CardAdapter(val items: List<Card>, val context: Context) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {


    companion object {
        lateinit var listener: onItemTouchListener
    }


    interface onItemTouchListener {
        fun onItemClick(itemView: View, position: Int)
        fun onItemTouch(itemView: View, position: Int)
    }

    fun setOnItemClickListener(listener: onItemTouchListener) {
        CardAdapter.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.card, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val card = items.get(position)
        holder.cardNameItem.text = card.getCardNameUser()
        holder.cardDataItem.text = card.getCardAddDataUser()
        holder.cardBackground.setBackgroundColor(card.getCardColorUser())
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val cardNameItem = v.nameCard
        val cardDataItem = v.addDataCard
        val cardBackground = v.cardBackground

        init {
            v.setOnTouchListener { v, event -> onTouch(v, event) }
        }

        var then: Long = 0

        fun onTouch(v: View, event: MotionEvent): Boolean {
            if (event.action == MotionEvent.ACTION_DOWN) {
                then = System.currentTimeMillis()
            } else if (event.action == MotionEvent.ACTION_UP) {
                if (System.currentTimeMillis() - then <= 600) {
                    CardAdapter.listener.onItemClick(v, layoutPosition)
                } else {
                    CardAdapter.listener.onItemTouch(v, layoutPosition)
                }
            }
            return true
        }
    }

}