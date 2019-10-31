package processo.smartmei.finderdogdetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import processo.smartmei.finderdogdetails.R
import processo.smartmei.finderdogdetails.model.DogImage

class DogImageAdapter constructor(dogImageArrayList: ArrayList<DogImage>, context: Context): RecyclerView.Adapter<DogImageAdapter.ViewHolder>() {

    var dogImageArrayList: ArrayList<DogImage> ?= dogImageArrayList
    var context: Context ? = context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.content_breed, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(dogImageArrayList != null)
            return dogImageArrayList!!.size
        else
            return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dogImageArrayList!!.get(position)
        holder.iv.setImageDrawable(item.getImageDrawable())

    }


    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var iv: ImageView = itemView.findViewById(R.id.imgView)

    }

}