package br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu.adapter

import com.google.firebase.firestore.DocumentSnapshot
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.fatec.projeto.sistemaintegradocontinuo.R

class ClienteAdapter(var itemList: List<DocumentSnapshot>) :
    RecyclerView.Adapter<ClienteAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textViewItem: TextView = itemView.findViewById(R.id.nomeClienteTextView)

        fun bind(item: DocumentSnapshot) {
            val itemText = item.getString("nome_empresa")
            textViewItem.text = itemText
        }
    }
}

