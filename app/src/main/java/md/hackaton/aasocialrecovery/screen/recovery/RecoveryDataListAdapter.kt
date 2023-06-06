package md.hackaton.aasocialrecovery.screen.recovery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import md.hackaton.aasocialrecovery.databinding.ItemRecoveryDataBinding

class RecoveryDataListAdapter(items: List<ItemModel> = emptyList()) :
    RecyclerView.Adapter<RecoveryDataListAdapter.ViewHolder>() {

    data class ItemModel(
        val id: String,
        val label: String,
        val value: String
    )

    class ViewHolder(private val binding: ItemRecoveryDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ItemModel) {
            binding.labelTV.text = model.label
            binding.valueTV.text = model.value
        }
    }

    var data: List<ItemModel> = items
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecoveryDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = data[position]
        holder.bind(model)
    }

    override fun getItemCount() = data.size
}