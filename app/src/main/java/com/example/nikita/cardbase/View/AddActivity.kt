package com.example.nikita.cardbase.View

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import com.example.nikita.cardbase.Contact.contact
import com.example.nikita.cardbase.DataBase.Card
import com.example.nikita.cardbase.DataBase.CardBase
import com.example.nikita.cardbase.DataBase.CardBaseManager
import com.example.nikita.cardbase.Presenter.AddPresenter
import com.example.nikita.cardbase.R
import android.R.attr.data
import android.app.Activity
import android.app.VoiceInteractor
import android.content.pm.ActivityInfo
import android.support.v4.app.NotificationCompat.getExtras
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.google.zxing.Result
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AddActivity : AppCompatActivity(), contact.AddView, ZXingScannerView.ResultHandler {

    /*Navigation element*/
    lateinit var editTextCardName: EditText
    lateinit var editTextCardNumber: EditText
    lateinit var editTextCardComment: EditText
    lateinit var imageViewFace: ImageView
    lateinit var imageViewCode: ImageView

    lateinit var cameraButton: ImageButton
    lateinit var zXingScannerView: ZXingScannerView

    lateinit var presenter: AddPresenter
    var dataBase: CardBase? = null

    lateinit var currentUri: Uri
    val CAMERA_REQEST_CODE = 0
    lateinit var imagePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        supportActionBar!!.setTitle("Добавить новую карту")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        init()

        if (intent.getStringExtra("id") != null) {
            val id = intent.getStringExtra("id").toLong()
            presenter.initRedactCard(presenter.model.getInfoCardShow(id))
            supportActionBar!!.setTitle("Редактирование")
        }
    }

    override fun init() {

        editTextCardName = findViewById(R.id.editCardName)
        editTextCardNumber = findViewById(R.id.editCardNumber)
        editTextCardComment = findViewById(R.id.comment)
        imageViewFace = findViewById(R.id.imageViewFace)
        imageViewCode = findViewById(R.id.imageViewCode)

        cameraButton = findViewById(R.id.cameraBarcodeInput)

        dataBase = CardBaseManager.getDataBase(applicationContext)

        presenter = AddPresenter()
        presenter.initAddPresenter(this, dataBase!!)

        imageViewFace.setOnClickListener({ v -> presenter.onFaceImageAddClick(v) })
        imageViewCode.setOnClickListener({ v -> presenter.onCodeImageClick(v) })

        cameraButton.setOnClickListener({ v -> presenter.onCameraInputClick(v) })

    }

    override fun handleResult(result: Result) {
        zXingScannerView.removeAllViewsInLayout()
        zXingScannerView.stopCamera()
        setContentView(R.layout.activity_add)
        init()
        presenter.initRedactCard(presenter.getSavedCard())
        editTextCardNumber.setText(result.text)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_REQEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (presenter.currentImage == imageViewFace) {
                        presenter.compuleColor()
                    }
                    CropImage.activity(currentUri).setAspectRatio(1, 1).setCropShape(CropImageView.CropShape.RECTANGLE).setOutputUri(currentUri).start(this)
                    presenter.currentImage().setImageBitmap(presenter.getBitmapPicture(imagePath))
                }
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    val result: CropImage.ActivityResult = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        // val resultUri: Uri = result.getUri()

                        if (presenter.currentImage == imageViewFace) {
                            presenter.setImageFacePathFile(currentUri)
                        } else {
                            presenter.setImageCodePathFile(currentUri)
                        }
                        Picasso.get().load(currentUri).into(presenter.currentImage)
                        presenter.deleteLatest()
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Toast.makeText(this, "Ошибка при кадрировании", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else -> {
                Toast.makeText(this, "Поймал маслину", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
       // super.onBackPressed()
        presenter.deleteImage()
        zXingScannerView.stopCamera()
        setContentView(R.layout.activity_add)
        init()
        presenter.initRedactCard(presenter.getSavedCard())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_reduct,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (intent.getStringExtra("id") != null) {
            presenter.onRedactClick(intent.getStringExtra("id").toLong())
        } else {
            presenter.onAddClick()
        }
        return super.onOptionsItemSelected(item)
    }

    fun startCamera(imageUri: Uri, filePath: String) {
        imagePath = filePath
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        currentUri = imageUri
        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, CAMERA_REQEST_CODE)
    }
}
