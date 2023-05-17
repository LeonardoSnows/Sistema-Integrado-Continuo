package br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import br.com.fatec.projeto.sistemaintegradocontinuo.R

class ChatFragment : Fragment() {

    private lateinit var etComment: EditText
    private lateinit var btnSend: Button
    private lateinit var listComments: ListView
    private lateinit var commentsAdapter: ArrayAdapter<String>
    private val commentsList = ArrayList<String>() // Lista de comentários simulada

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflar o layout do fragmento
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        etComment = view.findViewById(R.id.etComment)
        btnSend = view.findViewById(R.id.btnSend)
        listComments = view.findViewById(R.id.listComments)

        // Configurar o adaptador da lista de comentários
        commentsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, commentsList)
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

        return view
    }
}
