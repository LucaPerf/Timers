package it.pdm.timers

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import java.util.*

/**
 * Classe che gestisce il Countdown di un singolo Timer
 */
class CountdownActivity : AppCompatActivity() {
    var txt_time: TextView? = null
    var img_play: ImageView? = null
    var progressbar: ProgressBar? = null
    var timerLengthMin: Int? = null
    var timerLengthSec: Int? = null

    private enum class TimerState{
        STARTED, STOPPED
    }

    private var timerState = TimerState.STARTED
    var timeLeftInMillis: Long = 1 * 60000
    lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countdown)
        init()
        getData()

        val color = Color.parseColor("#FF000000")
        img_play?.setColorFilter(color)

        img_play!!.setOnClickListener{
            if(timerState == TimerState.STARTED){
                pauseTimer()
            }else{
                startTimer()
            }
        }

        updateCountDownText()
        setProgressBarValues()
    }

    fun init(){
        txt_time = findViewById(R.id.tv_countdown)
        progressbar = findViewById(R.id.progress_coundown)
        img_play = findViewById(R.id.iv_play)
    }

    /**
     * Metodo che permette di leggere il valore dei minuti e secondi
     */
    fun getData(){
        timerLengthMin = intent.getIntExtra("MINUTI", 1)
        timerLengthSec = intent.getIntExtra("SECONDI", 1)

        timeLeftInMillis = (timerLengthMin!! * 60 * 1000).toLong() + (timerLengthSec!! * 1000).toLong()
        startTimer()
    }

    private fun setProgressBarValues(){
        progressbar!!.setMax(timeLeftInMillis.toInt() / 1000)
        progressbar!!.setProgress(timeLeftInMillis.toInt() / 1000)
    }

    /**
     * Metodo che permette di far partire il Countdown
     */
    private fun startTimer(){
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000){
            override fun onTick(millisUntilFinished: Long){
                timeLeftInMillis = millisUntilFinished
                img_play!!.setImageResource(R.drawable.ic_pause)
                updateCountDownText()
                progressbar!!.setProgress((millisUntilFinished/1000).toInt())
            }

            override fun onFinish(){
                timerState = TimerState.STOPPED
            }
        }.start()
        timerState = TimerState.STARTED
    }

    /**
     * Metodo che permette di mettere in pausa il Countdown
     */
    private fun pauseTimer(){
        countDownTimer.cancel()
        timerState = TimerState.STOPPED
        img_play!!.setImageResource(R.drawable.ic_play)
    }

    /**
     * Metodo che aggiorna, a ogni secondo, il testo del Countdown
     */
    private fun updateCountDownText(){
        val minutes: Int = (timeLeftInMillis.toInt() / 1000) / 60
        val seconds: Int = (timeLeftInMillis.toInt() / 1000) % 60

        val timeLeftFormatted: String =
            java.lang.String.format(Locale.getDefault(),
            "%02d:%02d", minutes, seconds)
        txt_time!!.setText(timeLeftFormatted)
    }
}