package jp.ac.dendai.cdl.raibito.dynamicbottomnavtest

import android.content.ClipData
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem

const val ITEMID_ZERO = 0
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ボトムナビゲーションを動的に生成することはできなくはない
        // 推奨はしないどころかボトムナビゲーションはそういうものではないのであらゆる方面から叩かれるかも
        val bnv = findViewById<BottomNavigationView>(R.id.bottom_nav)
        // 実際は add(groupID, itemID, order, title)を入れる
        // itemIDは追加時に自動で割り振ってくれないため、addしただけだと全てのitemIDは0になる
        bnv.menu.add("A")
        bnv.menu.add("B")
        bnv.setOnNavigationItemReselectedListener {
            when(it.itemId){
                ITEMID_ZERO -> { // addしただけだとitemIDは0固定
                    if (bnv.menu.size() < 5) {  // 6個以上は増やせない
                        bnv.menu.add("C")
                    }
                    //  bnv.menu.removeItem(itemID)
                    //  でアイテムを消すことができるが、消した後、itemIDが動的に変更されることはない
                    //  対処法: 一回 bnv.menu.clear()で全て消してからitemID振り直してaddし直す
                    // 　　　   つまりボトムナビゲーションの再描画(再配置)をする
                }
            }
        }

    }
}
