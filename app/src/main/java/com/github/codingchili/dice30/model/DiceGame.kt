package com.github.codingchili.dice30.model

import java.util.*

/**
 * Implementation of the dice30 game, 10 turns with each turn
 * allowing for a maximum of three rolls.
 */
class DiceGame {
    val player: Player = Player("P1")
    val dice = ArrayList<Die>()
    val scoring = Scoring()

    private val random = Random(System.nanoTime())
    private var roll = 0
    var turn = 0
        private set

    fun roll() {
        roll++
        if (dice.isEmpty()) {
            for (i in 0 until DICE_COUNT) {
                dice.add(Die(random))
            }
        } else {
            dice.replaceAll {
                if (it.stored) {
                    it
                } else {
                    Die(random)
                }
            }
        }
    }

    fun hasRolls(): Boolean {
        return roll < ROLL_PER_TURN
    }

    fun hasTurns(): Boolean {
        return turn < TURNS_PER_GAME
    }

    fun score(): Int {
        player.score = scoring.total()
        roll = 0
        turn++
        dice.clear()
        return player.score
    }

    companion object {
        const val DICE_COUNT = 6
        const val ROLL_PER_TURN = 3
        const val TURNS_PER_GAME = 10
    }
}