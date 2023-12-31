package kr.hs.emirim.dreamorreality

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
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
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study)

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

        val fab = findViewById<ExtendedFloatingActionButton>(R.id.floatingBtn)
        fab.setOnClickListener{
            val intent = Intent(this, StudyWriteActivity::class.java)
            startActivity(intent)
        }

        // ListView 초기화 및 어댑터 설정
        listView = findViewById<ListView>(R.id.contents)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListOf<String>())
        listView.adapter = adapter

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
                val content = jsonObject.getString("content")
                val tag = jsonObject.getString("tag")
                val period = jsonObject.getString("period")
                val people = jsonObject.getInt("people")
                val limit = jsonObject.getString("limit")

                // 가져온 데이터로 UI 업데이트
                val inflater = layoutInflater
                val itemView = inflater.inflate(R.layout.list_item_layout, null)

                val textTitle = itemView.findViewById<TextView>(R.id.textTitle)
                val textContent = itemView.findViewById<TextView>(R.id.textContent)
                val textTag = itemView.findViewById<TextView>(R.id.textTag)

                textTitle.text = "제목: $title"
                textContent.text = "내용: $content"
                textTag.text ="$tag"

                listView.addHeaderView(itemView) // 리스트뷰에 추가
            }
        } catch (e: JSONException) {
            Log.e("JSONError", "JSON 파싱 오류: $response")
        }
    }
}