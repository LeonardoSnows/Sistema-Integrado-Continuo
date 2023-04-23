package br.com.fatec.projeto.sistemaintegradocontinuo.paginaCarregamento

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.content.Intent
import br.com.fatec.projeto.sistemaintegradocontinuo.MainActivity
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import br.com.fatec.projeto.sistemaintegradocontinuo.comentarios.ChatActivity


class pagina_carregamento : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagina_carregamento)

        // Atrasa a transição para a próxima página em 2 segundos
        Handler().postDelayed({
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000) // 2000 ms = 2 segundos
    }
}