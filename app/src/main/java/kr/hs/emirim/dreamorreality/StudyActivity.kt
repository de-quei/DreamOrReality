package kr.hs.emirim.dreamorreality

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StudyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study)

        val fab = findViewById<ExtendedFloatingActionButton>(R.id.floatingBtn)
        fab.setOnClickListener{
            val intent = Intent(this, StudyWriteActivity::class.java)
            startActivity(intent)
        }
    }
}