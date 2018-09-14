package jp.ac.dendai.cdl.raibito.gamecapture.capture

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import jp.ac.dendai.cdl.raibito.gamecapture.R
import jp.ac.dendai.cdl.raibito.gamecapture.ServiceManager

class CaptureActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_CAPTURE = 1
        var projection: MediaProjection? = null
    }
    private lateinit var mediaProjectionManager: MediaProjectionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaProjectionManager = getSystemService(Service.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                projection = mediaProjectionManager.getMediaProjection(resultCode, data)
                val intent = Intent(this, CaptureService::class.java)
                        .setAction(CaptureService.ACTION_ENABLE_CAPTURE)
                //Thread.sleep(100) // 初回の許可確認が出る場合 今回は時短のためなし
                startService(intent)
                ServiceManager.isServiceRunning = true
            } else {
                projection = null
                Toast.makeText(this, R.string.capture_error, Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }
}
