package it.pdm.timers

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

/**
 * Classe che gestisce il suono di avviso quando è scaduto il tempo totale dell'Allenamento
 */
class BackgroundAlarmFinishService: Service() {
    private lateinit var player: MediaPlayer
    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer.create(this, R.raw.long_alarm_tone)
        player.isLooping = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        player.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}