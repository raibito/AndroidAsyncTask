package jp.ac.dendai.cdl.raibito.gamecapture.floating

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.view.WindowManager
import android.widget.ImageView
import jp.ac.dendai.cdl.raibito.gamecapture.MainActivity
import jp.ac.dendai.cdl.raibito.gamecapture.R
import jp.ac.dendai.cdl.raibito.gamecapture.capture.CaptureService
import java.util.*
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.util.Log
import jp.ac.dendai.cdl.raibito.gamecapture.ServiceManager
import jp.ac.dendai.cdl.raibito.gamecapture.capture.CaptureActivity


class FloatingService : Service() {
    companion object {
        val ACTION_START = "start"
        val ACTION_STOP = "stop"
    }
    private val notificationId = Random().nextInt()
    private var button: FloatingButton? = null

    override fun onCreate() {
        super.onCreate()
        startNotification()
    }

    private fun startNotification(){
        val activityIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, 0)
        val notification = NotificationCompat.Builder(this, "NC_C_ID")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(FloatingService::class.simpleName)
                .setContentText("Service is running.")
                .build()
        startForeground(notificationId, notification)
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null || intent.action == ACTION_START){
            startOverlay()
        } else {
            stopSelf()
        }
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopOverlay()
    }

    private fun startOverlay(){
        ImageView(this).run {
            val windowManager = getSystemService(Service.WINDOW_SERVICE) as WindowManager
            setImageResource(android.R.drawable.ic_menu_add)
            button = FloatingButton(windowManager, this).apply {
//                startService(Intent(this@FloatingService, CaptureService::class.java))
//                ServiceManager.isServiceRunning = true
                visible = true
                setOnClickListener{
                    startActivity(Intent(this@FloatingService, CaptureActivity::class.java))
                }
            }
        }
    }

    private fun stopOverlay(){
        button?.run{
            visible = false
        }
        button = null
    }

}
