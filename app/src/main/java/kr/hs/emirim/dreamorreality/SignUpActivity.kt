package kr.hs.emirim.dreamorreality

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.ComponentActivity

class SignUpActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //필요 필드 값을 가져옴.
        val username = findViewById<EditText>(R.id.signUpEditTextName).text.toString()
        val userid = findViewById<EditText>(R.id.signUpEditTextID).text.toString()
        val userpw = findViewById<EditText>(R.id.signUpEditTextPW).text.toString()
        val checkpw = findViewById<EditText>(R.id.checkEditTextPW).text.toString()
        val usergrade = getSelectedSpinnerItem(R.id.gradeSpinner) + getSelectedSpinnerItem(R.id.classSpinner) + getSelectedSpinnerItem(R.id.numberSpinner)
        val useremail = getEmail()
        val usergraduation = getSelectedSpinnerItem(R.id.yearSpinner)
        val userdream = findViewById<EditText>(R.id.signupEditTextDream).text.toString()

        val nextButton = findViewById<Button>(R.id.go_nextBtn)
        nextButton.setOnClickListener {

            //필드가 하나라도 비었다면 Error Dialog 발생
            if(username.isEmpty() || userid.isEmpty() || userpw.isEmpty() || checkpw.isEmpty()
                || usergrade.isEmpty() || useremail.isEmpty() || usergraduation.isEmpty() || userdream.isEmpty())
                showErrorDialog("모든 필드를 입력해주세요.")

            //비밀번호가 다시 입력한 값과 일치하지 않다면 Error Dialog 발생
            if(userpw != checkpw) showErrorDialog("비밀번호가 일치하지 않습니다.")
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

    //학번 조합 메소드
    private fun getSelectedSpinnerItem(spinnerId: Int): String {
        val spinner = findViewById<Spinner>(spinnerId)
        return spinner.selectedItem.toString()
    }

    //이메일 조합 메소드
    private fun getEmail(): String {
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val emailSpinner = findViewById<Spinner>(R.id.emailSpinner)
        val emailProvider = emailSpinner.selectedItem.toString()

        return emailEditText.text.toString() + "@" + emailProvider

    }

}