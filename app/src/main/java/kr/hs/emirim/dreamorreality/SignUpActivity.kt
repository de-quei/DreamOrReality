package kr.hs.emirim.dreamorreality

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.ComponentActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class SignUpActivity : ComponentActivity() {

    private val serverUrl = "http://10.0.2.2/dreamorreality_server/signup.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val nextButton = findViewById<Button>(R.id.go_nextBtn)
        nextButton.setOnClickListener {
            handleNextButtonClick()
        }
    }

    //error dialog를 띄우는 메소드
    private fun showErrorDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("확인", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun showSuccessDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Success")
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

    private fun handleNextButtonClick() {
        //필요 필드 값을 가져옴.
        val username = findViewById<EditText>(R.id.signUpEditTextName).text.toString()
        val userid = findViewById<EditText>(R.id.signUpEditTextID).text.toString()
        val userpw = findViewById<EditText>(R.id.signUpEditTextPW).text.toString()
        val checkpw = findViewById<EditText>(R.id.checkEditTextPW).text.toString()
        val usergender = getSelectedSpinnerItem(R.id.genderSpinner)
        val userclass = getSelectedSpinnerItem(R.id.gradeSpinner) + getSelectedSpinnerItem(R.id.classSpinner) + getSelectedSpinnerItem(R.id.numberSpinner)
        val useremail = getEmail()
        val usergraduation = getSelectedSpinnerItem(R.id.yearSpinner)
        val userdream = findViewById<EditText>(R.id.signupEditTextDream).text.toString()

        //필드가 하나라도 비었다면 Error Dialog 발생
        if(username.isEmpty() || userid.isEmpty() || userpw.isEmpty() || checkpw.isEmpty() || usergender.isEmpty()
            || userclass.isEmpty() || useremail.isEmpty() || usergraduation.isEmpty() || userdream.isEmpty())
            showErrorDialog("모든 필드를 입력해주세요.")

        //비밀번호가 다시 입력한 값과 일치하지 않다면 Error Dialog 발생
        if(userpw != checkpw) showErrorDialog("비밀번호가 일치하지 않습니다.")

        //JSON 객체를 생성하여 데이터를 저장
        val json = JSONObject()
        json.put("username", username)
        json.put("userid", userid)
        json.put("userpw", userpw)
        json.put("usergender", usergender)
        json.put("userclass", userclass)
        json.put("useremail", useremail)
        json.put("usergraduation", usergraduation)
        json.put("userdream", userdream)

        // Volley를 사용하여 서버로 POST 요청 보내기
        sendPostRequest(json.toString())
    }

    private fun sendPostRequest(data: String) {
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

                        // 에러 상세 정보를 출력할 경우
                        if (jsonResponse.has("errorDetails")) {
                            val errorDetails = jsonResponse.getString("errorDetails")
                            Log.e("ServerError", errorDetails)
                        }
                    } else {
                        val successMessage = jsonResponse.getString("message")
                        // 성공 메시지 처리
                        Log.d("ServerResponse", successMessage)
                    }
                } catch (e: JSONException) {
                    Log.e("JSONError", "Error parsing JSON: $response")
                    //json parsing error가 해결될때까지 성공 메세지코드는 여기 위치합니다.
                    showSuccessDialog("성공적으로 회원가입 되었습니다.")
                    //실질적으론 확인 버튼을 누르면 넘어가는게 아닌 2초 대기 후 넘어가는 코드
                    Handler().postDelayed({
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }, 2000) // 2초 대기
                }
            },
            Response.ErrorListener { error ->
                // 오류 처리
                Log.e("VolleyError", "Volley Error: ${error.networkResponse?.statusCode}, ${error.message}")
                showErrorDialog("서버 오류가 발생했습니다.")
            }
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return data.toByteArray()
            }
        }

        requestQueue.add(stringRequest)
    }


}