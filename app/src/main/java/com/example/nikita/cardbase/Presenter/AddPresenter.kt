package com.example.nikita.cardbase.Presenter

import android.content.ContentValues
import android.view.View
import com.example.nikita.cardbase.Contact.Contact
import com.example.nikita.cardbase.DataBase.CardBase
import com.example.nikita.cardbase.Model.Model
import com.example.nikita.cardbase.View.AddActivity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import android.widget.ImageView
import android.widget.Toast
import com.example.nikita.cardbase.DataBase.Card
import com.squareup.picasso.Picasso
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class AddPresenter : Contact.AddPresenter {

    lateinit var addActivity: AddActivity
    lateinit var currentImage: ImageView
    lateinit var model: Model

    var dataBase: CardBase? = null
    var imageFaceUri: Uri? = null
    var imageCodeUri: Uri? = null

    var imageFaceFilePath: String = ""
    var imageCodeFilePath: String = ""

    companion object {
        var currentSavedCard = Card()
    }

    override fun initAddPresenter(addActivity: AddActivity, dataBase: CardBase) {
        this.addActivity = addActivity
        this.dataBase = dataBase
        model = Model()
        model.setDataCardBase(dataBase)
    }

    override fun onAddClick() {

        if (validation()) {
            val cv = getCardInfoInFild()
            model.addNewCard(cv)
            addActivity.finish()
        }
    }

    fun onRedactClick(id: Long) {
        if (validation()) {
            val cv = getCardInfoInFild()
            model.redactCard(id, cv)
            addActivity.finish()
        }
    }

    fun getCardInfoInFild(): ContentValues {

        val cv = ContentValues()
        cv.put(dataBase!!.KEY_NAME, addActivity.editTextCardName.text.toString())
        cv.put(dataBase!!.KEY_NUMBER, addActivity.editTextCardNumber.text.toString())
        cv.put(dataBase!!.KEY_COMMENT, addActivity.editTextCardComment.text.toString())
        cv.put(dataBase!!.KEY_IMAGE_FACE, imageFaceFilePath)
        cv.put(dataBase!!.KEY_IMAGE_CODE, imageCodeFilePath)
        val color: IntArray = model.getColor()
        val stringColor: String = model.codeColor(color)
        if (stringColor != "0,0,0,0,")
            cv.put(dataBase!!.KEY_COLOR, stringColor)
        cv.put(dataBase!!.KEY_ADD_DATA, SimpleDateFormat("dd.MM.yyyy").format(Date()))
        return cv
    }

    private fun validation(): Boolean {

        var emptyErrorString: String = ""
        if (addActivity.editTextCardName.text.toString().isEmpty()) emptyErrorString += "имя "
        if (addActivity.editTextCardNumber.text.toString().isEmpty()) emptyErrorString += "номер "
        if (imageFaceFilePath.isEmpty()) emptyErrorString += "фото карты "
        if (imageCodeFilePath.isEmpty()) emptyErrorString += "фото кода"
        if (!emptyErrorString.isEmpty()) {
            Toast.makeText(addActivity, "Добавь: $emptyErrorString", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun deleteImage() {

        if (imageFaceUri != null) {
            deleteImage(imageFaceUri!!)
            imageFaceFilePath = ""
        }

        if (imageCodeUri != null) {
            deleteImage(imageCodeUri!!)
            imageCodeFilePath = ""
        }
    }

    fun compuleColor() {
        var colorBit: Bitmap
        if (!imageFaceFilePath.contains("://")) {
            colorBit = model.getScaleBitmap(1000, 1000, imageFaceFilePath)
        } else {
            val way = addActivity.contentResolver.openInputStream(Uri.parse(imageFaceFilePath))
            colorBit = BitmapFactory.decodeStream(way)
        }
        model.setBitmap(colorBit)
    }

    override fun onFaceImageAddClick(v: View) {

        val requestCode = "Face"

        if (imageFaceUri != null) {
            deleteImage(imageFaceUri!!)
        }

        currentImage = v as ImageView
        imageFaceUri = createFile(requestCode)
        addActivity.startCamera(imageFaceUri!!, imageFaceFilePath)
    }

    override fun onCodeImageClick(v: View) {

        val requestCode = "Code"

        if (imageCodeUri != null) {
            deleteImage(imageCodeUri!!)
        }

        currentImage = v as ImageView
        imageCodeUri = createFile(requestCode)
        addActivity.startCamera(imageCodeUri!!, imageCodeFilePath)
    }

    fun onCameraInputClick(v: View) {

        savedCard()
        addActivity.zXingScannerView = ZXingScannerView(addActivity.applicationContext)
        addActivity.setContentView(addActivity.zXingScannerView)
        addActivity.zXingScannerView!!.setResultHandler(addActivity)
        addActivity.zXingScannerView!!.startCamera()
    }

    private fun setImageFacePathFile(imageFile: File) {
        imageFaceFilePath = imageFile.absolutePath
    }

    fun setImageFacePathFile(imageFile: Uri) {
        imageFaceFilePath = "${imageFile.scheme}:${imageFile.schemeSpecificPart}"
    }

    private fun setImageCodePathFile(imageFile: File) {
        imageCodeFilePath = imageFile.absolutePath
    }

    fun setImageCodePathFile(imageFile: Uri) {
        imageCodeFilePath = "${imageFile.scheme}:${imageFile.schemeSpecificPart}"
    }

    fun getBitmapPicture(imageFilePath: String): Bitmap {

        val imageViewWidth = 1000
        val imageViewHeight = 1000
        val minPicture: Bitmap = model.getScaleBitmap(imageViewWidth, imageViewHeight, imageFilePath)
        return minPicture
    }

    fun createFile(imageType: String): Uri {

        val storageDir = addActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        storageDir.mkdir()
        val imageFile = model.createImageFile(storageDir)
        when (imageType) {
            "Face" -> {
                setImageFacePathFile(imageFile)
            }
            "Code" -> {
                setImageCodePathFile(imageFile)
            }
            else -> {
                Toast.makeText(addActivity, "Ошибка создания файла", Toast.LENGTH_SHORT).show()
            }
        }

        val authorities = addActivity.packageName + ".fileprovider"
        val imageUri = FileProvider.getUriForFile(addActivity, authorities, imageFile)

        return imageUri
    }

    fun currentImage(): ImageView {
        return currentImage
    }

    fun deleteImage(imageUri: Uri) {
        addActivity.contentResolver.delete(imageUri, null, null)
    }

    fun initRedactCard(card: Card) {

        addActivity.editTextCardName.setText(card.getCardNameUser())
        addActivity.editTextCardNumber.setText(card.getCardNumberUser())

        if (!card.getCardFaceImageUser().isEmpty()) {
            Picasso.get().load(card.getCardFaceImageUser()).into(addActivity.imageViewFace)
            imageFaceFilePath = card.getCardFaceImageUser()
        }
        if (!card.getCardQrCodeImageUser().isEmpty()) {
            Picasso.get().load(card.getCardQrCodeImageUser()).into(addActivity.imageViewCode)
            imageCodeFilePath = card.getCardQrCodeImageUser()
        }
        addActivity.editTextCardComment.setText(card.getCardDescriptionUser())
    }

    fun savedCard() {
        currentSavedCard.setCardNameUser(addActivity.editTextCardName.text.toString())
        currentSavedCard.setCardNumberUser(addActivity.editTextCardNumber.text.toString())
        currentSavedCard.setCardDescriptionUser(addActivity.editTextCardComment.text.toString())
        if (imageFaceFilePath.contains("://")) currentSavedCard.setCardFaceImageUser(imageFaceFilePath)
        if (imageCodeFilePath.contains("://")) currentSavedCard.setCardQrCodeImageUser(imageCodeFilePath)
    }

    fun getSavedCard(): Card {
        return currentSavedCard
    }

    fun deleteLatest() {
        val f = File(Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera")
        val files = f.listFiles()
        Arrays.sort(files, object : Comparator<Any> {
            override fun compare(o1: Any, o2: Any): Int {
                return if ((o1 as File).lastModified() > (o2 as File).lastModified()) {
                    return -1
                } else if (o1.lastModified() < o2.lastModified()) {
                    return 1
                } else {
                    return 0
                }
            }

        })
        files!![0].delete()
    }

}