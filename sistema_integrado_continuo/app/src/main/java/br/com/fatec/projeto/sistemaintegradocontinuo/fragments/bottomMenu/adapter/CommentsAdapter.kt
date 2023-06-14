import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.fatec.projeto.sistemaintegradocontinuo.R
import br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu.ChatFragment
import br.com.fatec.projeto.sistemaintegradocontinuo.fragments.bottomMenu.StatusFragment
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

class CommentsAdapter(private var commentsList: List<ChatFragment.OsComments>) : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    private var filteredList: List<ChatFragment.OsComments> = commentsList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = filteredList[position]
        holder.tvComment.text = comment.details["mensagem"] as CharSequence?
        val timestamp = comment.details["data_comentario"] as com.google.firebase.Timestamp
        val date = timestamp.toDate()
        val readableDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(date)
        holder.tvData.text = readableDate
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvComment: TextView = itemView.findViewById(R.id.tvComment)
        val tvData: TextView = itemView.findViewById(R.id.tvData)

    }

    fun criarComentarios(idEmpresa: String, idOS: String, mensagem: String) {
        val db = FirebaseFirestore.getInstance()

        val novo_comentario = hashMapOf(
            "empresa" to idEmpresa,
            "data_comentario" to FieldValue.serverTimestamp(),
            "mensagem" to mensagem
        )

        db.collection("Ordem_servico").document(idOS).collection("comentarios").add(novo_comentario)
//            .addOnSuccessListener { result ->
//                println(result.id)
//            }
    }

}
