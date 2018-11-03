package com.mercandalli.android.apps.files.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.Window
import com.mercandalli.android.apps.files.R

class PermissionActivity : AppCompatActivity(), PermissionContract.Screen {

    private val userAction = createUserAction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_permission)
        findViewById<View>(R.id.activity_permission_allow).setOnClickListener {
            userAction.onPermissionAllowClicked()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != REQUEST_CODE) {
            return
        }
        if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            finish()
        } else {
            showSnackbar("This app needs this permission to work", com.google.android.material.snackbar.Snackbar.LENGTH_LONG)
        }
    }

    override fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            PERMISSIONS,
            REQUEST_CODE)
    }

    private fun createUserAction(): PermissionContract.UserAction {
        return PermissionPresenter(
            this
        )
    }

    private fun showSnackbar(@StringRes text: Int, duration: Int) {
        com.google.android.material.snackbar.Snackbar.make(window.decorView, text, duration).show()
    }

    private fun showSnackbar(text: String, duration: Int) {
        com.google.android.material.snackbar.Snackbar.make(window.decorView, text, duration).show()
    }

    companion object {
        private val PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        private const val REQUEST_CODE = 26

        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, PermissionActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(intent)
        }
    }
}
