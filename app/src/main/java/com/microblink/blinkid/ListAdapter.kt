package com.microblink.blinkid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.result_entry.view.*

class ListAdapter(list: MutableList<String>) : RecyclerView.Adapter<ListAdapter.FinesHolder>() {
    private var mDataset: MutableList<String?> = mutableListOf()

    inner class FinesHolder(private val view: View) : RecyclerView.ViewHolder(view){


        fun bind(s: String, position: Int) {
            itemView.resultValue.text = s
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListAdapter.FinesHolder {
        return FinesHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.result_entry,
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int = mDataset.size

    override fun onBindViewHolder(holder: ListAdapter.FinesHolder, position: Int) {
        val model: String = this.mDataset[position]!!
        holder.bind(model, position)
    }

    fun deleteItem(
        position: Int
    ) {
        mDataset.removeAt(position)
        notifyDataSetChanged()
    }

    fun addList(lst: MutableList<String>) {
        mDataset = ((lst as MutableList<String>).toMutableList())
        notifyDataSetChanged()
    }

    fun refreshList(model:String){
        updateitem(0, model)
        notifyDataSetChanged()
    }
    fun removeAll() {
        mDataset.clear()
        notifyDataSetChanged()
    }

    fun getCurrentList(): MutableList<String?> {
        if (mDataset.isNullOrEmpty())
            return mutableListOf()
        return mDataset
    }

    fun addNewItem(obj: String?) {
        obj.let {
            it?.let { it1 -> mDataset.add(it1) }
        }
        notifyDataSetChanged()
    }

    init {
        mDataset = list.toMutableList()
    }

    fun updateitem(pos: Int, obj: String?) {
        obj?.let { mDataset.set(pos, it) }
        notifyDataSetChanged()
    }


}