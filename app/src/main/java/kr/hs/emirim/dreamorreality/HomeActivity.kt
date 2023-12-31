package kr.hs.emirim.dreamorreality

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import org.json.JSONObject

class HomeActivity : ComponentActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val welcomeTextView = findViewById<TextView>(R.id.welcomeTextView)

        // Intent에서 사용자 이름을 가져와서 TextView에 설정
        val username = intent.getStringExtra("username")
        welcomeTextView.text = "$username 님"

        val home = findViewById<ImageButton>(R.id.home_btn)
        val study = findViewById<ImageButton>(R.id.study_btn)
        val chat = findViewById<ImageButton>(R.id.chat_btn)
        val daily = findViewById<ImageButton>(R.id.daily_btn)
        val mypage = findViewById<ImageButton>(R.id.mypage_btn)
        home.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        study.setOnClickListener{
            val intent = Intent(this, StudyActivity::class.java)
            startActivity(intent)
        }

    }
}