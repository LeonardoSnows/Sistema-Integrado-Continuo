package br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ServiceOrderRegisterFragment : Fragment() {

    private lateinit var tituloOrdemServicoText: TextInputEditText
    private lateinit var descricaoOrdemDeServicoText: TextInputEditText
    private lateinit var address2EmpresaMaterialInput: TextInputLayout
    private lateinit var address3EmpresaMaterialInput: TextInputLayout
    private lateinit var address4EmpresaMaterialInput: TextInputLayout
    private lateinit var address5EmpresaMaterialInput: TextInputLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_service_order_register, container, false)

        tituloOrdemServicoText = view.findViewById(R.id.titulo_ordem_servicotext)
        descricaoOrdemDeServicoText = view.findViewById(R.id.descricao_ordem_de_sercico_text)
        return view
    }

    // Função para recuperar o título da ordem de serviço
    fun getTituloOrdemServico(): String {
        return tituloOrdemServicoText.text.toString()
    }

    // Função para recuperar a descrição da ordem de serviço
    fun getDescricaoOrdemServico(): String {
        return descricaoOrdemDeServicoText.text.toString()
    }

}