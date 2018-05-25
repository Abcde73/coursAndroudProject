package com.example.nikita.cardbase.Model

import android.content.ContentValues
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Environment
import android.widget.ResourceCursorTreeAdapter
import com.example.nikita.cardbase.Contact.contact
import com.example.nikita.cardbase.DataBase.Card
import com.example.nikita.cardbase.DataBase.CardBase
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.os.Environment.getExternalStorageDirectory
import android.provider.MediaStore

class Model : contact.Model {

    var dataBase: CardBase? = null

    lateinit var pic: Bitmap

    var colorArr = IntArray(4)

    override fun getSortCardArr(order: String?): LinkedList<Card> {
        val cardArr = LinkedList<Card>()
        val cursor: Cursor = dataBase!!.readableDatabase.query(dataBase!!.KEY_TABLE_NAME, null, null, null, null, null, order)
        while (cursor.moveToNext()) {
            val newCard = Card()
            newCard.setCardIdUser(cursor.getLong(cursor.getColumnIndex(dataBase!!.KEY_ID)))
            newCard.setCardNameUser(cursor.getString(cursor.getColumnIndex(dataBase!!.KEY_NAME)))
            newCard.setCardNumberUser(cursor.getString(cursor.getColumnIndex(dataBase!!.KEY_NUMBER)))
            newCard.setCardDescriptionUser(cursor.getString(cursor.getColumnIndex(dataBase!!.KEY_COMMENT)))
            newCard.setCardFaceImageUser(cursor.getString(cursor.getColumnIndex(dataBase!!.KEY_IMAGE_FACE)))
            newCard.setCardQrCodeImageUser(cursor.getString(cursor.getColumnIndex(dataBase!!.KEY_IMAGE_CODE)))
            newCard.setCardColorUser(parseColor(cursor.getString(cursor.getColumnIndex(dataBase!!.KEY_COLOR))))
            newCard.setCardAddDataUser(cursor.getString(cursor.getColumnIndex(dataBase!!.KEY_ADD_DATA)))
            if (order == dataBase!!.KEY_ADD_DATA) {
                cardArr.add(0,newCard)
            } else {
                cardArr.add(newCard)
            }
        }
        cursor.close()
        return cardArr
    }

    override fun addNewCard(cv: ContentValues) {
        dataBase!!.writableDatabase.insert(dataBase!!.KEY_TABLE_NAME, null, cv)
    }

    override fun setDataCardBase(dataBase: CardBase) {
        this.dataBase = dataBase
    }

    override fun redactCard(id: Long, cv: ContentValues) {
         val sel = "${dataBase!!.KEY_ID} = ?"
        val idRow = Array<String>(1) { id.toString() }
        dataBase!!.writableDatabase.update(dataBase!!.KEY_TABLE_NAME, cv, sel , idRow)
    }

    override fun deleteCard(id: Long) {
        dataBase!!.writableDatabase.execSQL("DELETE FROM " + dataBase!!.KEY_TABLE_NAME +
                " WHERE _id='" + id + "'")
    }

    override fun getInfoCardShow(id: Long): Card {
        val selection = "_id = ?"
        val selectionArgs = Array<String>(1) { id.toString() }
        val cursor = dataBase!!.readableDatabase.query(dataBase!!.KEY_TABLE_NAME, null, selection, selectionArgs, null, null, null)
        val card = Card()
        if (cursor.moveToFirst()) {
            card.setCardIdUser(cursor.getLong(cursor.getColumnIndex(dataBase!!.KEY_ID)))
            card.setCardNameUser(cursor.getString(cursor.getColumnIndex(dataBase!!.KEY_NAME)))
            card.setCardNumberUser(cursor.getString(cursor.getColumnIndex(dataBase!!.KEY_NUMBER)))
            card.setCardDescriptionUser(cursor.getString(cursor.getColumnIndex(dataBase!!.KEY_COMMENT)))
            card.setCardFaceImageUser(cursor.getString(cursor.getColumnIndex(dataBase!!.KEY_IMAGE_FACE)))
            card.setCardQrCodeImageUser(cursor.getString(cursor.getColumnIndex(dataBase!!.KEY_IMAGE_CODE)))
            card.setCardAddDataUser(cursor.getString(cursor.getColumnIndex(dataBase!!.KEY_ADD_DATA)))
        }
        cursor.close()
        return card
    }

    @Throws(IOException::class)
    fun createImageFile(storageDir: File): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        if (!storageDir.exists()) storageDir.mkdir()
        val imageFile = createTempFile(imageFileName, ".jpg", storageDir)
        return imageFile
    }

    fun getScaleBitmap(imageViewWidth: Int, imageViewHeight: Int, imageFilePath: String): Bitmap {

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFilePath, options)
        val bitmapWidth = options.outWidth
        val bitmapHeight = options.outHeight
        val scaleFactory = Math.min(bitmapWidth / imageViewWidth, bitmapHeight / imageViewHeight)
        options.inSampleSize = scaleFactory
        options.inJustDecodeBounds = false

        return BitmapFactory.decodeFile(imageFilePath, options)
    }

    fun setBitmap(pic: Bitmap) {
        this.pic = pic
        val getColor = ColorForBitmap()
        getColor.start()
    }

    fun getColor(): IntArray{
        return colorArr
    }

    fun codeColor(colorCode: IntArray): String {
        var stringColor = "${colorCode[0]},${colorCode[1]},${colorCode[2]},${colorCode[3]},"
        return stringColor
    }

    fun parseColor(colorCode: String): IntArray {

        val color = IntArray(4)
        var value = ""
        var currentArgbValue = 0
        for (i in colorCode) {
            if (i != ',') {
                value += i
            } else {
                color[currentArgbValue] = value.toInt()
                currentArgbValue++
                value = ""
            }
        }
        return color
    }

    inner class ColorForBitmap : Thread() {

        override fun run() {
            var A: Int = 0
            var R: Int = 0
            var G: Int = 0
            var B: Int = 0
            var pixelColor: Int
            val width = pic.width / 3
            val height = pic.height / 3
            val size = width * height

            for (x in 0 until width) {
                for (y in 0 until height) {
                    pixelColor = pic.getPixel(x + width , y + height )
                    A += Color.alpha(pixelColor)
                    R += Color.red(pixelColor)
                    G += Color.green(pixelColor)
                    B += Color.blue(pixelColor)
                }
            }

            colorArr[0] = A / size
            colorArr[1] = R / size
            colorArr[2] = G / size
            colorArr[3] = B / size
        }
    }



}
