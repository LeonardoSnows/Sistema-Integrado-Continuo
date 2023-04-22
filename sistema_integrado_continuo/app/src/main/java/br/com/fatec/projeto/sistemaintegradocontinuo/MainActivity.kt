package br.com.fatec.projeto.sistemaintegradocontinuo

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import br.com.fatec.projeto.sistemaintegradocontinuo.cadastro.Cadastro_User
import br.com.fatec.projeto.sistemaintegradocontinuo.formlogin.FormLogin
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
                val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnSignIn = findViewById<Button>(R.id.btn_sign_in)
        val btnSignOut = findViewById<Button>(R.id.btn_sign_out_user)

        btnLogin.setOnClickListener {
            carregaTela(Intent(this, FormLogin::class.java))
        }

        btnSignIn.setOnClickListener {
            carregaTela(Intent(this, Cadastro_User::class.java))
        }

        btnSignOut.setOnClickListener { view ->
            val usuarioAtual = FirebaseAuth.getInstance().currentUser
            if (usuarioAtual != null) {
                FirebaseAuth.getInstance().signOut()
                carregaTela(Intent(this, FormLogin::class.java))
            } else {
                val snackbar =
                    Snackbar.make(view, "Usuário não logado !!!", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            }
        }
    }

    private fun carregaTela(tela: Intent) {
        startActivity(tela)
    }
}