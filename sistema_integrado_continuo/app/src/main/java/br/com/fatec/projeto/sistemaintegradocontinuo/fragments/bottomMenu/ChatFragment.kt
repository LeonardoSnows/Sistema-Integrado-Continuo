package br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu

import CommentsAdapter
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.fatec.projeto.sistemaintegradocontinuo.R

class ChatFragment : Fragment() {

    private lateinit var etComment: EditText
    private lateinit var btnSend: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var commentsAdapter: CommentsAdapter
    private val commentsList = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        etComment = view.findViewById(R.id.etComment)
        btnSend = view.findViewById(R.id.btnSend)
        recyclerView = view.findViewById(R.id.recyclerView)

        commentsAdapter = CommentsAdapter(commentsList)
        recyclerView.adapter = commentsAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        btnSend.setOnClickListener {
            enviarMensagem()
        }

        etComment.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND || (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                enviarMensagem()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        return view
    }

    private fun enviarMensagem() {
        val comment = etComment.text.toString()
        if (comment.isNotEmpty()) {
            commentsList.add(comment)
            commentsAdapter.notifyDataSetChanged()
            etComment.text.clear()
        }
    }
}
