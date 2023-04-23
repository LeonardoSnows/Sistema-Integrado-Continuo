package br.com.fatec.projeto.sistemaintegradocontinuo.cadastro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import br.com.fatec.projeto.sistemaintegradocontinuo.comentarios.Chat

class Cadastro_Empresa : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_empresa)

        val btnNextPageCep = findViewById<Button>(R.id.btnNextPageCep)

        btnNextPageCep.setOnClickListener {
            carregaTela(Intent(this, Cadastro_Cep_Empresa::class.java))
        }
    }

    private fun carregaTela(tela: Intent) {
        startActivity(tela)
    }
}