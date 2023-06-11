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
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
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

        if (email.isEmpty() || senha.isEmpty()) {
            val snackbar =
                Snackbar.make(view, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
            snackbar.setBackgroundTint(Color.RED)
            snackbar.show()
        } else {
            auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener { task ->
                 if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Email $email foi registrado com Sucesso !!!",
                        Toast.LENGTH_LONG
                    )
                        .show();
                    val user = auth.currentUser
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