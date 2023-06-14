package br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import br.com.fatec.projeto.sistemaintegradocontinuo.funcoes_uteis.requestViaCep
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

//         ------------ Teste das Funções ------------ //
//        pegarDadosEmpresas()
//        criarOS("1", "Outra OS na sala", "este é um teste da nossa função")
//        criarComentarios("1", "1", "Segunda Mensagem")
//        pegarOS("1")
//        listarComentarios("1")
//        criarEmpresa(
//            "Empresa Criada",
//            "criada@gmail.com",
//            "11.634.898/0001-50",
//            "(61) 99249-4864",
//            "64606-062",
//            152
//        )


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val txtOsPendete = view.findViewById<TextView>(R.id.textOsPendente)
        val txtOsEmAndamento = view.findViewById<TextView>(R.id.textOsAndamento)
        val txtOsFinalizada = view.findViewById<TextView>(R.id.textFinalizadas)
        val txtOsCancelada = view.findViewById<TextView>(R.id.textCanceladas)
        val textAprovClient = view.findViewById<TextView>(R.id.textAprovClient)

        val firebaseAuth = FirebaseAuth.getInstance()
        val userEmail = firebaseAuth.currentUser?.email

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val listaOS = db.collection("Ordem_servico")

        if (userEmail != null) {
            definirContagemDadosFirebaseEmTextView(listaOS, txtOsPendete, "pendente", userEmail)
            definirContagemDadosFirebaseEmTextView(
                listaOS,
                txtOsEmAndamento,
                "Em Andamento",
                userEmail
            )
            definirContagemDadosFirebaseEmTextView(
                listaOS,
                textAprovClient,
                "Aguardando Aprovação",
                userEmail
            )
            definirContagemDadosFirebaseEmTextView(
                listaOS,
                txtOsFinalizada,
                "Finalizada",
                userEmail
            )
            definirContagemDadosFirebaseEmTextView(listaOS, txtOsCancelada, "Cancelada", userEmail)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment HomeFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            HomeFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }

    // --------------------- Funções do Firebase ---------------------------- //
    // Substituir os prints para os devidos campos na tela
    // Retirar as Funções daqui para as telas a qual cada função é designada


    private fun pegarDadosEmpresas(idEmpresa: String = "") {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        if (idEmpresa != "") {
            val refEmpresa = db.collection("Empresa").document(idEmpresa).get()
            refEmpresa.addOnSuccessListener { result ->
                if (result.exists()) {
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

    private fun pegarOS(idEmpresa: String) {
        // Pegando a instancia do Firestore e as OSs referente a empresa que foi passada
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val listaOS = db.collection("Ordem_servico").whereEqualTo("empresa", idEmpresa).get()

        listaOS.addOnSuccessListener { result ->
            for (os in result) {
                for (dado in os.data) {
                    println(dado)
                }
            }
        }
    }

    private fun Status_das_OS(
        referencia: CollectionReference,
        status: String,
        user: String,
        onComplete: (Long) -> Unit

    ) {

        val statusOs = referencia.whereEqualTo("empresa", user).whereEqualTo("status",status).get()

        statusOs.addOnSuccessListener { result ->
            val count = result.size()
            onComplete(count.toLong())
        }.addOnFailureListener { exception ->
            onComplete(-1)
        }
    }


    private fun listarComentarios(idOS: String) {
        // Pegando a instancia do Firestore e as OSs referente a empresa que foi passada
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        db.collection("Ordem_servico").document("1").collection("comentarios").get()
            .addOnSuccessListener { result ->
                // Aqui irá retornar os comentarios, porém para visualizar os dados
                // utilize o ".data"
                // A saida vai seguir essa estrutura
                // {mensagem=Já está pronto?, empresa=1, data_comentario=Timestamp(seconds=1684329979, nanoseconds=494000000)}
                // Isso é valido para todas as outras funções
                for (comentario in result) {
                    println(comentario.data)
                }
            }
    }

    private fun criarEmpresa(
        nome_empresa: String,
        email: String,
        cnpj: String,
        telefone: String,
        cep: String,
        numero: Int
    ) {
        // pegando os dados do endereço pelo ViaCep
        runBlocking {
            val viaCepResponse = async(Dispatchers.IO) {
                requestViaCep(cep)
            }

            val endereco = viaCepResponse.await()
            if (endereco != null) {
                val rua = endereco.logradouro
                val bairro = endereco.bairro
                val cidade = endereco.localidade
                val estado = endereco.uf

                if (email != null) {
                    // Criar uma referência para o documento da empresa usando o email como ID
                    val empresasRef = FirebaseFirestore.getInstance().collection("Empresa")
                    val empresaDoc = empresasRef.document(email)

                    // Verificar se o documento da empresa já existe
                    empresaDoc.get().addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            // Retornar que o Email já está cadastrado

                        } else {
                            // O documento da empresa não existe, crie um novo documento
                            val novaEmpresa = hashMapOf(
                                "nome_empresa" to nome_empresa,
                                "cnpj" to cnpj,
                                "telefone" to telefone,
                                "cep" to cep,
                                "numero" to numero,
                                "rua" to rua,
                                "bairro" to bairro,
                                "cidade" to cidade,
                                "estado" to estado,

                                )
                            empresaDoc.set(novaEmpresa).addOnSuccessListener {
                                // A criação foi bem-sucedida
                            }.addOnFailureListener { exception ->
                                // Tratar erros durante a criação
                            }
                        }
                    }.addOnFailureListener { exception ->
                        // Tratar erros durante a verificação do documento
                    }
                }

            } else {
                println("Não foi possível obter as informações do CEP.")
            }
        }
    }

    private fun criarOS(idEmpresa: String, titulo: String, descricao: String) {
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

    private fun criarComentarios(idEmpresa: String, idOS: String, mensagem: String) {
        val db = FirebaseFirestore.getInstance()

        val novo_comentario = hashMapOf(
            "empresa" to idEmpresa,
            "data_comentario" to FieldValue.serverTimestamp(),
            "mensagem" to mensagem
        )

        db.collection("Ordem_servico").document(idOS).collection("comentarios").add(novo_comentario)
            .addOnSuccessListener { result ->
                println(result.id)
            }
    }

    fun definirContagemDadosFirebaseEmTextView(
        collectionReference: CollectionReference, textView: TextView, status: String, user: String
    ) {
        Status_das_OS(collectionReference, status, user) { count ->
            if (count >= 0) {
                if (status == "Aguardando Aprovação") {
                    val texto = "Aprov. Cliente: $count"
                    textView.text = texto
                } else {
                    val texto = "$status: $count"
                    textView.text = texto
                }

            } else {
                val erro = "Erro ao recuperar dados do Firestore"
                textView.text = erro
            }
        }
    }
}