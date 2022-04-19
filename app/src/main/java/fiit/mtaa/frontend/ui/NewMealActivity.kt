package fiit.mtaa.frontend.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import fiit.mtaa.frontend.R
import fiit.mtaa.frontend.data.model.ImageFilePath
import fiit.mtaa.frontend.data.model.Meal
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.lang.Integer.parseInt
import java.util.*

//private const val FILES_MANAGEMENT_PERMISSION_CODE = 1

class NewMealActivity() : AppCompatActivity() {

    val pickImage = 100
    var imageUri: Uri? = null
    var realPath = ""
    lateinit var imageBitmap: Bitmap
    var encodedBytes: ByteArray? = null
    var isFileChosen = findViewById<TextView>(R.id.tv_file_path)

    @OptIn(InternalAPI::class)
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_meal)

        val btmConfirm = findViewById<Button>(R.id.confirm_btn)
        val btnDiscard = findViewById<Button>(R.id.discard_btn)
        val btnPhoto = findViewById<Button>(R.id.btn_choose_file)

        var name = findViewById<EditText>(R.id.enter_name)
        var description = findViewById<EditText>(R.id.enter_description)
        var price = findViewById<EditText>(R.id.enter_price)

        var meal: Meal? = null
        if (intent.hasExtra("Meal")) {
            meal = intent.getSerializableExtra("Meal") as Meal
        }

        if (meal != null) {
            name.setText(meal.name)
            description.setText(meal.description)
            price.setText(meal.price.toString())
        }

        btmConfirm.setOnClickListener {
            var nameText = name.text.toString()
            var descriptionText = description.text.toString()
            var priceText = price.text.toString()

            if(nameText.isEmpty() || descriptionText.isEmpty() || priceText.isEmpty()){
                Toast.makeText(this@NewMealActivity, "Fields can not be empty!", Toast.LENGTH_LONG).show()
            }

            else {
                runBlocking {
                    launch {

                        try {
                            if (meal == null) {

                                val response: HttpResponse = client.post("$server_ip/addMeal") {
                                    body = MultiPartFormDataContent(
                                        formData {
                                            if (encodedBytes != null) {
                                                append("file", encodedBytes!!)
                                            }
                                            append("name", nameText)
                                            append("description", descriptionText)
                                            append("price", parseInt(priceText))
                                        }
                                    )
                                    header("Authorization", token)
                                }
                                Toast.makeText(
                                    this@NewMealActivity,
                                    "Meal added",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                val response: HttpResponse =
                                    client.put("$server_ip/editMeal/${meal.id}") {
                                        body = MultiPartFormDataContent(
                                            formData {
                                                if (encodedBytes != null) {
                                                    append("file", encodedBytes!!)
                                                }
                                                append("name", nameText)
                                                append("description", descriptionText)
                                                append("price", parseInt(priceText))
                                            }
                                        )
                                        header("Authorization", token)
                                    }
                                Toast.makeText(
                                    this@NewMealActivity,
                                    "Meal edited",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            this@NewMealActivity.onBackPressed()
                            return@launch

                            /*val response: HttpResponse = client.submitFormWithBinaryData(
                            url = "$server_ip/addMeal",
                            formData = formData {
                                append("file", File(realPath).readBytes(), Headers.build {
                                    append(HttpHeaders.ContentType, "image/jpg")
                                })
                                append("name", "abc")
                                append("description", "abc")
                                append("price", 1)
                            }
                        )*/

                            //this@NewMealActivity.onBackPressed()
                        } catch (e: Exception) {
                            println(e.localizedMessage)
                            ErrorOutput(this@NewMealActivity, e)
                            when (e) {
                                is ClientRequestException -> {
                                    Toast.makeText(
                                        this@NewMealActivity,
                                        "Check input data",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }

        btnDiscard.setOnClickListener {
            this@NewMealActivity.onBackPressed()
        }

        btnPhoto.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImage && resultCode == RESULT_OK && data != null && data.getData() != null) {
            val uri: Uri = data.getData()!!
            realPath = ImageFilePath.getPath(this@NewMealActivity, data.getData())
            //                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
            //Log.i(TAG, "onActivityResult: file path : $realPath")
            try {
                val stream = ByteArrayOutputStream()
                imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream)
                encodedBytes = stream.toByteArray()
                //encodedBytes = Base64.getEncoder().encode(imageBitmap.toString().toByteArray())
                //val bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
        }
    }

//    @Throws(IOException::class)
//    fun readUri(context: Context, uri: Uri?): ByteArray? {
//        val pdf: ParcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r")!!
//        assert(pdf.statSize <= Int.MAX_VALUE)
//        val data = ByteArray(pdf.statSize.toInt())
//        val fd: FileDescriptor = pdf.fileDescriptor
//        val fileStream = FileInputStream(fd)
//        fileStream.read(data)
//        return data
//    }

    /*private fun requestFilesPermission(dialogShown: Boolean = false) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) &&
            !dialogShown) {
            showPermissionRationaleDialog()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            ), FILES_MANAGEMENT_PERMISSION_CODE
            )
        }
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Files Management Permission Required")
            .setMessage("This app need the files management access to function")
            .setPositiveButton("Grant") { dialog, _ ->
                dialog.dismiss()
                requestFilesPermission(true)
            }
            .setNegativeButton("Deny") { dialog, _ ->
                dialog.dismiss()
                onFilesPermissionDenied()
            }
            .show()
    }

    private fun onFilesPermissionDenied() {
        Toast.makeText(this, "Files Management Permission Denied", Toast.LENGTH_LONG).show()
    }*/

}