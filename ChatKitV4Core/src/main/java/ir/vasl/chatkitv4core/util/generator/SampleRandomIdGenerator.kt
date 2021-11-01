package ir.vasl.chatkitv4core.util.generator

import java.util.*

class SampleRandomIdGenerator {

    companion object {

        private const val idSize = 10

        private var starterId = 0

        fun getRandomStrId(): String {
            // Descriptive alphabet using three CharRange objects, concatenated
            val alphabet: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

            // Build list from 20 random samples from the alphabet,
            // and convert it to a string using "" as element separator
            return List(idSize) { alphabet.random() }.joinToString("")
        }

        fun getRandomIntId(): Int {
            return Random().nextInt(999999999 - 100000000 + 1) + 100000000
        }

        fun getIncreasingId(): Int {
            return ++starterId
        }

        fun refreshIncreasingId() {
            starterId = 1
        }
    }
}