package br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu.adapter.ClienteAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class ListarClientesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var ClienteAdapter: ClienteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_listar_clientes, container, false)
        val item = inflater.inflate(R.layout.item_cliente, container, false)

        val btnItem = item.findViewById<Button>(R.id.verOrdensButton)

        recyclerView = view.findViewById(R.id.recyclerViewClientes)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        ClienteAdapter = ClienteAdapter(ArrayList())
        recyclerView.adapter = ClienteAdapter

        val db = FirebaseFirestore.getInstance()
        val colecao = db.collection("Empresa")

        colecao.get().addOnSuccessListener { querySnapshot: QuerySnapshot? ->
            val itemList = querySnapshot?.documents

            if (itemList != null) {
                ClienteAdapter.itemList = itemList
            }

            ClienteAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            // Trate erros ao obter os documentos
        }

        return view
    }

}
