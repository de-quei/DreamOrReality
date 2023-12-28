package kr.hs.emirim.dreamorreality

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import org.json.JSONObject

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val welcomeTextView = findViewById<TextView>(R.id.welcomeTextView)

        // Intent에서 사용자 이름을 가져와서 TextView에 설정
        val username = intent.getStringExtra("username")
        welcomeTextView.text = "$username 님"
    }
}