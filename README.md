# Dice30

Dice game for Android written in Kotlin, fragments, animations and sneaky algorithms for score calculation.

### Building

Regular Android build, API 29.

### Player instructions

Thirty is a game of dice, not unlike Yatzy where one throws six dice in turns.
Each turn starts with the player rolling the dice, the player may then choose
any number of dice to save and re-roll the others. The player may not re-roll
a die that has been saved in previous turns and the maximum number of re-rolls
are limited to 2.

The score is calculated by one of the following methods, low, four, five, six, seven,
eight, nine, eleven or twelve. When using "low" each die with three or less eyes count
as one point per eye. When using the other methods combinations of dice that match
up to the chosen value count. For each combination possible, with no die reuse -
the number of combinations is multiplied with the method chosen. For example,
        when using "four", three possible combinations yields a score of twelve.

Each game of thirty is made up of 10 rounds, with a possibility of 30 rolls in total -
hence the name.
