package com.github.codingchili.dice30.model

import java.util.*

var next = 0

class Die {
    var stored = false
    var countOrder = 0
    var counted = false
        set(counted) {
            countOrder = next++
            field = counted
        }
    var eyes: Eyes = Eyes.ONE
        private set

    constructor(value: Eyes) {
        eyes = value
    }

    constructor(random: Random) {
        roll(random)
    }

    private val value: Int
        get() = eyes.value()

    private fun roll(random: Random) {
        eyes = Eyes.values()[random.nextInt(Eyes.values().size)]
    }

    override fun hashCode(): Int {
        return value
    }

    override fun equals(other: Any?): Boolean {
        var equals = false
        if (other is Die) {
            if (other.eyes == eyes) equals = true
        }
        return equals
    }

    override fun toString(): String {
        return StringBuilder().append(value).toString()
    }

    fun resourceName(): String {
        val resource = eyes.toString().toLowerCase(Locale.ROOT)
        return if (this.stored) "${resource}_stored" else resource;
    }

    fun count(): Die {
        counted = true
        return this
    }

    enum class Eyes {
        ONE, TWO, THREE, FOUR, FIVE, SIX;

        fun value(): Int {
            return this.ordinal + 1
        }
    }
}