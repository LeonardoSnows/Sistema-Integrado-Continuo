package br.com.fatec.projeto.sistemaintegradocontinuo.formlogin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.fatec.projeto.sistemaintegradocontinuo.MainActivity
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import br.com.fatec.projeto.sistemaintegradocontinuo.cadastro.Cadastro_User
import br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu.HomeFragment
import br.com.fatec.projeto.sistemaintegradocontinuo.mainMenu.MainMenu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class FormLogin : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_login)

        val btnEntrar = findViewById<Button>(R.id.boto_entrar)
        btnEntrar.setOnClickListener { view ->
            verifyUser(view)
        }

        val txtRecoverPass = findViewById<TextView>(R.id.txtRecuperaSenha)
        txtRecoverPass.setOnClickListener { view ->
            recoverPass(view)
        }

        val txtTelaCadastro = findViewById<TextView>(R.id.txtTelaCadastro)
        txtTelaCadastro.setOnClickListener { view ->
            val intent = Intent(this, Cadastro_User::class.java)
            startActivity(intent)
        }
    }

    private fun verifyUser(view: View) {
        val email =
            findViewById<TextInputEditText>(R.id.email_material_text).text.toString().trim();
        val senha =
            findViewById<TextInputEditText>(R.id.senha_material_text).text.toString().trim();
        if (email.isEmpty() || senha.isEmpty()) {
            val snackbar =
                Snackbar.make(view, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
            snackbar.setBackgroundTint(Color.RED)
            snackbar.show()
        } else {
            signInUser(email, senha, view)
        }
    }

    private fun recoverPass(view: View) {
        val email =
            findViewById<TextInputEditText>(R.id.email_material_text).text.toString().trim();
        if (email.isEmpty()) {
            val snackbar =
                Snackbar.make(
                    view,
                    "Preencha o campo de e-mail para que seja enviado o reset de senha",
                    Snackbar.LENGTH_SHORT
                )
            snackbar.setBackgroundTint(Color.RED)
            snackbar.show()
        } else {
            sendEmail(email)
        }
    }

    private fun sendEmail(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            val value = if (task.isSuccessful) {
                Toast.makeText(
                    this,
                    "Email enviado para $email",
                    Toast.LENGTH_LONG
                )
                    .show();
                val user = auth.currentUser
            } else {
                Toast.makeText(
                    this, " Erro ao enviar email !!!",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }

    private fun signInUser(email: String, senha: String, view: View) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { autenticacao ->
                if (autenticacao.isSuccessful) {
                    val snackbar =
                        Snackbar.make(view, "Login feito com sucesso", Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.BLUE)
                    snackbar.show()

                    startActivity(Intent(this, MainMenu::class.java))
                }
            }.addOnFailureListener {
                val snackbar =
                    Snackbar.make(
                        view,
                        "Erro ao fazer o login do usuario",
                        Snackbar.LENGTH_SHORT
                    )
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            }
    }


}
