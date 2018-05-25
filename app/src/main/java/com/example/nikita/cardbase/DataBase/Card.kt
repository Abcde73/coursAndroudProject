package com.example.nikita.cardbase.DataBase

import android.graphics.Color
import java.util.*

class Card {

    private var cardId: Long = 0
    private var cardName: String = ""
    private var cardNumber: String = ""
    private var cardDescription: String = ""
    private var cardAddDate: String = ""
    private var cardColor: IntArray = intArrayOf(4)
    private var cardFaceImage: String = ""
    private var cardQrCodeImage: String = ""

    constructor() {

    }

    constructor(cardId: Long, cardName: String, cardNumber: String, cardAddDate: String, cardColor: IntArray,
                cardFaceImage: String, cardQrCodeImage: String, cardDescription: String) {
        this.cardId = cardId
        this.cardName = cardName
        this.cardNumber = cardNumber
        this.cardAddDate = cardAddDate
        this.cardDescription = cardDescription
        this.cardColor = cardColor
        this.cardFaceImage = cardFaceImage
        this.cardQrCodeImage = cardQrCodeImage
    }

    fun getCardIdUser(): Long {
        return cardId
    }

    fun setCardIdUser(cardId: Long) {
        this.cardId = cardId
    }

    fun getCardNameUser(): String {
        return cardName
    }

    fun setCardNameUser(cardName: String) {
        this.cardName = cardName
    }

    fun getCardNumberUser(): String {
        return cardNumber
    }

    fun setCardNumberUser(cardNumber: String) {
        this.cardNumber = cardNumber
    }

    fun getCardDescriptionUser(): String {
        return cardDescription
    }

    fun setCardDescriptionUser(cardDescription: String) {
        this.cardDescription = cardDescription
    }

    fun getCardAddDataUser(): String {
        return cardAddDate
    }

    fun setCardAddDataUser(cardAddDate: String) {
        this.cardAddDate = cardAddDate
    }

    fun getCardColorUser(): Int {
        return Color.argb(cardColor[0], cardColor[1], cardColor[2], cardColor[3])
    }

    fun setCardColorUser(cardColor: IntArray) {
        this.cardColor = cardColor
    }

    fun getCardFaceImageUser(): String {
        return cardFaceImage
    }

    fun setCardFaceImageUser(cardFaceImage: String) {
        this.cardFaceImage = cardFaceImage
    }

    fun getCardQrCodeImageUser(): String {
        return cardQrCodeImage
    }

    fun setCardQrCodeImageUser(cardQrCodeImage: String) {
        this.cardQrCodeImage = cardQrCodeImage
    }

}