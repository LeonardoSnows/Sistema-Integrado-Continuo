package br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeEmpresaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeEmpresaFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val totalClientesText = view.findViewById<TextView>(R.id.totalClientesText);

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val listaOS = db.collection("Empresa")


        definirContagemDadosFirebaseEmTextView(listaOS, totalClientesText)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        lifecycleScope.launch{

        }


        val view = inflater.inflate(R.layout.fragment_home_empresa, container, false)
        val totalClientesText = view.findViewById<TextView>(R.id.totalClientesText);
        // Inflate the layout for this fragment
        totalClientesText.setOnClickListener {


            val fragment = ListarClientesFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        return view
    }


    private fun qtdeClientes(
        referencia: CollectionReference,
        onComplete: (Long) -> Unit
    ) {

        referencia.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val quantidadeDocumentos = task.result?.size()
                if (quantidadeDocumentos != null) {
                    onComplete(quantidadeDocumentos.toLong())
                }
            }
        }.addOnFailureListener { exception ->
            onComplete(-1)
        }
    }

    fun definirContagemDadosFirebaseEmTextView(
        collectionReference: CollectionReference, textView: TextView,
    ) {
        qtdeClientes(collectionReference) { count ->
            if (count >= 0) {
                val texto = "Clientes Cadastrados: $count"
                textView.text = texto
            } else {
                val erro = "Erro ao recuperar dados do Firestore"
                textView.text = erro
            }
        }
    }
}