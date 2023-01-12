package it.pdm.timers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import it.pdm.timers.fragments.AddTimerFragment
import it.pdm.timers.fragments.TimerFragment

class TimerActivity2 : AppCompatActivity(), Communicator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer2)

        val fragmentA = TimerFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentA).commit()
    }

    override fun passData(addItem: Int) {
        val bundle = Bundle()
        bundle.putInt("message", addItem)

        val transaction = this.supportFragmentManager.beginTransaction()
        val fragmentB = AddTimerFragment()
        fragmentB.arguments = bundle

        transaction.replace(R.id.fragment_container, fragmentB)
        transaction.commit()
    }
}