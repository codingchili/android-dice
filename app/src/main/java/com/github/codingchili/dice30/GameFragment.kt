package com.github.codingchili.dice30

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.codingchili.dice30.model.DiceGame
import com.github.codingchili.dice30.model.Die
import java.util.*

class GameFragment : Fragment() {
    private var game: DiceGame = DiceGame()

    private lateinit var fragment: View
    private lateinit var roll: Button
    private lateinit var score: Button

    // references to drawables where dice can be displayed.
    private val slots =
        intArrayOf(R.id.dice_1, R.id.dice_2, R.id.dice_3, R.id.dice_4, R.id.dice_5, R.id.dice_6)

    private fun updateGame(animate: Boolean = false) {
        renderGameTurn()
        renderPlayerName()
        renderPlayerScore()
        renderDice(game.dice, animate)
    }

    private fun renderGameTurn() {
        fragment.findViewById<TextView>(R.id.player_turn).text =
            resources.getString(R.string.player_turn, game.turn)
    }

    private fun renderPlayerName() {
        fragment.findViewById<TextView>(R.id.player_name).text = game.player.name
    }

    private fun renderPlayerScore() {
        fragment.findViewById<TextView>(R.id.player_score).text =
            resources.getString(R.string.player_score, game.player.score)
    }

    private fun renderDice(dice: ArrayList<Die>, animate: Boolean = false) {
        for (id in slots.indices) {
            val image = fragment.findViewById<View>(slots[id]) as ImageView

            if (id < dice.size) {
                val drawable = resources.getIdentifier(
                    dice[id].resourceName(),
                    "drawable",
                    context!!.packageName
                )
                if (!dice[id].stored && animate) {
                    image.alpha = 0.0f
                    ObjectAnimator.ofFloat(image, "alpha", 1f).apply {
                        duration = 625
                        start()
                    }
                }
                image.setImageDrawable(resources.getDrawable(drawable, null))
                image.visibility = View.VISIBLE
            } else {
                image.visibility = View.GONE
            }
        }
    }

    private fun rollAction(view: View) {
        game.roll()
        updateGame(true)

        roll.visibility = if (game.hasRolls()) View.VISIBLE else View.GONE
        score.visibility = View.VISIBLE
    }

    fun score() {
        game.score()
        updateGame()

        score.visibility = View.GONE
        roll.visibility = if (game.hasRolls() && game.hasTurns()) View.VISIBLE else View.GONE

        if (!game.hasTurns()) {
            fragmentManager!!.beginTransaction()
                .replace(R.id.container, EndFragment(game))
                .commit()
        }
    }

    private fun scoreAction(view: View) {
        fragmentManager!!.beginTransaction()
            .add(R.id.container, ScoringFragment(game, this))
            .commit()
    }

    private fun saveAction(view: View) {
        if (game.dice.isNotEmpty()) {
            game.dice[slots.indexOf(view.id)].stored = true
        }
        renderDice(game.dice)

        // all dice are stored, force score action.
        if (game.dice.all { it.stored }) {
            scoreAction(view)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragment = inflater.inflate(R.layout.fragment_game, container, false)

        roll = fragment.findViewById(R.id.dice_roll)
        roll.setOnClickListener(this::rollAction)

        score = fragment.findViewById(R.id.dice_score)
        score.setOnClickListener(this::scoreAction)

        slots.forEach { fragment.findViewById<ImageView>(it).setOnClickListener(this::saveAction) }

        updateGame()
        return fragment
    }

    init {
        retainInstance = true
    }
}