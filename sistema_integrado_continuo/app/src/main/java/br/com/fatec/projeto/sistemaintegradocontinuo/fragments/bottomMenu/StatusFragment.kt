package br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu


import OSAdapter
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*


class StatusFragment : Fragment(), OSAdapter.OSItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var osAdapter: OSAdapter
    private lateinit var sSearch: Spinner
    private lateinit var etSearch: EditText
    private lateinit var btnSearch: ImageButton
    private lateinit var btnFilter: ImageButton
    private var filteredList: MutableList<OsItem> = mutableListOf()
    private var isSearchingByName = true



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


        var osItems: List<OsItem> = listOf()

        lifecycleScope.launch {

            val firebaseAuth = FirebaseAuth.getInstance()
            val userEmail = firebaseAuth.currentUser?.email


            if (userEmail == "admin@admin.com") {
                osItems = pegarOS("")

            } else if (userEmail != null) {


                osItems = pegarOS(userEmail)
            } else {
                // Tratar o caso em que o usuário não está logado ou o e-mail está indisponível
            }

            filteredList.addAll(osItems)


            recyclerView = view.findViewById(R.id.rvOrdens)
            osAdapter = OSAdapter(filteredList)
            osAdapter.itemClickListener = this@StatusFragment
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
                osAdapter.performSearch(query, isSearchingByName)
            }
        }

        val statusOptions =
            listOf("Pendente", "Em Andamento", "Aguardando Aprovação", "Finalizada", "Cancelada")
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statusOptions)
        sSearch.adapter = spinnerAdapter

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

    private suspend fun pegarOS(idEmpresa: String): List<OsItem> {

        // Pegando a instancia do Firestore e as OSs referente a empresa que foi passada
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        var osDados = mutableListOf<OsItem>()
<<<<<<< Updated upstream


=======
>>>>>>> Stashed changes
        var listaOS: QuerySnapshot? = null

        if (idEmpresa == "") {
            listaOS = db.collection("Ordem_servico").get().await()
        } else {
            listaOS = db.collection("Ordem_servico").whereEqualTo("empresa", idEmpresa).get().await()
        }

        var id = ""

        listaOS.forEach { os ->
            val osMap: MutableMap<String, Any> = os.data
            id = os.id
            val item = OsItem(id, osMap)
            osDados.add(item)
        }
        return osDados
    }

    override fun onOSItemClicked(osItem: OsItem) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_layout)

        val osTitle = dialog.findViewById<TextView>(R.id.osTitle)
        val osStatus = dialog.findViewById<TextView>(R.id.osStatus)
        val osDesc = dialog.findViewById<TextView>(R.id.osDesc)
        val osDate = dialog.findViewById<TextView>(R.id.osDate)
        val osCompany = dialog.findViewById<TextView>(R.id.osCompany)

        osCompany.text = osItem.details["empresa"] as String
        osTitle.text = osItem.details["titulo"] as String
        osStatus.text = osItem.details["status"] as String
        osDesc.text = osItem.details["descricao"] as String
        val timestamp = osItem.details["data_solicitacao"] as com.google.firebase.Timestamp
        val date = timestamp.toDate()
        val readableDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)

        osDate.text = readableDate

        val osId = osItem.id as String

        val closeButton = dialog.findViewById<ImageView>(R.id.closeButton)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        val commentsButton = dialog.findViewById<Button>(R.id.commentsButton)
        commentsButton.setOnClickListener {
            val fragment = ChatFragment()


            val bundle = Bundle()
            bundle.putString("idOS", osId)
            fragment.arguments = bundle


            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit()

            dialog.dismiss()

        }

        val btnEditButton = dialog.findViewById<Button>(R.id.editButton)
        btnEditButton.setOnClickListener {
            val fragment = ServiceOrderEditFragment()

            val bundle = Bundle()
            bundle.putString("idOS", osId)
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit()

            dialog.dismiss()

        }

        dialog.show()
    }


}
