import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.fatec.projeto.sistemaintegradocontinuo.R

class CommentsAdapter(private val commentsList: List<String>) : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = commentsList[position]
        holder.bind(comment)
    }

    override fun getItemCount(): Int {
        return commentsList.size
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvComment: TextView = itemView.findViewById(R.id.tvComment)

        fun bind(comment: String) {
            tvComment.text = comment
        }
    }
}
