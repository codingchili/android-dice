package com.github.codingchili.dice30.model

import java.util.*
import kotlin.math.max

class Scoring {
    val history = ArrayList<Pair<Algorithm, Int>>()

    fun commit(dice: ArrayList<Die>, algorithm: Algorithm) {
        history.add(Pair(algorithm, score(dice, algorithm)))
    }

    fun total(): Int {
        return history.stream()
            .map { it.second }
            .reduce(0, Integer::sum)
    }

    fun available(): List<Algorithm> {
        val available = Algorithm.values().toMutableList()
        available.removeAll { algorithm ->
            history.stream().filter { entry -> entry.first == algorithm }
                .findAny().isPresent
        }
        return available
    }

    fun score(dice: List<Die>, algorithm: Algorithm): Int {
        return if (algorithm == Algorithm.LOW) {
            dice.map { it.eyes.value() }.filter { it <= 3 }.sum()
        } else {
            permute(0, dice, null, algorithm)
        }
    }

    private fun permute(score: Int, dice: List<Die>, chosen: Die?, algorithm: Algorithm): Int {
        val sorted = dice.sortedBy { it.countOrder }
        sorted.filter { !it.counted }.apply {
            var high = score
            forEach { high = max(permute(score, sorted, it.count(), algorithm), high) }
            chosen?.counted = false
            return max(high, selected(sorted, algorithm))
        }
    }

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