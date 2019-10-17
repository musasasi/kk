package com.job.ebursary

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.job.ebursary.commoners.PickerBottomSheet
import com.job.ebursary.commoners.PickerInterface
import com.job.ebursary.commoners.captureFromCameraWithPerm
import com.job.ebursary.commoners.pickFromGallery
import kotlinx.android.synthetic.main.activity_data.*
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


class EnterDataActivity : AppCompatActivity(), PickerInterface {

    private val GALLERY_REQUEST_CODE = 165
    private val CAMERA_REQUEST_CODE = 116

    private lateinit var pickerInterface: PickerInterface

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, EnterDataActivity::class.java)
    }

    private var auth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        setSupportActionBar(toolbar)


        val names = auth.currentUser!!.displayName!!


        fullname.editText!!.setText(names)

        pickerInterface = this
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            finish()
        } else {
            submitInfo()

        }
        return super.onOptionsItemSelected(item)
    }

    fun uploadClick(v: View){
        val sheet = PickerBottomSheet(pickerInterface)
        sheet.show(supportFragmentManager, PickerBottomSheet.TAG)

    }

    private fun submitInfo(){
        val name = fullname.editText!!.text.toString()
        val adminNo = admin_number.editText!!.text.toString()
        val id = idnumber.editText!!.text.toString()
        val county = county.editText!!.text.toString()
        val number = phone.editText!!.text.toString()

        val school = school.editText!!.text.toString()
        val address = address.editText!!.text.toString()
        val dateofbirth = dob.editText!!.text.toString()
        val amountRequest = amount.editText!!.text.toString()





        val progressBar = ProgressDialog(this)
        progressBar.setMessage("Please wait...")
        progressBar.setTitle("Applying new bursary")
        progressBar.show()

        val dataMap = hashMapOf(
            "userid" to auth.currentUser!!.uid,
            "name" to name,
            "admission" to adminNo,
            "idnumber" to id,
            "county" to county,
            "phone" to number,
            "school" to school,
            "address" to address,
            "dateofbirth" to dateofbirth,
            "amountrequest" to amountRequest,
            "time" to FieldValue.serverTimestamp()


        )

        firestore.collection("applications")
            .document().set(dataMap)
            .addOnSuccessListener {
                progressBar.dismiss()
                finish()
                Toast.makeText(this@EnterDataActivity,"Success",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this@EnterDataActivity,"Failed to apply",Toast.LENGTH_SHORT).show()
            }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                val selectedImage = data!!.data
                if (selectedImage != null) {

                    //image_got.setImageURI(selectedImage)
                    toUpload()
                }
            }
            CAMERA_REQUEST_CODE -> {

                toUpload()
            }
            else -> {
                Timber.d("Nothing")
            }
        }
    }

    private fun toUpload(){

        val progressBar = ProgressDialog(this)
        progressBar.setMessage("Please wait...")
        progressBar.setTitle("Uploading file")
        progressBar.show()

        val r = Random(6000)

        Handler().postDelayed({
            progressBar.dismiss()
        },r.nextLong())
    }

    //region TAKE PHOTO LOGIC

    override fun picked(isCamera: Boolean) {
        if (isCamera) captureFromCameraWithPerm(this, { createImageFile() }, CAMERA_REQUEST_CODE)
        else pickFromGallery(this, GALLERY_REQUEST_CODE)
    }


    private fun createImageFile(): File? {
        val mediaStorageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "DropExpress Driver")

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val image = File(
            mediaStorageDir.path + File.separator +
                    "IMG_" + timeStamp + ".jpg"
        )

        return image
    }

}
