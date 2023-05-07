package br.com.fatec.projeto.sistemaintegradocontinuo.cadastro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import br.com.fatec.projeto.sistemaintegradocontinuo.databinding.ActivityMainBinding

class Cadastro_Empresa : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_cadastro_empresa)
    }

    private fun carregaTela(tela: Intent) {
        startActivity(tela)
    }
}