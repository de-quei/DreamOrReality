package kr.hs.emirim.dreamorreality

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONException

class StudyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study)

        val fab = findViewById<ExtendedFloatingActionButton>(R.id.floatingBtn)
        fab.setOnClickListener{
            val intent = Intent(this, StudyWriteActivity::class.java)
            startActivity(intent)
        }

        fetchDataFromServer()
    }

    private fun fetchDataFromServer() {
        // 서버에서 데이터를 가져오기 위한 Volley 요청 생성
        val serverUrl = "http://10.0.2.2/dreamorreality_server/fetchData.php"
        val requestQueue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(
            Request.Method.GET,
            serverUrl,
            { response ->
                // JSON 응답을 파싱하고 UI 업데이트
                updateUIWithFetchedData(response)
            },
            { error ->
                // 오류 처리
                Log.e("VolleyError", "Volley Error: ${error.networkResponse?.statusCode}, ${error.message}")
            }
        )

        // 요청을 큐에 추가
        requestQueue.add(stringRequest)
    }

    private fun updateUIWithFetchedData(response: String) {
        // JSON 응답을 파싱하고 UI를 업데이트
        try {
            val jsonArray = JSONArray(response)

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)

                // JSON 객체에서 데이터 추출
                val title = jsonObject.getString("title")
                val tag = jsonObject.getString("tag")
                val content = jsonObject.getString("content")
                val period = jsonObject.getString("period")
                val people = jsonObject.getString("people")
                val limit = jsonObject.getString("limit")

                // 가져온 데이터로 UI 업데이트
                val textView = TextView(this)

                textView.text = "제목: $title\n태그: $tag\n 내용: $content\n 기간: $period\n 인원: $people\n 제한사항: $limit\n\n"
                // LinearLayout에 TextView 추가
                findViewById<LinearLayout>(R.id.contentLayout).addView(textView)
            }
        } catch (e: JSONException) {
            Log.e("JSONError", "JSON 파싱 오류: $response")
        }
    }
}