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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChatFragment : Fragment() {

    private lateinit var etComment: EditText
    private lateinit var btnSend: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var commentsAdapter: CommentsAdapter
    private val commentsList = ArrayList<String>()
    private var filteredList: MutableList<OsComments> = mutableListOf()

    data class OsComments(val id: String, val details: Map<String, Any>)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        etComment = view.findViewById(R.id.etComment)
        btnSend = view.findViewById(R.id.btnSend)

        var osComms : List<OsComments> = listOf()
        val idOS = arguments?.getString("idOS")

        lifecycleScope.launch {
            osComms = listarComentarios(idOS.toString())

            recyclerView = view.findViewById(R.id.recyclerView)
            commentsAdapter = CommentsAdapter(osComms)
            recyclerView.adapter = commentsAdapter
            recyclerView.layoutManager = LinearLayoutManager(activity)

        }
        btnSend.setOnClickListener {
            if (etComment.text.toString().trim() != null && etComment.text.toString().trim() != ""){
                commentsAdapter.criarComentarios("1",idOS.toString(),etComment.text.toString().trim())
                etComment.setText("")
            } else {
                Toast.makeText(context, "Campo vazio, digite um comentário", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }


    private suspend fun listarComentarios(idOS: String) : List<OsComments>{
        // Pegando a instancia do Firestore e as OSs referente a empresa que foi passada
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        var osDados = mutableListOf<OsComments>()
        val listaOsComments = db.collection("Ordem_servico").document(idOS).collection("comentarios").get().await()
        var id = ""


        listaOsComments.forEach { osComm ->
            val osMap: MutableMap<String, Any> = osComm.data
            id = osComm.id
            val comment = OsComments(id, osMap)
            osDados.add(comment)
        }
        return osDados
    }

}
