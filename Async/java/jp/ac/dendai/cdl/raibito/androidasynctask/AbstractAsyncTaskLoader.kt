package jp.ac.dendai.cdl.raibito.androidasynctask


import android.content.Context
import android.support.v4.content.AsyncTaskLoader

/**
 * Created by raibito on 2018/06/12.
 * AsyncTaskLoaderの抽象クラス
 * AsyncTaskLoaderのimportはv4にする <- えらい目にあった
 *
 * 下記を参考にした。正しいAsyncTaskLoaderの使い方
 * https://qiita.com/akkuma/items/a1252498e95c1f68316c
 */
abstract class AbstractAsyncTaskLoader<D>(context: Context) : AsyncTaskLoader<D>(context) {

    private var result : D? = null
    private var isStart : Boolean = false

    override fun onStartLoading() {
        if (result != null){
            deliverResult(result)
            return
        }
        if (!isStart || takeContentChanged()) { // forceLoadが複数回呼ばれないようにする
            forceLoad()
        }
    }

    override fun onForceLoad() {
        super.onForceLoad()
        isStart = true
    }

    override fun deliverResult(data: D?) {
        result = data
        super.deliverResult(data)
    }

}