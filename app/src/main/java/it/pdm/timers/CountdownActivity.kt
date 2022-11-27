package it.pdm.timers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import java.util.*

class CountdownActivity : AppCompatActivity() {
    var txt_time: TextView? = null
    var txt_breaks: TextView? = null
    var img_play: ImageView? = null
    var progressbar: ProgressBar? = null
    var timerLength: Int? = null
    var breaksCount: Int = 0
    var totalbreaks: Int? = null

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

        //start with time and progressbar complete
        updateCountDownText()
        setProgressBarValues()
    }

    fun init(){
        txt_time = findViewById(R.id.tv_countdown)
        txt_breaks = findViewById(R.id.txt_total_breaks)
        progressbar = findViewById(R.id.progress_coundown)
        img_play = findViewById(R.id.iv_play)
    }

    fun getData(){
        timerLength = intent.getIntExtra("TIME", 1)
        totalbreaks = intent.getIntExtra("BREAKS", 0)

        if(totalbreaks == 0){
            txt_breaks!!.visibility = View.GONE
        }else{
            txt_breaks!!.setText(breaksCount.toString() + "/" + totalbreaks.toString())
        }

        timeLeftInMillis = (timerLength!! * 60 * 1000).toLong()
        startTimer()
    }

    private fun setProgressBarValues(){
        //errore da guardare il setMax
        progressbar!!.setMax(timeLeftInMillis.toInt() / 1000)

        progressbar!!.setProgress(timeLeftInMillis.toInt() / 1000)
    }

    private fun startTimer(){

        //possibile errore
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
        val seconds: Int = (timeLeftInMillis.toInt() / 1000) / 60
        val minutes: Int = (timeLeftInMillis.toInt() / 1000) % 60
        val hours: Int = (timeLeftInMillis.toInt() / 1000) % 60

        val timeLeftFormatted: String =
            java.lang.String.format(Locale.getDefault(),
            "%02d:%02d", hours, minutes, seconds)
        txt_time!!.setText(timeLeftFormatted)
    }
}