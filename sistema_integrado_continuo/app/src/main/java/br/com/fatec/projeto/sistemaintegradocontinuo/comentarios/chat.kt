package br.com.fatec.projeto.sistemaintegradocontinuo.comentarios

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import br.com.fatec.projeto.sistemaintegradocontinuo.R

class ChatActivity : AppCompatActivity() {

    private lateinit var etComment: EditText
    private lateinit var btnSend: Button
    private lateinit var listComments: ListView
    private lateinit var commentsAdapter: ArrayAdapter<String>
    private val commentsList = ArrayList<String>() // Lista de comentários simulada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        etComment = findViewById(R.id.etComment)
        btnSend = findViewById(R.id.btnSend)
        listComments = findViewById(R.id.listComments)

        // Configurar o adaptador da lista de comentários
        commentsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, commentsList)
        listComments.adapter = commentsAdapter


        // Definir o listener de clique no botão de envio
        btnSend.setOnClickListener {
            val comment = etComment.text.toString()
            if (comment.isNotEmpty()) {
                commentsList.add(comment)
                commentsAdapter.notifyDataSetChanged()
                etComment.text.clear()
            }
        }
    }
}