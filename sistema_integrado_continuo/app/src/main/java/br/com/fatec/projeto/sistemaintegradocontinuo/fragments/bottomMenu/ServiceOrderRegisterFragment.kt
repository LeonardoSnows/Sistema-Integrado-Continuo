package br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import br.com.fatec.projeto.sistemaintegradocontinuo.cadastro.Cadastro_User
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class ServiceOrderRegisterFragment : Fragment() {

    private lateinit var tituloOrdemServicoText: TextInputEditText
    private lateinit var descricaoOrdemDeServicoText: TextInputEditText
    private lateinit var address2EmpresaMaterialInput: TextInputLayout
    private lateinit var address3EmpresaMaterialInput: TextInputLayout
    private lateinit var address4EmpresaMaterialInput: TextInputLayout
    private lateinit var address5EmpresaMaterialInput: TextInputLayout
    private lateinit var btnCadastrar: Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_service_order_register, container, false)

        tituloOrdemServicoText = view.findViewById(R.id.titulo_ordem_servicotext)
        descricaoOrdemDeServicoText = view.findViewById(R.id.descricao_ordem_de_sercico_text)

        btnCadastrar = view.findViewById(R.id.btn_cadastrar_os)

        btnCadastrar.setOnClickListener {
            // Para pegar o email quando tiver funcionando
            // val email = FirebaseAuth.getInstance()
            // val emailUser = email.currentUser?.email

            val titulo = view.findViewById<TextInputEditText>(R.id.titulo_ordem_servicotext).text.toString().trim();
            val descricao = view.findViewById<TextInputEditText>(R.id.descricao_ordem_de_sercico_text).text.toString().trim();

            criarOS("1", titulo, descricao)
            tituloOrdemServicoText.setText("")
            descricaoOrdemDeServicoText.setText("")

        }

        return view
    }

    // Função para recuperar o título da ordem de serviço
    fun getTituloOrdemServico(): String {
        return tituloOrdemServicoText.text.toString()
    }

    // Função para recuperar a descrição da ordem de serviço
    fun getDescricaoOrdemServico(): String {
        return descricaoOrdemDeServicoText.text.toString()
    }

    private fun criarOS(idEmpresa: String, titulo: String, descricao: String){
        val db = FirebaseFirestore.getInstance()

        val novaOrdem = hashMapOf(
            "empresa" to idEmpresa,
            "data_solicitacao" to FieldValue.serverTimestamp(),
            "status" to "pendente",
            "titulo" to titulo,
            "descricao" to descricao,
        )

        val nova_os = db.collection("Ordem_servico")
        nova_os.add(novaOrdem).addOnSuccessListener { result ->
            val id_nova_os = result.id
            println("${id_nova_os}")
        }
    }

    private fun pegarDadosEmpresas(idEmpresa: String = ""){
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        if (idEmpresa != ""){
            val refEmpresa = db.collection("Empresa").document(idEmpresa).get()
            refEmpresa.addOnSuccessListener { result ->
                if (result.exists()){
                    println(result["nome_empresa"])
                } else {
                    println("Não encontrado")
                }
            }
        } else {
            db.collection("Empresa").get().addOnSuccessListener { result ->
                for (document in result) {
                    for (docu in document.data) {
                        println("${docu.value}")
                    }
                    println("-----------------------")
                }
            }.addOnFailureListener { exception ->
                println(exception)
            }
        }
    }

}