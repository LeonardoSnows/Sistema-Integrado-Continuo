package br.com.fatec.projeto.sistemaintegradocontinuo.cadastro

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import br.com.fatec.projeto.sistemaintegradocontinuo.formlogin.FormLogin
import br.com.fatec.projeto.sistemaintegradocontinuo.funcoes_uteis.requestViaCep
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Text

class Cadastro_User : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_user)

        val btnCadastro = findViewById<Button>(R.id.btn_cadastro_user)
        btnCadastro.setOnClickListener { view ->
            createUserFireBase(view)
        }

        val txtTelaLogin = findViewById<TextView>(R.id.txtTelaLogin)
        txtTelaLogin.setOnClickListener {
            val intent = Intent(this, FormLogin::class.java)
            startActivity(intent)
        }

    }

    private fun createUserFireBase(view: View) {
        val email =
            findViewById<TextInputEditText>(R.id.email_cadastro_material_text).text.toString()
                .trim();
        val senha =
            findViewById<TextInputEditText>(R.id.senha_cadastro_material_text).text.toString()
                .trim();
        val nome =
            findViewById<TextInputEditText>(R.id.nome_empresa_text).text.toString()
                .trim();
        val cnpj =
            findViewById<TextInputEditText>(R.id.cnpj_text).text.toString()
                .trim();
        val fone =
            findViewById<TextInputEditText>(R.id.telefone_text).text.toString()
                .trim();
        val cep =
            findViewById<TextInputEditText>(R.id.cep_text).text.toString()
                .trim();
        val numero =
            findViewById<TextInputEditText>(R.id.numero_text).text.toString()
                .trim();

        if (email.isEmpty() || senha.isEmpty()) {
            val snackbar = Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT)
            snackbar.setBackgroundTint(Color.RED)
            snackbar.show()
        } else {
            auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener { task ->
                val value = if (task.isSuccessful) {
                    criarEmpresa(nome, email, cnpj, fone, cep, numero)
                    Toast.makeText(
                        this,
                        "Email ${email} foi registrado com sucesso",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this, " Email invalido !!! !!!",
                        Toast.LENGTH_LONG
                    ).show();
                    val exception = task.exception;
                    println("Erro ao cadastrar o usuário: ${exception?.message}")
                }
            }
        }
    }
}

private fun criarEmpresa(
    nome_empresa: String,
    email: String,
    cnpj: String,
    telefone: String,
    cep: String,
    numero: String
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
                empresaDoc.get()
                    .addOnSuccessListener { documentSnapshot ->
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
                                "email" to email,
                            )
                            empresaDoc.set(novaEmpresa)
                                .addOnSuccessListener {
                                    // A criação foi bem-sucedida
                                }
                                .addOnFailureListener { exception ->
                                    println(exception.message)
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Tratar erros durante a verificação do documento
                    }
            }

        } else {
            println("Não foi possível obter as informações do CEP.")
        }
    }
}