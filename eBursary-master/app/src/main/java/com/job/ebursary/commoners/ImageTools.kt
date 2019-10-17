package com.job.ebursary.commoners

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.job.ebursary.BuildConfig
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import timber.log.Timber
import java.io.File
import java.io.IOException

fun captureFromCameraWithPerm(ctx: Activity, createImageFile: () -> File?, requestCode: Int) {
    Dexter.withActivity(ctx)
        .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                if (report!!.isAnyPermissionPermanentlyDenied) {
                    // navigate user to app settings
                    DialogUtils.showSettingsDialog(ctx)
                } else if (report.areAllPermissionsGranted()) {
                    //permissions granted
                    captureFromCamera(ctx, createImageFile, requestCode)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                token: PermissionToken?
            ) {
                token?.continuePermissionRequest()
            }

        }).check()
}

fun pickFromGallery(ctx: Activity, requestCode: Int) {
    //Create an Intent with action as ACTION_PICK
    val intent = Intent(Intent.ACTION_PICK)
    // Sets the type as image/*. This ensures only components of type image are selected
    intent.type = "image/*"
    //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
    val mimeTypes = arrayOf("image/jpeg", "image/png")
    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
    // Launching the Intent
    ctx.startActivityForResult(intent, requestCode)
}

private fun captureFromCamera(ctx: Activity, createImageFile: () -> File?, requestCode: Int) {
    try {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val file = FileProvider.getUriForFile(
            ctx,
            BuildConfig.APPLICATION_ID + ".provider",
            createImageFile.invoke()!!
        )

        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file)
        if (intent.resolveActivity(ctx.packageManager) != null) {
            ctx.startActivityForResult(intent, requestCode)
        }

    } catch (ex: IOException) {
        Timber.e(ex)
        Toast.makeText(ctx,"Camera Failed. Try again",Toast.LENGTH_SHORT).show()

    }
}