package kr.hs.emirim.dreamorreality

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kr.hs.emirim.dreamorreality.ui.theme.DreamOrRealityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val pwEditText = findViewById<EditText>(R.id.editTextPassword) //비밀번호 입력 칸의 아이디
        val showPasswordCheckBox = findViewById<CheckBox>(R.id.show_pw) //체크박스의 아이디

        //CheckBox의 상태 변경 이벤트 처리
        showPasswordCheckBox.setOnCheckedChangeListener{ buttonView, isChecked ->
            //비밀번호 표시 여부에 따라 EditTex의 InputType 설정 변경
            if(isChecked) pwEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else pwEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        //회원가입하기 클릭 이벤트 처리
        val signUpTextView = findViewById<TextView>(R.id.go_signup)
        signUpTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}