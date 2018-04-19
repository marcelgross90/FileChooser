package rocks.marcelgross.filechooser

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import java.io.FileOutputStream
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()

        val button = findViewById<Button>(R.id.button2)
        button.setOnClickListener { performFileSearch() }

        val button2 = findViewById<Button>(R.id.button3)
        button2.setOnClickListener { createFile() }
    }

    private fun performFileSearch() {

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"

        startActivityForResult(intent, READ_REQUEST_CODE)
    }

    private fun createFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)

        intent.addCategory(Intent.CATEGORY_OPENABLE)

        intent.type = "application/json"
        intent.putExtra(Intent.EXTRA_TITLE, "test.json")
        startActivityForResult(intent, WRITE_REQUEST_CODE)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int,
                                         resultData: Intent?) {

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                READ_REQUEST_CODE -> {
                    if (resultData != null) {
                        val uri = resultData.data

                        val intent = Intent(this@MainActivity, DisplayActivity::class.java)
                        intent.putExtra("uri", uri.toString())

                        startActivity(intent)
                    }
                }

                WRITE_REQUEST_CODE -> {
                    if (resultData != null) {
                        val uri = resultData.data

                        write(uri)

                        val intent = Intent(this@MainActivity, DisplayActivity::class.java)
                        intent.putExtra("uri", uri.toString())

                    }
                }
            }
        }

    }

    private fun write(uri: Uri) {
        val pfd = contentResolver.openFileDescriptor(uri, "w")
        val fileOutputStream = FileOutputStream(pfd.fileDescriptor)
        fileOutputStream.write(("{msg:\"Hallo Welt\"}").toByteArray())
        fileOutputStream.close()
        pfd.close()
    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                        this,
                        "Permissions missing",
                        Toast.LENGTH_SHORT)
                        .show()
                finish()
            }
        }
    }

    private fun checkPermissions() {
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val listPermissionsNeeded = ArrayList<String>()
        for (p in permissions) {
            val result = ContextCompat.checkSelfPermission(this, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), 1)
        }
    }

    companion object {
        private const val READ_REQUEST_CODE = 42
        private const val WRITE_REQUEST_CODE = 43
    }
}
