package me.kmmiller.baseui.navigation

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import me.kmmiller.baseui.R
import me.kmmiller.baseui.databinding.BottomNavViewHolderBinding
import java.util.*

class BottomNavAdapter(initialItem: Int, @ColorInt private val textActiveColor: Int, private val navItems: ArrayList<BottomNavItemModel>) : RecyclerView.Adapter<BottomNavAdapter.BottomNavViewHolder>() {

    private var currentNavId = initialItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomNavViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BottomNavViewHolder(BottomNavViewHolderBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: BottomNavViewHolder, position: Int) {
        val item = navItems[position]
        holder.setLabel(item.label)

        if(item.navId == currentNavId) {
            val textColorStateList = ColorStateList.valueOf(textActiveColor)
            holder.apply {
                setTextColor(textColorStateList)
                setIcon(item.iconActive)
            }
        } else {
            val textColorStateList = ColorStateList.valueOf(getColor(holder.itemView.context, R.color.dark_gray))
            holder.apply {
                setTextColor(textColorStateList)
                setIcon(item.iconInactive)
            }
        }

        holder.itemView.setOnClickListener {
            if(it.context is BottomNavAdapterListener) {
                currentNavId = item.navId
                (it.context as BottomNavAdapterListener).onNavItemSelected(navItems[holder.adapterPosition].navId)
            }
        }
    }

    fun updateSelected(listener: BottomNavAdapterListener, navId: Int) {
        currentNavId = navId
        listener.onNavItemSelected(navId)
    }

    override fun getItemCount(): Int = navItems.size

    inner class BottomNavViewHolder(private val binding: BottomNavViewHolderBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = itemView.context

        fun setIcon(@DrawableRes drawableRes: Int) {
            binding.itemIcon.setImageDrawable(ContextCompat.getDrawable(context, drawableRes))
        }

        fun setLabel(@StringRes labelRes: Int) {
            binding.itemLabel.text = context.getString(labelRes).toUpperCase(Locale.getDefault())
        }

        fun setTextColor(color: ColorStateList) {
            binding.itemLabel.setTextColor(color)
        }
    }

    interface BottomNavAdapterListener {
        fun onNavItemSelected(@IdRes itemId: Int)
    }
}