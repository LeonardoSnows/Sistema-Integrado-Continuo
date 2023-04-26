package br.com.fatec.projeto.sistemaintegradocontinuo.mainMenu

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import br.com.fatec.projeto.sistemaintegradocontinuo.MainActivity
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import br.com.fatec.projeto.sistemaintegradocontinuo.cadastro.Cadastro_Empresa
import br.com.fatec.projeto.sistemaintegradocontinuo.cadastro.Cadastro_Ordem_De_Servico
import br.com.fatec.projeto.sistemaintegradocontinuo.comentarios.ChatActivity
import br.com.fatec.projeto.sistemaintegradocontinuo.formlogin.FormLogin
import br.com.fatec.projeto.sistemaintegradocontinuo.status.status
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val btnPageChat = findViewById<Button>(R.id.btnPageChat)
        val btnRegEmpresa = findViewById<Button>(R.id.btnRegEmpresa)
        val btnIni = findViewById<Button>(R.id.btnIni)
        val btnRegService = findViewById<Button>(R.id.btnRegService)
        val btnStatus = findViewById<Button>(R.id.btnStatusEmpresa)

        btnPageChat.setOnClickListener {
            carregaTela(Intent(this, ChatActivity::class.java))
        }

        btnRegEmpresa.setOnClickListener {
            carregaTela(Intent(this, Cadastro_Empresa::class.java))
        }

        btnRegService.setOnClickListener {
            carregaTela(Intent(this, Cadastro_Ordem_De_Servico::class.java))
        }

        btnIni.setOnClickListener { view ->
            signOutUser(view)
            carregaTela(Intent(this, MainActivity::class.java))
        }

        btnStatus.setOnClickListener{ view ->
            carregaTela(Intent(this, status::class.java))
        }


    }

    private fun carregaTela(tela: Intent) {
        startActivity(tela)
    }

    private fun signOutUser(view:View) {
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