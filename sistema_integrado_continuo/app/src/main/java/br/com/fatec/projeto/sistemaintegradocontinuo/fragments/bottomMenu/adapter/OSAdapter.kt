import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu.StatusFragment

//class OSAdapter(private val osList: List<String>) : RecyclerView.Adapter<OSAdapter.OSViewHolder>() {
//
//    class OSViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val itemTextView: TextView = view.findViewById(R.id.tvOs)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OSViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_os, parent, false)
//        return OSViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: OSViewHolder, position: Int) {
//        val item = items[position]
//        holder.itemTextView.text = item
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//}



class OSAdapter(private val osItems: List<StatusFragment.OSItem>) : RecyclerView.Adapter<OSAdapter.OSViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OSViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_os, parent, false)
        return OSViewHolder(view)
    }

    override fun onBindViewHolder(holder: OSViewHolder, position: Int) {
        val osItem = osItems[position]
        holder.bind(osItem)
    }

    override fun getItemCount(): Int {
        return osItems.size
    }

    inner class OSViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(osItem: StatusFragment.OSItem) {
            // Bind the data to your view holder's views
            itemView.findViewById<TextView>(R.id.tvOs).text = osItem.title
        }
    }
}