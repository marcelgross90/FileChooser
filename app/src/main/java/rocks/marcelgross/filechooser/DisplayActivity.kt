package rocks.marcelgross.filechooser

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View.VISIBLE
import android.view.View.GONE
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.io.FileInputStream
import java.io.IOException


class DisplayActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)

        val intent = intent
        val uri = intent.getStringExtra("uri")

        Log.d("mgr", "reached display activity")
        textView = findViewById(R.id.textView2)
        progressBar = findViewById(R.id.progressBar)

  //      textView.post{displayText(Uri.parse(uri))}
        displayText(Uri.parse(uri))
    }

    private fun displayText(uri: Uri) {
        launch {
            val readLines = readTextFromUri(uri)
            Log.d("mgr", "Done reading $readLines")
            launch(UI) {
                textView.text = readLines
                textView.visibility = VISIBLE

                progressBar.visibility = GONE
                Log.d("mgr", "UI launch finish")
            }
        }
        Log.d("mgr", "finish displayText")
    }

    @Throws(IOException::class)
    private fun readTextFromUri(uri: Uri): String {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor = parcelFileDescriptor.fileDescriptor
        val fis = FileInputStream(fileDescriptor)

        val stringBuilder = StringBuilder()
        val line = fis.bufferedReader().readLines()

        for (s in line) {
            stringBuilder.append(s)
        }

        parcelFileDescriptor.close()
        fis.close()

        return stringBuilder.toString()
    }
}
