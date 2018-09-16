package jp.ac.dendai.cdl.raibito.gamecapture.floating

import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import jp.ac.dendai.cdl.raibito.gamecapture.ServiceManager
import jp.ac.dendai.cdl.raibito.gamecapture.capture.CaptureService

/**
 * Created by raibito on 2018/09/11.
 */
class FloatingButton(val windowManager: WindowManager, val view: View) {
    companion object {
        private val TAG = FloatingButton::class.qualifiedName
    }

    private val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    } else {
        WindowManager.LayoutParams.TYPE_PHONE
    }

    private val params =
        WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)
                .apply {
                    gravity = Gravity.TOP or Gravity.START
                    x = 100
                    y = 100
                }

    var visible: Boolean = false
        set(value) {
            if (field != value){
                field = value
                if (value){
                    windowManager.addView(view, params)
                }else{
                    windowManager.removeView(view)
                }
            }
        }

    private var initial: Position? = null

    init {
        view.setOnTouchListener {
            view, e ->
            when (e.action) {
                MotionEvent.ACTION_DOWN -> {
                    initial = params.position - e.position
                }
                MotionEvent.ACTION_MOVE -> {
                    initial?.let {
                        params.position = it + e.position
                        windowManager.updateViewLayout(view, params)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    initial = null
                }
            }
            false
        }
    }

    private val MotionEvent.position: Position
        get() = Position(rawX, rawY)

    private var WindowManager.LayoutParams.position: Position
        get() = Position(x.toFloat(), y.toFloat())
        set(value) {
            x = value.x
            y = value.y
        }

    private data class Position(val fx: Float, val fy: Float) {

        val x: Int
            get() = fx.toInt()

        val y: Int
            get() = fy.toInt()

        operator fun plus(p: Position) = Position(fx + p.fx, fy + p.fy)
        operator fun minus(p: Position) = Position(fx - p.fx, fy - p.fy)
    }

}