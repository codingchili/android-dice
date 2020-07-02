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

class ScoringFragment(
    private val game: DiceGame,
    private val fragment: GameFragment
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scoring, container, false)
        val list = view.findViewById<ListView>(R.id.scoring_list)
        val adapter =
            object : ArrayAdapter<Scoring.Algorithm>(context!!, R.layout.item_two_columns) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val option =
                        convertView ?: inflater.inflate(R.layout.item_two_columns, parent, false)

                    val item = getItem(position)!!

                    option.findViewById<TextView>(R.id.scoring_algorithm).text = item.name
                    option.findViewById<TextView>(R.id.scoring_points).text =
                        game.scoring.score(game.dice, item).toString()

                    option.setOnClickListener {
                        game.scoring.commit(game.dice, item)

                        fragmentManager!!
                            .beginTransaction()
                            .remove(this@ScoringFragment)
                            .commit()

                        fragment.score()
                    }
                    return option
                }
            }
        adapter.clear()
        adapter.addAll(game.scoring.available())
        list.adapter = adapter
        return view
    }

    init {
        retainInstance = true
    }
}