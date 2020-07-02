package com.github.codingchili.dice30

import com.github.codingchili.dice30.model.Die
import com.github.codingchili.dice30.model.Scoring
import org.junit.Assert
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Runs test for the scoring algorithm with test cases read from a separate file.
 * The test file uses the following format
 *
 * dice=<eyes>,<eyes>,<eyes>] alg=<algorithm> score=<expected score>
 *
 * Example;
 * dice=5,2,3,2,2,1,6 alg=FIVE score=15
 * dice=6,3,3,2,2,2,4,2 alg=SIX score=24
 * ... and so on.
 *
 * Scenarios can be commented out by leading with '#'.
 */
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

                val message = "roll $dice expected score $expected with algorithm $algorithm was $score."
                Assert.assertEquals(message, expected, score)
            }
        }
    }
}