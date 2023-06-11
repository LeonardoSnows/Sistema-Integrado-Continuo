import android.app.Dialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu.StatusFragment
import kotlinx.coroutines.CoroutineScope
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Objects

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



class OSAdapter(private val osItems: List<StatusFragment.OsItem>) : RecyclerView.Adapter<OSAdapter.OSViewHolder>() {

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
            val dialog = Dialog(holder.itemView.context)
            dialog.setContentView(R.layout.popup_layout)

            val osTitle = dialog.findViewById<TextView>(R.id.osTitle)
            val osStatus = dialog.findViewById<TextView>(R.id.osStatus)
            val osDesc = dialog.findViewById<TextView>(R.id.osDesc)
            val osDate = dialog.findViewById<TextView>(R.id.osDate)

            osTitle.text = osItem.details["titulo"] as CharSequence?
            osStatus.text = osItem.details["status"] as CharSequence?
            osDesc.text = osItem.details["descricao"] as CharSequence?
            val timestamp = osItem.details["data_solicitacao"] as com.google.firebase.Timestamp
            val date = timestamp.toDate()
            val readableDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)

            osDate.text = readableDate

            // Customize the dialog layout and its views
            // For example:
            val closeButton = dialog.findViewById<ImageView>(R.id.closeButton)
            closeButton.setOnClickListener {
                dialog.dismiss() // Dismiss the dialog when the close button is clicked
            }

            // Show the dialog
            dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    inner class OSViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val osTextView: TextView = itemView.findViewById(R.id.tvOs)
        val osStatusView : TextView = itemView.findViewById(R.id.tvOsStatus)

//        init {
//            osTextView.setOnClickListener{
//                val position = bindingAdapterPosition
//                onItemClick(position)
//            }
//        }
    }

    fun performSearch(query: String, searchByName: Boolean) {
        filteredList = if (query.isNotEmpty()) {
            osItems.filter { item ->
                if (searchByName) {
                    item.details["titulo"].toString().contains(query, ignoreCase = true) ?: false
                } else {
                    item.details["status"].toString().contains(query, ignoreCase = true) ?: false
                }
//                when (searchBy) {
//                    "ID" -> item.id.contains(query, ignoreCase = true)
//                    "Name" -> item.details["name"]?.contains(query, ignoreCase = true) ?: false
//                    "Status" ->
//                    else -> false
//                }
            }
        } else {
            osItems
        }
        notifyDataSetChanged()
    }


}