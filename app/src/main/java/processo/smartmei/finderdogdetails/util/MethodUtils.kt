package processo.smartmei.finderdogdetails.util

import android.graphics.drawable.Drawable
import org.json.JSONArray
import processo.smartmei.finderdogdetails.model.DogImage
import android.graphics.Bitmap
import android.graphics.Matrix


class MethodUtils {
    fun jsonArrayToStringArray(breeds: JSONArray?): ArrayList<String> {
        val list: ArrayList<String> = ArrayList()
        if(breeds != null) {
            for (i in 0 until breeds.length()) {
                list.add(breeds[i] as String)
            }
        }
        return list
    }

    fun getDogImage(image: Drawable, breedName: String?): DogImage {
        val dogImage = DogImage()
        dogImage.setNames(breedName?:"not identified")
        dogImage.setImageDrawable(image)
        return dogImage
    }

    fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix()

        matrix.postScale(scaleWidth, scaleHeight)


        val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()
        return resizedBitmap
    }
}