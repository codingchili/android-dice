package com.github.codingchili.dice30.model

import java.util.*
import kotlin.math.max

/**
 * Implements scoring for each dice roll and keeps a history
 * of each scoring algorithm with its score.
 */
class Scoring {
    val history = ArrayList<Pair<Algorithm, Int>>()

    /**
     * Records scoring for the given dice using the given algorithm.
     * The algorithm can only be used once during a game of dice.
     *
     * @param dice the set of dice the user rolled, used to calculate the score.
     * @param algorithm indicates how score is calculated for the given dice.
     */
    fun commit(dice: ArrayList<Die>, algorithm: Algorithm) {
        history.add(Pair(algorithm, score(dice, algorithm)))
    }

    /**
     * @return the total score as recorded in the history.
     */
    fun total(): Int {
        return history.stream()
            .map { it.second }
            .reduce(0, Integer::sum)
    }

    /**
     * @return a list of scoring algorithms that can still be used for this game.
     * this list includes all possible scoring algorithms with the ones recorded in
     * history removed.
     */
    fun available(): List<Algorithm> {
        val available = Algorithm.values().toMutableList()
        available.removeAll { algorithm ->
            history.stream().filter { entry -> entry.first == algorithm }
                .findAny().isPresent
        }
        return available
    }

    /**
     * Scores the given roll according to the given algorithm. This does not
     * record the score in the history and can be used to check the score,
     * before calling commit.
     *
     * @param dice a roll to calculate the score of.
     * @param algorithm the algorithm to be used when calculating the score.
     */
    fun score(dice: List<Die>, algorithm: Algorithm): Int {
        return if (algorithm == Algorithm.LOW) {
            dice.map { it.eyes.value() }.filter { it <= 3 }.sum()
        } else {
            permute(0, dice, null, algorithm)
        }
    }

    /**
     * The scoring algorithm does the following,
     *
     * 1) generate all possible permutations of the die set using recursion
     *    with depth first and backtracking. Reorder the dice, to make sure
     *    to get all permutations, not just combinations.
     *
     * 2) on each iteration, select one of each of the previously unselected dice and create
     *    new branches to test.
     * 3) unmark the last chosen die after each of the currently possible branches are tested.
     * 4) calculate the score of the permutation, keep if higher than the current maximum.
     *
     * The permutations (order) of the dice represents dice combinations.
     * Call stack will never be deeper than the number of dice.
     */
    private fun permute(score: Int, dice: List<Die>, chosen: Die?, algorithm: Algorithm): Int {
        val sorted = dice.sortedBy { it.countOrder }
        sorted.filter { !it.counted }.apply {
            var high = score
            forEach { high = max(permute(score, sorted, it.count(), algorithm), high) }
            chosen?.counted = false
            return max(high, selected(sorted, algorithm))
        }
    }

    /**
     * Calculates the score of the selected dice permutation. The order of the dice
     * represents the matching of the groups, as an example
     *
     * Algorithm is; 8
     * Permutation is; 4, 2, 2, 6, 3, 1 (supports any number of dice)
     *
     * The scoring algorithm creates the following pairs, from left to right
     * [4], [2, 2]
     *
     * The rest of the dice are ignored, as the die after 4,2,2 is a 6 and cannot be used
     * with itself or the next N dice to aggregate the sum of the algorithm target.
     *
     * The scoring algorithm goes from left to right, creating a combination of dice groups
     * that matches the sum. Whenever the algorithm encounters a die which value
     * when added to the running total exceeds the algorithm target - all other dice are
     * ignored and won't be added to a combination.
     *
     * So for example, if the permutation produced the following instead
     *
     * Algorithm is; 8
     * Permutation is; 4, 2, 2, 3, 1, 6 (same as before, different order)
     *
     * The following combinations are created
     *
     * [4], [2, 2], [3, 1]
     *
     * And the last 6 is ignored, this combination yields a higher score.
     */
    private fun selected(dice: List<Die>, algorithm: Algorithm): Int {
        var current = 0
        var times = 0
        dice.forEach {
            current += it.eyes.value()

            if (current == algorithm.sum) {
                current = 0
                times++
            }
        }
        return times * algorithm.sum
    }

    /**
     * The different algorithms which can be used to score a roll.
     */
    enum class Algorithm(val sum: Int = -1) {
        LOW,
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        ELEVEN(11),
        TWELVE(12);
    }
}