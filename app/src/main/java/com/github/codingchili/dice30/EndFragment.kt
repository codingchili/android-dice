package com.github.codingchili.dice30

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.codingchili.dice30.model.DiceGame
import com.github.codingchili.dice30.model.Scoring

class EndFragment(private val game: DiceGame) : Fragment() {

    private val newGameClick =
        View.OnClickListener {
            fragmentManager!!.beginTransaction()
                .replace(R.id.container, GameFragment())
                .commit()
        }
    var quitClick = View.OnClickListener { activity!!.finish() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_end, container, false)
        val list = view.findViewById<ListView>(R.id.scoring_history);

        view.findViewById<TextView>(R.id.winner_name).text = game.player.name
        view.findViewById<TextView>(R.id.winner_score).text =
            resources.getString(R.string.player_score, game.player.score)
        view.findViewById<TextView>(R.id.winner_turn).text =
            resources.getString(R.string.player_turn, game.turn)

        val adapter =
            object :
                ArrayAdapter<Pair<Scoring.Algorithm, Int>>(context!!, R.layout.item_two_columns) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val option =
                        convertView ?: inflater.inflate(
                            R.layout.item_two_columns,
                            parent,
                            false
                        )

                    val item = getItem(position)!!
                    option.findViewById<TextView>(R.id.scoring_algorithm).text = item.first.name
                    option.findViewById<TextView>(R.id.scoring_points).text =
                        item.second.toString()

                    return option
                }
            }
        adapter.addAll(game.scoring.history)
        list.adapter = adapter

        view.findViewById<View>(R.id.button_newgame).setOnClickListener(newGameClick)
        view.findViewById<View>(R.id.button_quit).setOnClickListener(quitClick)
        return view
    }

    init {
        retainInstance = true
    }
}