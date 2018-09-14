package jp.ac.dendai.cdl.raibito.gamecapture.capture

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.view.View
import jp.ac.dendai.cdl.raibito.gamecapture.R
import java.util.*
import android.provider.MediaStore
import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap.CompressFormat
import java.nio.file.Files.exists
import android.os.Environment.getExternalStorageDirectory
import android.graphics.Bitmap
import android.os.Environment
import jp.ac.dendai.cdl.raibito.gamecapture.ServiceManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat


class CaptureService : Service() {

    companion object {
        private val TAG = CaptureService::class.qualifiedName
        val ACTION_ENABLE_CAPTURE = "enable_capture"
    }

    private val notificationId = Random().nextInt()
    private val capture = Capture(this)

//    override fun onCreate() {
//        super.onCreate()
//        startNotification()
//    }
//
//    private fun startNotification(){
//        val activityIntent = Intent(this, CaptureActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, 0)
//        val notification = NotificationCompat.Builder(this, "NC_C_ID")
//                .setContentIntent(pendingIntent)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(CaptureService::class.simpleName)
//                .setContentText("Service is running.")
//                .build()
//        startForeground(notificationId, notification)
//    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            Log.d("test", "onStartCommand")
            when (intent.action) {
                ACTION_ENABLE_CAPTURE -> doCapture()
            }
        }
        return Service.START_STICKY
    }

    private fun enableCapture() {
        if (CaptureActivity.projection == null) {
            Log.d(TAG, "startActivity(CaptureActivity)")
            val intent = Intent(this, CaptureActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            doCapture()
        }
    }

    fun doCapture() {
        CaptureActivity.projection?.run {
            capture.run(this) {
                capture.stop()
                // save bitmap
                Log.d("Capture", "CaptureSuccess!!")
                stopSelf()
                ServiceManager.isServiceRunning = false
            }
        }
    }

    private fun disableCapture() {
        capture.stop()
        CaptureActivity.projection = null
    }

    override fun onDestroy() {
        super.onDestroy()
        disableCapture()
    }
}
