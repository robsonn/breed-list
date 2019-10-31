package processo.smartmei.finderdogdetails.model

import android.graphics.drawable.Drawable

class DogImage {
    var name: String? = null
    var image_drawable: Drawable? = null

    fun getNames(): String {
        return name.toString()
    }

    fun setNames(name: String) {
        this.name = name
    }

    fun getImageDrawable(): Drawable {
        return image_drawable!!
    }

    fun setImageDrawable(image_drawable: Drawable) {
        this.image_drawable = image_drawable
    }

}
