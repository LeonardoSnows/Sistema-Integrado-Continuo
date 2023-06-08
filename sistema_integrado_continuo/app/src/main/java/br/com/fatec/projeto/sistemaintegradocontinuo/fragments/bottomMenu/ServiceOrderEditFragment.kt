package br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import br.com.fatec.projeto.sistemaintegradocontinuo.formlogin.FormLogin
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import br.com.fatec.projeto.sistemaintegradocontinuo.funcoes_uteis.requestViaCep
import com.google.firebase.firestore.FieldValue
import java.sql.Timestamp

class ServiceOrderEditFragment : Fragment() {
    private lateinit var spinner: Spinner

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinner = view.findViewById(R.id.statusSpinner)

        val items = arrayOf("Pendente", "Em Andamento", "Aguardando Aprovação","Cancelado", "Finalizado") // Defina os itens do spinner

        // Cria um ArrayAdapter usando o array de itens e o layout padrão para o spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)

        // Define o layout para ser usado quando a lista de opções aparecer
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Aplica o adapter ao spinner
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Lida com a seleção do item aqui
                val selectedItem = parent?.getItemAtPosition(position).toString()
                Toast.makeText(requireContext(), selectedItem, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Lida com o caso em que nada é selecionado
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_order_edit, container, false)
    }

}
