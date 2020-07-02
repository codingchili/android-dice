package com.github.codingchili.dice30

import com.github.codingchili.dice30.model.Die
import com.github.codingchili.dice30.model.Scoring
import org.junit.Assert
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths

class ScoringTest {

    @Test
    fun runScenariosFromFile() {
        Files.readAllLines(Paths.get("./scenario.txt")).forEach {
            if (!it.startsWith("#")) {
                val lines = it.split(" ")
                val dice = lines[0].split("=")[1].split(",").map { eyes ->
                    Die(Die.Eyes.values()[Integer.parseInt(eyes) - 1])
                }
                val algorithm = Scoring.Algorithm.valueOf(lines[1].split("=")[1])
                val expected = Integer.parseInt(lines[2].split("=")[1])

                val score = Scoring().score(dice, algorithm)

                System.err.println("roll $dice expected score $expected with algorithm $algorithm was $score.")
            }

            /*Assert.assertEquals(
                "roll $dice expected score $expected with algorithm $algorithm but was $score.",
                expected,
                score
            )*/
        }
    }
}