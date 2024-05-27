package ru.kpfu.itis.paramonov.androidtasks.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.ui.fragments.GraphFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.commit {
            add(
                R.id.main_activity_container,
                GraphFragment.newInstance(),
                GraphFragment.GRAPH_FRAGMENT_TAG)
        }
    }
}