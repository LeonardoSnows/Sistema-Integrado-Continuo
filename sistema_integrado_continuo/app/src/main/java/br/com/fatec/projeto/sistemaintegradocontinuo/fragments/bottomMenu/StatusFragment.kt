package br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu


import OSAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StatusFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var osAdapter: OSAdapter

    private lateinit var etSearch: EditText

    private lateinit var allOS: List<OSItem>

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_status)
//
//        val recyclerView: RecyclerView = findViewById(R.id.rvOrdens)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        val items = listOf("Item 1", "Item 2", "Item 3") // Replace with your data
//        val adapter = OSAdapter(items)
//        recyclerView.adapter = adapter
//    }


    data class OSItem(val title: String)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_status, container, false)

        etSearch = view.findViewById(R.id.etSearch)

        var osItems : List<StatusFragment.OSItem> = listOf()



        lifecycleScope.launch{
            osItems = pegarOS("1")

            recyclerView = view.findViewById(R.id.rvOrdens)
            recyclerView.layoutManager = LinearLayoutManager(activity)
            osAdapter = OSAdapter(osItems)
            recyclerView.adapter = osAdapter
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filter the data based on the search query
                val query = s.toString().trim()
                val filteredItens = filterItens(osItems, query)

                // Update the RecyclerView adapter with the filtered data
                osAdapter = OSAdapter(osItems)
                recyclerView.adapter = osAdapter
            }

            override fun afterTextChanged(s: Editable?) {
                // Not needed
            }
        })


        return view
    }




    private fun filterItens(itens: List<OSItem>, query: String): List<OSItem> {
        val filteredList = mutableListOf<OSItem>()
        for (iten in itens) {
            if (iten.title.contains(query, true)) {
                filteredList.add(iten)
            }
        }
        return filteredList
    }

    private suspend fun pegarOS(idEmpresa: String) : List<OSItem>{
        // Pegando a instancia do Firestore e as OSs referente a empresa que foi passada
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val osList = mutableListOf<OSItem>()
        val listaOS = db.collection("Ordem_servico").whereEqualTo("empresa", idEmpresa).get().await()


        for (os in listaOS) {
            val osDados = os.data
            val osTitulo = osDados["titulo"] as? String
            println(osDados)
            osTitulo?.let {
                osList.add(OSItem(it))
            }
        }

        return osList
    }



}