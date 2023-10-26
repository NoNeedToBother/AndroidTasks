package ru.kpfu.itis.paramonov.androidtasks.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.kpfu.itis.paramonov.androidtasks.model.Question
import ru.kpfu.itis.paramonov.androidtasks.ui.QuestionFragment

class QuestionFragmentAdapter(
    private val questions : List<Question>,
    manager : FragmentManager,
    lifecycle : Lifecycle
) : FragmentStateAdapter(manager, lifecycle){

    override fun getItemCount(): Int {
        return questions.size + 2
    }

    override fun createFragment(position: Int): Fragment {
        val realPos = getRealPos(position)
        return QuestionFragment.newInstance(questions[realPos], realPos)
    }

    private fun getRealPos(position: Int) : Int {
        return when (position) {
            0 -> questions.size - 1
            questions.size + 1 -> 0
            else -> position - 1
        }
    }

}