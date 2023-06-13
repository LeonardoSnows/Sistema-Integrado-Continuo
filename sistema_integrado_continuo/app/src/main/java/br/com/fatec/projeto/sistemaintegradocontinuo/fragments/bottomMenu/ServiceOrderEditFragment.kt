package br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class ServiceOrderEditFragment : Fragment() {

    private lateinit var spinner: Spinner
    private lateinit var tituloOS: EditText
    private lateinit var descricaoOS: EditText
    private lateinit var adapter: ArrayAdapter<String>
    val items = arrayOf("Pendente", "Em Andamento", "Aguardando Aprovação","Cancelado", "Finalizado") // Defina os itens do spinner
    private lateinit var btnSalvar: Button
    var selectedItem: String? = null // Variável global nullable para armazenar o item selecionado do Spinner



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val idOS = arguments?.getString("idOS")

        lifecycleScope.launch {
            val dadosOS = listaDadosOS(idOS.toString())
            onOrdemServicoCarregada(dadosOS)
        }

        super.onViewCreated(view, savedInstanceState)

        spinner = view.findViewById(R.id.statusSpinner)
        tituloOS = view.findViewById(R.id.titulo_ordem_servicotext)
        descricaoOS = view.findViewById(R.id.descricao_ordem_de_sercico_text)


        // Cria um ArrayAdapter usando o array de itens e o layout padrão para o spinner
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)

        // Define o layout para ser usado quando a lista de opções aparecer
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Aplica o adapter ao spinner
        spinner.adapter = adapter


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Lida com a seleção do item aqui
                selectedItem = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Lida com o caso em que nada é selecionado
            }
        }
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_service_order_edit, container, false)

        btnSalvar = view.findViewById(R.id.btn_salvar_alteracoes)

        btnSalvar.setOnClickListener {
            val idOS = arguments?.getString("idOS")
            val novoTitulo =
                view.findViewById<TextInputEditText>(R.id.titulo_ordem_servicotext).text.toString()
                    .trim()
            val novaDescricao =
                view.findViewById<TextInputEditText>(R.id.descricao_ordem_de_sercico_text).text.toString()
                    .trim()
            val novoStatus = selectedItem // Utilize a variável selectedItem

            println(selectedItem)

            if (idOS != null && novoStatus != null) {
                atualizarOrdemServico(idOS, novoTitulo, novoStatus, novaDescricao)

                val fragment = StatusFragment()

                val bundle = Bundle()
                bundle.putString("idOS", idOS)
                fragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .addToBackStack(null)
                    .commit()

            }
        }


        return view
    }

    private suspend fun listaDadosOS(idOS: String): DocumentSnapshot? {
        // Pegando a instancia do Firestore e as OSs referente a empresa que foi passada
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val listaOsDados = db.collection("Ordem_servico").document(idOS).get().await()
        var id = ""

        return listaOsDados
    }

    fun onOrdemServicoCarregada(ordemServico: DocumentSnapshot?) {
        // Preencha os campos de entrada (inputs) na tela com os dados da ordem de serviço

        var titulo = ordemServico?.getString("titulo")
        var descricao = ordemServico?.getString("descricao")
        var status = ordemServico?.getString("status")

        status = capitalizePhrase(status)
        println(status)

        // Encontre a posição do status atual no array de opções do Spinner
        val position = items.indexOf(status)

        // Defina o status atual como a opção selecionada no Spinner
        spinner.setSelection(position)


        if (ordemServico != null) {
            tituloOS.setText(titulo)
            descricaoOS.setText(descricao)
         }

    }

    private fun capitalizePhrase(phrase: String?): String {
        val words = phrase?.split(" ")
            ?.map { it.toLowerCase().capitalize() }
        if (words != null) {
            return words.joinToString(" ")
        }
        return ""
    }

    fun atualizarOrdemServico(id: String, novoTitulo: String, novoStatus: String, novaDescricao: String) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val ordemServicoRef = db.collection("Ordem_servico").document(id)

        val updateMap: MutableMap<String, Any> = HashMap()
        updateMap["titulo"] = novoTitulo
        updateMap["status"] = novoStatus
        updateMap["descricao"] = novaDescricao

        ordemServicoRef.update(updateMap)
            .addOnSuccessListener {
                // Atualização concluída com sucesso
                Toast.makeText(requireContext(), "Ordem de serviço atualizada com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // Ocorreu um erro durante a atualização
                Toast.makeText(requireContext(), "Erro ao atualizar ordem de serviço: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }



}
