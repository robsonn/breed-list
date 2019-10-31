package processo.smartmei.finderdogdetails

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_breed.*
import processo.smartmei.finderdogdetails.adapter.DogImageAdapter
import processo.smartmei.finderdogdetails.model.DogImage
import processo.smartmei.finderdogdetails.util.Http
import processo.smartmei.finderdogdetails.util.MethodUtils
import android.graphics.drawable.BitmapDrawable
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView







class BreedActivity : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    var dogImages: ArrayList<DogImage> = ArrayList<DogImage>()
    var imageUrlToShowList: ArrayList<String> ? = null
    var breedname: String ? = null

    var progressBar: ProgressBar? = null
    var handler = Handler()
    var http = Http()
    var methodUtils = MethodUtils()
    var hasScroll = false
    var currentItems = 0
    var outItems = 0
    var totalItems = 0
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breed)
        setSupportActionBar(toolbar)

        breedname = intent.getStringExtra("breedName")
        this.title = breedname

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        progressBar = findViewById(R.id.progressId)
        recyclerView = findViewById(R.id.rview)
        recyclerView.setHasFixedSize(true)
        viewManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = viewManager

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    hasScroll = true
                }

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                currentItems = viewManager.childCount
                totalItems = viewManager.itemCount
                outItems = (viewManager  as LinearLayoutManager).findFirstVisibleItemPosition()

                if(hasScroll && (currentItems + outItems == totalItems)) {
                    hasScroll = false
                    updateDogList()
                }
            }


        })

        val url = "https://dog.ceo/api/breed/$breedname/images"

        object : Thread() {
            override fun run() {
                progressBar!!.visibility = View.VISIBLE

                val imagesUrl = http.get(url)
                imageUrlToShowList = methodUtils.jsonArrayToStringArray(imagesUrl)

                if(imagesUrl != null) {
                    for (i in count..(totalItems + 9)) {
                        if(i < imageUrlToShowList!!.size) {
                            val bmImage = http.getImage(imageUrlToShowList?.get(i) as String)

                            if (bmImage != null) {
                                val resized = methodUtils.getResizedBitmap(bmImage, 600, 400)
                                val d = BitmapDrawable(resources, resized)
                                val dogImageM = methodUtils.getDogImage(d, breedname)
                                dogImages.add(dogImageM)
                                count++
                            }
                        }
                    }

                }

                handler.post(Runnable {

                    progressBar!!.visibility = View.GONE

                    if (dogImages != null) {
                        recyclerView.adapter = DogImageAdapter(dogImages, baseContext)

                    } else {
                        Toast.makeText(baseContext, "not found dog...", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                })
            }
        }.start()

    }

    fun updateDogList() {

        progressBar?.visibility = View.VISIBLE

        object : Thread() {
            override fun run() {
                if (imageUrlToShowList != null) {
                    for (i in count..(totalItems + 10)) {
                        if(i < imageUrlToShowList!!.size && i > count) {
                            val bmImage = http.getImage(imageUrlToShowList?.get(i))

                            if (bmImage != null) {
                                val resized = methodUtils.getResizedBitmap(bmImage, 600, 400)
                                val d = BitmapDrawable(resources, resized)
                                val dogImageM = methodUtils.getDogImage(d, breedname)
                                dogImages?.add(dogImageM)
                                count++
                            }
                        }
                    }

                }

                handler.post(Runnable {
                        if(count % 2 == 0) {
                            recyclerView.adapter!!.notifyDataSetChanged()
                            progressBar?.visibility = View.GONE
                        }


                })
            }
        }.start()
    }

}
