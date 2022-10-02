package it.pdm.timers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import it.pdm.timers.fragments.HomeFragment
import it.pdm.timers.fragments.TimerFragment
import it.pdm.timers.fragments.TimerSalvatiFragment

class TimerActivity : AppCompatActivity() {
    private val timerFragment = TimerFragment()
    private val timerSalvatiFragment = TimerSalvatiFragment()
    private val homeFragment = HomeFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        replaceFragment(timerFragment)

        val btn_navigation = findViewById<TextView>(R.id.bottom_navigation) as BottomNavigationView

        btn_navigation.setOnItemSelectedListener{
            when(it.itemId){
                R.id.timer -> replaceFragment(timerFragment)
                R.id.timer_salvati -> replaceFragment(timerSalvatiFragment)
                R.id.home -> replaceFragment(homeFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransition = fragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.fragment_container, fragment)
        fragmentTransition.commit()
    }
}