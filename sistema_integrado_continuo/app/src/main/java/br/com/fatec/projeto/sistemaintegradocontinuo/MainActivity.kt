package br.com.fatec.projeto.sistemaintegradocontinuo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.fatec.projeto.sistemaintegradocontinuo.cadastro.Cadastro_Empresa
import br.com.fatec.projeto.sistemaintegradocontinuo.cadastro.Cadastro_User
import br.com.fatec.projeto.sistemaintegradocontinuo.formlogin.FormLogin
import br.com.fatec.projeto.sistemaintegradocontinuo.mainMenu.MainMenu
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        OnStart()
        setContentView(R.layout.activity_main)

        val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnSignIn = findViewById<Button>(R.id.btn_sign_in)
        val btnSignInCompany = findViewById<Button>(R.id.btn_sign_in_company)


        btnLogin.setOnClickListener {
            carregaTela(Intent(this, FormLogin::class.java))
        }

        btnSignIn.setOnClickListener {
            carregaTela(Intent(this, Cadastro_User::class.java))
        }

        btnSignInCompany.setOnClickListener {
            carregaTela(Intent(this, Cadastro_Empresa::class.java))
        }
    }

    private fun carregaTela(tela: Intent) {
        startActivity(tela)
    }

    private fun OnStart() {
        super.onStart()

        val usuarioAtual = FirebaseAuth.getInstance().currentUser
        if (usuarioAtual != null) {
            Toast.makeText(
                this, " Usuario ja logado, redirecionando!",
                Toast.LENGTH_LONG
            ).show()

            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)

        }
    }
}