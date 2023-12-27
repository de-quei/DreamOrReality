package kr.hs.emirim.dreamorreality

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val nextButton = findViewById<Button>(R.id.go_nextBtn)

        //다음 Button의 이벤트 리스너
        nextButton.setOnClickListener {
            //필요 필드 값을 가져옴.
            val nameEditText = findViewById<EditText>(R.id.signUpEditTextName)
            val idEditText = findViewById<EditText>(R.id.signUpEditTextID)
            val pwEditText = findViewById<EditText>(R.id.signUpEditTextPW)
            val checkPwEditText = findViewById<EditText>(R.id.checkEditTextPW)

            val signupNAME = nameEditText.text.toString()
            val signupID = idEditText.text.toString()
            val signupPW = pwEditText.text.toString()
            val signupCheckPW = checkPwEditText.text.toString()

            //필드가 하나라도 비었다면 Error Dialog 발생
            if(signupNAME.isEmpty() || signupID.isEmpty() || signupPW.isEmpty() || signupCheckPW.isEmpty())
                showErrorDialog("모든 필드를 입력해주세요.")

            //비밀번호가 다시 입력한 값과 일치하지 않다면 Error Dialog 발생
            if(signupPW != signupCheckPW) showErrorDialog("비밀번호가 일치하지 않습니다.")
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