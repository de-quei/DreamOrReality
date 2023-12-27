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

        // 각 스피너 초기화
        val genderSpinner: Spinner = findViewById(R.id.genderSpinner)
        val gradeSpinner: Spinner = findViewById(R.id.gradeSpinner)
        val classSpinner: Spinner = findViewById(R.id.classSpinner)
        val numberSpinner: Spinner = findViewById(R.id.numberSpinner)
        val emailSpinner: Spinner = findViewById(R.id.emailSpinner)

        // 어댑터 생성
        val gradeAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.grade_array,
            android.R.layout.simple_spinner_item
        )
        val classAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.class_array,
            android.R.layout.simple_spinner_item
        )
        val numberAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.number_array,
            android.R.layout.simple_spinner_item
        )
        val emailAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.email_array,
            android.R.layout.simple_spinner_item
        )

        // 드롭다운 레이아웃 설정
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        numberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        emailAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // 스피너에 어댑터 설정
        gradeSpinner.adapter = gradeAdapter
        classSpinner.adapter = classAdapter
        numberSpinner.adapter = numberAdapter
        emailSpinner.adapter = emailAdapter

        val nextButton = findViewById<Button>(R.id.go_nextBtn)
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