package kr.hs.emirim.dreamorreality

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kr.hs.emirim.dreamorreality.ui.theme.DreamOrRealityTheme
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class StudyWriteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_studywrite)

        val registrationBtn = findViewById<Button>(R.id.studyWriteRegistrationBtn)
        registrationBtn.setOnClickListener {
            handleRegistrationButtonClick()
        }
    }
    private fun getSelectedSpinnerItem(spinnerId: Int): String {
        val spinner = findViewById<Spinner>(spinnerId)
        return spinner.selectedItem.toString()
    }

    private fun updateDate(): String {
        // Spinner에서 선택한 값을 가져와서 날짜를 조합하는 로직
        val yearSpinner: Spinner = findViewById(R.id.yearSpinner)
        val monthSpinner: Spinner = findViewById(R.id.monthSpinner)
        val daySpinner: Spinner = findViewById(R.id.daySpinner)

        val selectedYear = yearSpinner.selectedItem.toString().toInt()
        val selectedMonth = monthSpinner.selectedItem.toString().toInt()
        val selectedDay = daySpinner.selectedItem.toString().toInt()

        // Calendar를 사용하여 날짜 조합
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, selectedYear)
        calendar.set(Calendar.MONTH, selectedMonth - 1) // Calendar의 월은 0부터 시작하므로 1을 빼줍니다.
        calendar.set(Calendar.DAY_OF_MONTH, selectedDay)

        // SimpleDateFormat을 사용하여 원하는 형식으로 날짜를 문자열로 변환
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun handleRegistrationButtonClick() {

        val title = findViewById<EditText>(R.id.studyWriteTitle).text.toString()
        val tag = findViewById<EditText>(R.id.studyWriteTag).text.toString()
        val content = findViewById<EditText>(R.id.studyWriteContent).text.toString()
        val period = updateDate()
        val people = getSelectedSpinnerItem(R.id.peopleSpinner)
        val limit = findViewById<EditText>(R.id.studyWriteLimit).text.toString()

        //필드가 하나라도 비었다면 Error Dialog 발생
        if(title.isEmpty() || tag.isEmpty() || content.isEmpty()
            || period.isEmpty() || people.isEmpty() || limit.isEmpty())
            showErrorDialog("모든 필드를 입력해주세요.")

        //JSON 객체를 생성하여 데이터를 저장
        val json = JSONObject()
        json.put("title", title)
        json.put("tag", tag)
        json.put("content", content)
        json.put("period", period)
        json.put("people", people)
        json.put("limit", limit)

        // Volley를 사용하여 서버로 POST 요청 보내기
        sendPostRequest(json.toString())
    }

    private fun sendPostRequest(data: String) {
        val serverUrl = "http://10.0.2.2/dreamorreality_server/studyRegistration.php"
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
                    //json parsing error가 해결될때까지 성공 코드는 여기 위치합니다.
                    showSuccessDialog("등록되었습니다.")
                    Handler().postDelayed({
                        val intent = Intent(this, StudyActivity::class.java)
                        startActivity(intent)
                    }, 2000) // 2초 대기
                }
            },
            Response.ErrorListener { error ->
                // 오류 처리
                Log.e(
                    "VolleyError",
                    "Volley Error: ${error.networkResponse?.statusCode}, ${error.message}"
                )
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
}