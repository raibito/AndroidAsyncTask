package jp.ac.dendai.cdl.raibito.dynamicbottomnavtest

import android.content.ClipData
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bnv = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bnv.menu.add("A")
        bnv.menu.add("B")
        bnv.setOnNavigationItemReselectedListener {
            when(it.itemId){
                0 -> bnv.menu.add("C")
            }
        }
    }
}
