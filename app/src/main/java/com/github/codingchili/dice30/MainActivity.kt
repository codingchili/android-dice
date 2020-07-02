package com.github.codingchili.dice30

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        if (savedInstanceState == null) {
            // only appear once
            AlertDialog.Builder(this)
                .setMessage(R.string.how_to_play)
                .setPositiveButton(R.string.thanks) { dialog, _ ->
                    supportFragmentManager.beginTransaction()
                        .add(R.id.container, GameFragment())
                        .commit()

                    dialog.dismiss()
                }.show()
        }
    }
}