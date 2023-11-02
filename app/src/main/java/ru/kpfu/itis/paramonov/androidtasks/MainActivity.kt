package ru.kpfu.itis.paramonov.androidtasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.kpfu.itis.paramonov.androidtasks.databinding.ActivityMainBinding
import ru.kpfu.itis.paramonov.androidtasks.ui.fragments.StartFragment

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(
            binding.mainActivityContainer.id,
            StartFragment(),
            StartFragment.START_FRAGMENT_TAG
        ).commit()
    }

    public fun goToScreen(
        destination: Fragment,
        tag: String?,
        isAddToBackStack: Boolean,
    ) {
        supportFragmentManager.beginTransaction()
            .replace(binding.mainActivityContainer.id, destination, tag)
            .apply {
                if(isAddToBackStack) {
                    this.addToBackStack(null)
                }
            }.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
