package br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu


import OSAdapter
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

class StatusFragment : Fragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var osAdapter: OSAdapter
    private lateinit var sSearch: Spinner
    private lateinit var etSearch: EditText
    private lateinit var btnSearch: ImageButton
    private lateinit var btnFilter: ImageButton
    private var osTitulo : MutableList<String> = mutableListOf()
    private var filteredList: MutableList<OsItem> = mutableListOf()

    private var isSearchingByName = true

//    private lateinit var allOS: List<OSItem>

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

    data class OsItem(val id: String, val details: Map<String, Any>)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_status, container, false)
        sSearch = view.findViewById(R.id.sSearch)
        etSearch = view.findViewById(R.id.etSearch)
        btnSearch = view.findViewById(R.id.btnSearch)
        btnFilter = view.findViewById(R.id.btnFilter)

        var osItems : List<OsItem> = listOf()

        lifecycleScope.launch {
            osItems = pegarOS("1")

//            val selectedKey = "titulo"
//            var desiredItem : Any? = ""
//            osTitulo = mutableListOf()
//
//            for ((outerKey, innerMap) in osItems) {
//                for ((innerKey, value) in innerMap) {
//                    if (innerKey == selectedKey) {
//                        desiredItem = innerMap[innerKey]
//                        osTitulo.add(desiredItem as String)
//                    }
//                }
//            }

            filteredList.addAll(osItems)

            recyclerView = view.findViewById(R.id.rvOrdens)
            osAdapter = OSAdapter(filteredList)
            recyclerView.adapter = osAdapter
            recyclerView.layoutManager = LinearLayoutManager(activity)


            btnFilter.setOnClickListener {
                toggleSearchOption()
            }

            btnSearch.setOnClickListener {
                val query = if (isSearchingByName) {
                    etSearch.text.toString().trim()
                } else {
                    sSearch.selectedItem.toString()
                }
                osAdapter.performSearch(query,isSearchingByName)
            }
        }

        val statusOptions = listOf("Pendente", "Em Andamento", "Aguardando Aprovação", "Finalizada", "Cancelada")
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statusOptions)
        sSearch.adapter = spinnerAdapter

//        etSearch.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                // Not needed
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                // Filter the data based on the search query
//                val query = s.toString().trim()
//                val filteredItens = filterItens(osItems, query)
//
//                // Update the RecyclerView adapter with the filtered data
//                osAdapter = OSAdapter(osItems)
//                recyclerView.adapter = osAdapter
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                // Not needed
//            }
//        })


        return view
    }


    private fun toggleSearchOption() {
        isSearchingByName = !isSearchingByName

        // Toggle visibility of UI elements based on the selected search option
        if (isSearchingByName) {
            etSearch.visibility = View.VISIBLE
            sSearch.visibility = View.GONE
        } else {
            etSearch.visibility = View.GONE
            sSearch.visibility = View.VISIBLE
        }
    }

//    private fun performSearch() {
//        val query = if (isSearchingByName) {
//            etSearch.text.toString().trim()
//        } else {
//            sSearch.selectedItem.toString()
//        }
//
////        val query = etSearch.text.toString().trim()
//
//        filteredList.clear()
//        if (query.isEmpty()) {
//            filteredList.addAll(osTitulo)
//        } else {
//            filteredList.addAll(osTitulo.filter { it.contains(query, true) })
//        }
//
//        osAdapter.notifyDataSetChanged()
//    }


//    private fun filterItens(itens: List<OSItem>, query: String): List<OSItem> {
//        val filteredList = mutableListOf<OSItem>()
//        for (iten in itens) {
//            if (iten.title.contains(query, true)) {
//                filteredList.add(iten)
//            }
//        }
//        return filteredList
//    }

    private suspend fun pegarOS(idEmpresa: String) : List<OsItem> {
        // Pegando a instancia do Firestore e as OSs referente a empresa que foi passada
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
//        val osList = mutableListOf<Any>()
        var osDados = mutableListOf<OsItem>()
        val listaOS = db.collection("Ordem_servico").whereEqualTo("empresa", idEmpresa).get().await()
        var number = 0
        val string = "OS"
        var id = ""

        listaOS.forEach { os ->
            val osMap: MutableMap<String, Any> = os.data
            id = string + number
            val item = OsItem(id,osMap)
            osDados.add(item)
            number++
        }
        return osDados
    }
}
