package br.com.fatec.projeto.sistemaintegradocontinuo.mainMenu

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import br.com.fatec.projeto.sistemaintegradocontinuo.databinding.ActivityMainMenuBinding
import br.com.fatec.projeto.sistemaintegradocontinuo.formlogin.FormLogin
import br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu.ChatFragment
import br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu.HomeFragment
import br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu.ServiceOrderEditFragment
import br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu.ServiceOrderRegisterFragment
import br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu.StatusFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class MainMenu : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {

                R.id.homePage -> replaceFragment(HomeFragment())
                R.id.chatPage -> replaceFragment(ChatFragment())
                R.id.statusPage -> replaceFragment(StatusFragment())
                R.id.criarOS -> replaceFragment(ServiceOrderRegisterFragment())

                else -> {}
            }
            true
        }
    }

    private fun carregaTela(tela: Intent) {
        startActivity(tela)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun signOutUser(view: View) {
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