package com.github.codingchili.dice30

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

/**
 * Main activity is just used to show the game instructions and
 * then it starts the main game fragment.
 */
class MainActivity : AppCompatActivity() {
    private val savedKey = "saved"
    private var introduced = false

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_game)
        introduced = bundle?.getBoolean(savedKey) ?: false

        if (!introduced) {
            AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(R.string.how_to_play)
                .setPositiveButton(R.string.thanks) { dialog, _ ->
                    introduced = true
                    showGameFragment()
                    dialog.dismiss()
                }.show()
        }
    }

    private fun showGameFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, GameFragment())
            .commit()
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        bundle.putBoolean(savedKey, introduced)
        super.onSaveInstanceState(bundle)
    }
}