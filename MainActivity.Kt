package com.example.llmchat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class ChatRequest(val messages: List<String>)
data class ChatResponse(val response: String)

interface ChatApi {
    @POST("chat")
    fun chat(@Body request: ChatRequest): Call<ChatResponse>
}

class MainActivity : AppCompatActivity() {

    private val messages = mutableListOf<String>()
    private lateinit var adapter: ChatAdapter
    private lateinit var api: ChatApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
        }

        val recyclerView = RecyclerView(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ChatAdapter(messages)
        recyclerView.adapter = adapter
        layout.addView(recyclerView, LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f)

        val input = EditText(this).apply { hint = "Type here..." }
        layout.addView(input, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        val button = Button(this).apply { text = "Send" }
        layout.addView(button)

        setContentView(layout)

        val retrofit = Retrofit.Builder()
          .baseUrl("https://halves-movie-debtless.ngrok-free.dev/") // <-- YOUR NEW LINK IS HERE
          .addConverterFactory(GsonConverterFactory.create())
          .build()
        api = retrofit.create(ChatApi::class.java)

        button.setOnClickListener {
            val userMsg = input.text.toString()
            if (userMsg.isNotBlank()) {
                messages.add("You: $userMsg")
                input.text.clear()
                adapter.notifyItemInserted(messages.size - 1)

                val context = messages.takeLast(6) // last 3 turns = memory
                api.chat(ChatRequest(context)).enqueue(object : Callback<ChatResponse> {
                    override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                        val botMsg = response.body()?.response?: "Error"
                        messages.add("Bot: $botMsg")
                        adapter.notifyItemInserted(messages.size - 1)
                        recyclerView.scrollToPosition(messages.size - 1)
                    }
                    override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                        messages.add("Bot: Failed: ${t.message}")
                        adapter.notifyItemInserted(messages.size - 1)
                    }
                })
            }
        }
    }
}

class ChatAdapter(private val msgs: List<String>) : RecyclerView.Adapter<ChatAdapter.VH>() {
    class VH(val tv: android.widget.TextView) : RecyclerView.ViewHolder(tv)
    override fun onCreateViewHolder(p: android.view.ViewGroup, vType: Int) =
        VH(android.widget.TextView(p.context).apply { setPadding(16, 16, 16, 16) })
    override fun getItemCount() = msgs.size
    override fun onBindViewHolder(h: VH, i: Int) { h.tv.text = msgs[i] }
}
