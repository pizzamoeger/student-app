import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.example.studentapp.databinding.ItemCheckboxBinding
import com.example.studentapp.databinding.ItemInputBinding
import com.example.studentapp.ui.assignments.checkbox.CheckboxItem

class CheckboxAdapter(
    private val items: MutableList<CheckboxItem>,
    private val onItemAdded: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class CheckboxViewHolder(val binding: ItemCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class InputViewHolder(val binding: ItemInputBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is CheckboxItem.Checkbox -> 0
            is CheckboxItem.InputBox -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding = ItemCheckboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CheckboxViewHolder(binding)
        } else {
            val binding = ItemInputBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            InputViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is CheckboxItem.Checkbox -> {
                val viewHolder = holder as CheckboxViewHolder
                viewHolder.binding.editText.setText(item.text)
                viewHolder.binding.checkbox.text = ""
                viewHolder.binding.checkbox.isChecked = item.checked
                viewHolder.binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                    item.checked = isChecked
                }
            }
            is CheckboxItem.InputBox -> {
                val viewHolder = holder as InputViewHolder
                val editText = viewHolder.binding.editText
                editText.requestFocus()

                editText.setOnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                        val input = editText.text.toString().trim()
                        if (input.isNotEmpty()) {
                            onItemAdded(input)
                            editText.setText("")
                        }
                        true
                    } else {
                        false
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
