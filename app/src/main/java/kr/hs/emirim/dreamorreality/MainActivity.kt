package kr.hs.emirim.dreamorreality

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class MainActivity : ComponentActivity() {
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
            handleSignupButtonClick()
        }

    }

    private fun handleSignupButtonClick() {
        //필드 값을 가져옴.
        val usernameEditText = findViewById<EditText>(R.id.editTextID)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)

        val userId = usernameEditText.text.toString()
        val userPw = passwordEditText.text.toString()

        //필드가 하나라도 비어있을 때 에러 dialog를 발생시킴
        if (userId.isEmpty() || userPw.isEmpty()) showErrorDialog("모든 필드를 입력해주세요.");
        else signinUser(userId, userPw)
    }

    private fun signinUser(userId: String, userPw: String) {
        val serverUrl = "http://10.0.2.2/dreamorreality_server/signin.php"

        val requestQueue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(
            Request.Method.POST,
            serverUrl,
            Response.Listener { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    val error = jsonResponse.getBoolean("error")

                    if (error) {
                        val errorMessage = jsonResponse.getString("message")
                        showErrorDialog(errorMessage)
                    } else {
                        val successMessage = jsonResponse.getString("message")
                        val username = jsonResponse.getJSONObject("userData").getString("username")
                        showSuccessDialog(successMessage, username)

                        // 로그인 성공 시 HomeActivity로 이동
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    }
                } catch (e: JSONException) {
                    Log.e("JSONError", "Error parsing JSON: $response")
                    showErrorDialog("서버 응답 처리 중 오류가 발생했습니다.")
                }
            },
            Response.ErrorListener { error ->
                showErrorDialog("서버 오류가 발생했습니다.")
            }
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                val jsonParams = JSONObject()
                jsonParams.put("userid", userId)
                jsonParams.put("userpw", userPw)
                return jsonParams.toString().toByteArray()
            }
        }

        requestQueue.add(stringRequest)
    }

    private fun showSuccessDialog(message: String, username: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Success")
        builder.setMessage("$username 님 $message") // 사용자 이름을 메시지와 결합
        builder.setPositiveButton("확인", null)
        val dialog = builder.create()
        dialog.show()
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