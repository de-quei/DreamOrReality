package kr.hs.emirim.dreamorreality

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val pwEditText = findViewById<EditText>(R.id.editTextPassword) //비밀번호 입력 칸의 아이디
        val showPasswordCheckBox = findViewById<CheckBox>(R.id.show_pw) //체크박스의 아이디

        //CheckBox의 상태 변경 이벤트 처리
        showPasswordCheckBox.setOnCheckedChangeListener{ buttonView, isChecked ->
            //비밀번호 표시여부에 따라 EditTex의 InputType 설정 변경
            if(isChecked) pwEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else pwEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        //회원가입하기 클릭 이벤트 처리
        val signUpTextView = findViewById<TextView>(R.id.go_signup)
        signUpTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java) //회원가입 창으론 넘어간다.
            startActivity(intent)
        }

        //로그인 버튼 클릭 이벤트 처리
        val signupButton = findViewById<Button>(R.id.signupBtn)
        signupButton.setOnClickListener {
            //필드 값을 가져옴.
            val usernameEditText = findViewById<EditText>(R.id.editTextID)
            val passwordEditText = findViewById<EditText>(R.id.editTextPassword)

            val userId = usernameEditText.text.toString()
            val userPw = passwordEditText.text.toString()

            //필드가 하나라도 비어있을 때 에러 dialog를 발생시킴
            if (userId.isEmpty() || userPw.isEmpty()) showErrorDialog("모든 필드를 입력해주세요.");
        }

    }

    private fun showErrorDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("확인", null)
        val dialog = builder.create()
        dialog.show()
    }
}