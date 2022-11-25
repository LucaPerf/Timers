package it.pdm.timers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import kotlinx.android.synthetic.main.activity_countdown.*

class CountdownActivity : AppCompatActivity() {
    enum class TimerState{
        Stopped, Paused, Running
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds : Long = 0
    private var timerState = TimerState.Stopped
    private var secondsRemaining = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countdown)

        fab_start.setOnClickListener{v ->
            startTimer()
            timerState = TimerState.Running
            updateButtons()
        }

        fab_pause.setOnClickListener{v ->
            timer.cancel()
            timerState = TimerState.Paused
            updateButtons()
        }

        fab_start.setOnClickListener{v ->
            timer.cancel()
            onTimerFinished()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu):Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return true
    }
}