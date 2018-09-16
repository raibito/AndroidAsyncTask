package jp.ac.dendai.cdl.raibito.gamecapture.capture

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.*
import android.graphics.Bitmap
import android.os.Environment
import jp.ac.dendai.cdl.raibito.gamecapture.ServiceManager
import java.io.File
import java.io.FileOutputStream
import android.media.MediaScannerConnection

/**
 * CaptureActivityから呼ばれる
 * 正しい場所から呼ばれたらスクショを実行
 * スクショ自体はCapture::runに任せて結果を保存
 *
 * 使ったらserviceを止めないと大変なことに...
 * */
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
        return START_STICKY
    }

    // キャプチャ処理を実行
    fun doCapture() {
        CaptureActivity.projection?.run {
            capture.run(this) {
                saveBitmap(it)
                capture.stop()
                // save bitmap
                Log.d("Capture", "CaptureSuccess!!")
                stopSelf() // スクショの役目が終わったら止めておく
                ServiceManager.isServiceRunning = false
            }
        }
    }

    private fun saveBitmap(bitmap: Bitmap) {
        // キャプチャしたBitmapを外部ストレージに保存
        val extStrageDir = Environment.getExternalStorageDirectory()
        val file = File(extStrageDir.absolutePath + "/" + Environment.DIRECTORY_DCIM, getFileName())
        val outStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        outStream.close()

        // 保存した画像をギャラリーに反映する
        val paths = arrayOf<String>(file.absolutePath)
        val mimeTypes = arrayOf("image/jpg")
        MediaScannerConnection.scanFile(applicationContext, paths, mimeTypes) {
            _, _ -> Log.d("onScanCompleted", "ギャラリーに反映!")
        }
    }

    /**
     * @return: 撮影時の時刻をファイル名化
     * */
    private fun getFileName(): String {
        val c = Calendar.getInstance()
        return (c.get(Calendar.YEAR).toString()
                + "_" + (c.get(Calendar.MONTH) + 1)
                + "_" + c.get(Calendar.DAY_OF_MONTH)
                + "_" + c.get(Calendar.HOUR_OF_DAY)
                + "_" + c.get(Calendar.MINUTE)
                + "_" + c.get(Calendar.SECOND)
                + "_" + c.get(Calendar.MILLISECOND)
                + ".png")
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
