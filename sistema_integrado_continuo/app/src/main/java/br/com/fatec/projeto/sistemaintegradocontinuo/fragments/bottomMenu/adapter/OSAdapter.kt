import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu.StatusFragment
import java.text.SimpleDateFormat
import java.util.Locale

class OSAdapter(private val osItems: List<StatusFragment.OsItem>, var itemClickListener: OSItemClickListener? = null) : RecyclerView.Adapter<OSAdapter.OSViewHolder>() {

    interface OSItemClickListener {

        fun onOSItemClicked(osItem: StatusFragment.OsItem)
    }

    private var filteredList: List<StatusFragment.OsItem> = osItems
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OSViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_os, parent, false)
        return OSViewHolder(view)
    }

    override fun onBindViewHolder(holder: OSViewHolder, position: Int) {
        val osItem = filteredList[position]
        holder.osTextView.text = osItem.details["titulo"] as CharSequence?
        holder.osStatusView.text = osItem.details["status"] as CharSequence?

        holder.itemView.setOnClickListener {
            itemClickListener?.onOSItemClicked(osItem)
        }

//        holder.itemView.setOnClickListener {
//            val dialog = Dialog(holder.itemView.context)
//            dialog.setContentView(R.layout.popup_layout)
//
//            val osTitle = dialog.findViewById<TextView>(R.id.osTitle)
//            val osStatus = dialog.findViewById<TextView>(R.id.osStatus)
//            val osDesc = dialog.findViewById<TextView>(R.id.osDesc)
//            val osDate = dialog.findViewById<TextView>(R.id.osDate)
//
//            osTitle.text = osItem.details["titulo"] as CharSequence?
//            osStatus.text = osItem.details["status"] as CharSequence?
//            osDesc.text = osItem.details["descricao"] as CharSequence?
//            val timestamp = osItem.details["data_solicitacao"] as com.google.firebase.Timestamp
//            val date = timestamp.toDate()
//            val readableDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
//
//            osDate.text = readableDate
//
//            val closeButton = dialog.findViewById<ImageView>(R.id.closeButton)
//            closeButton.setOnClickListener {
//                dialog.dismiss()
//            }
//
//            val commentsButton = dialog.findViewById<ImageView>(R.id.commentsButton)
//            commentsButton.setOnClickListener {
//                val navController = findNavController(StatusFragment)
//            }
//
//            dialog.show()
//        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    inner class OSViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val osTextView: TextView = itemView.findViewById(R.id.tvOs)
        val osStatusView : TextView = itemView.findViewById(R.id.tvOsStatus)

    }

    fun performSearch(query: String, searchByName: Boolean) {
        filteredList = if (query.isNotEmpty()) {
            osItems.filter { item ->
                if (searchByName) {
                    item.details["titulo"].toString().contains(query, ignoreCase = true) ?: false
                } else {
                    item.details["status"].toString().contains(query, ignoreCase = true) ?: false
                }
            }
        } else {
            osItems
        }
        notifyDataSetChanged()
    }
}