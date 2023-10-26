package ru.kpfu.itis.paramonov.androidtasks.util

import ru.kpfu.itis.paramonov.androidtasks.model.Option
import ru.kpfu.itis.paramonov.androidtasks.model.Question

class QuestionGenerator(number : Int) {
    private val questionNumber = number


    public fun generateQuestions() : List<Question>{
        val questions = ArrayList<Question>()
        for(i : Int in 1..questionNumber) {
            questions.add(generateQuestion())
        }

        return questions
    }

    private fun generateQuestion() : Question {
        val random = kotlin.random.Random
        val answer = random.nextInt(ANS_LOWER_BOUND_INCL, ANS_UPPER_BOUND_EXCL)
        val options = ArrayList<Option>()
        options.add(Option(answer))

        val optionNumber = random.nextInt(OPTION_NUMB_LOWER_BOUND_INCL, OPTION_NUMB_UPPER_BOUND_EXCL)
        for (i : Int in 2..optionNumber) {
            var randomInt = random.nextInt(ANS_LOWER_BOUND_INCL, ANS_UPPER_BOUND_EXCL)
            while (options.contains(Option(randomInt))) {
                randomInt = random.nextInt(ANS_LOWER_BOUND_INCL, ANS_UPPER_BOUND_EXCL)
            }
            options.add(Option(randomInt))
        }

        val text = "Pick a number $answer"

        return Question(text, answer, options.shuffled())
    }

    companion object {
        private const val ANS_LOWER_BOUND_INCL = -100
        private const val ANS_UPPER_BOUND_EXCL = 99

        private const val OPTION_NUMB_LOWER_BOUND_INCL = 5
        private const val OPTION_NUMB_UPPER_BOUND_EXCL = 11
    }
}