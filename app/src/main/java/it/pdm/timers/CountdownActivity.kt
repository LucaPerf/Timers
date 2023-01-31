package it.pdm.timers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import java.util.*

/**
 * Classe che permette la gestione del countdown di ogni singolo timer
 */
class CountdownActivity : AppCompatActivity() {
    var txt_time: TextView? = null
    var img_play: ImageView? = null
    var img_delete: ImageView? = null
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

        img_play!!.setOnClickListener{
            if(timerState == TimerState.STARTED){
                pauseTimer()
            }else{
                startTimer()
            }
        }

        img_delete?.setOnClickListener {
            val i = Intent(this, TimerActivity2::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(i)
        }

        //start with time and progressbar complete
        updateCountDownText()
        setProgressBarValues()
    }

    fun init(){
        txt_time = findViewById(R.id.tv_countdown)
        progressbar = findViewById(R.id.progress_coundown)
        img_play = findViewById(R.id.iv_play)
        img_delete = findViewById(R.id.tv_delete)
    }

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

    private fun pauseTimer(){
        countDownTimer.cancel()
        timerState = TimerState.STOPPED
        img_play!!.setImageResource(R.drawable.ic_play)

    }

    private fun updateCountDownText(){
        val minutes: Int = (timeLeftInMillis.toInt() / 1000) / 60
        val seconds: Int = (timeLeftInMillis.toInt() / 1000) % 60

        val timeLeftFormatted: String =
            java.lang.String.format(Locale.getDefault(),
            "%02d:%02d", minutes, seconds)
        txt_time!!.setText(timeLeftFormatted)
    }
}