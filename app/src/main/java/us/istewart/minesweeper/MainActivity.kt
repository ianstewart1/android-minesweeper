package us.istewart.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import us.istewart.minesweeper.view.MinesweeperView

class MainActivity : AppCompatActivity() {

    var flagOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toggleBtn.setOnCheckedChangeListener { _, isChecked ->
            flagOn = isChecked
        }

        btnReset.setOnClickListener {
            mineView.resetGame()
        }
    }

    fun toastMessage(msg: String) {
        Snackbar.make(layoutMain, msg, Snackbar.LENGTH_LONG).show()
    }
}
