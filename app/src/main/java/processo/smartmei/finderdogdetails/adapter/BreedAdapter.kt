package processo.smartmei.finderdogdetails.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import processo.smartmei.finderdogdetails.R
import processo.smartmei.finderdogdetails.model.DogImage


class BreedAdapter constructor(breeds: ArrayList<String>, context: Context, val clickListener: (String) -> Unit): RecyclerView.Adapter<BreedAdapter.Vh>() {

    var breeds: ArrayList<String> ?= breeds
    var context: Context ? = context


override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
    val inflater = LayoutInflater.from(context)
    val view = inflater.inflate(R.layout.activity_main, parent, false)

    return Vh(view)
}

override fun getItemCount(): Int {
    if(breeds != null)
        return breeds!!.size
    else
        return 0
}

override fun onBindViewHolder(holder: Vh, position: Int) {
    var item = breeds!![position]
    holder.breedText.text = item

    holder.breedText.setOnClickListener { clickListener(item) }

}


    class Vh constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var breedText: TextView = itemView.findViewById(R.id.breedTextView)
    }

}