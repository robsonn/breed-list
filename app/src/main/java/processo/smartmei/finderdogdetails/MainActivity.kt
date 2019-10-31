package processo.smartmei.finderdogdetails

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import processo.smartmei.finderdogdetails.util.Http
import android.widget.Toast
import android.os.Build
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import processo.smartmei.finderdogdetails.adapter.BreedAdapter
import processo.smartmei.finderdogdetails.util.MethodUtils
import kotlin.collections.ArrayList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager

    val PERMISSION_INTERNET_CODE = 1
    var http = Http()
    var progressBar: ProgressBar? = null
    var methodUtils = MethodUtils()
    var url = "https://dog.ceo/api/breeds/list/all"
    var listToShow: ArrayList<String> ? = null
    var breedsChunked: List<List<String>> ? = null
    var currentItems = 0
    var outItems = 0
    var totalItems = 0
    var count = 0
    var hasScroll = false

    var handler = Handler()
    var handler2 = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val PERMISSIONS = arrayOf(Manifest.permission.INTERNET)
        recyclerView = findViewById(R.id.breedRecView)
        progressBar = findViewById(R.id.progressBreedId)

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
                    updateBreedList()
                }
            }


        })


        if(!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_INTERNET_CODE)
        }



        object : Thread() {
            override fun run() {
                progressBar!!.visibility = View.VISIBLE

                val breeds = http.get(url)

                handler.post(Runnable {
                    progressBar!!.visibility = View.GONE

                    if (breeds != null) {
                        breedsChunked = methodUtils.jsonArrayToStringArray(breeds).chunked(10)
                        listToShow = ArrayList<String>(breedsChunked!![count])
                        viewManager = LinearLayoutManager(baseContext)
                        recyclerView.layoutManager = viewManager
                        recyclerView.addItemDecoration(
                            DividerItemDecoration(
                                recyclerView.context,
                                DividerItemDecoration.VERTICAL
                            )
                        )


                        recyclerView.adapter =
                            BreedAdapter(listToShow!!, applicationContext) { breed ->
                                val intentDogDescription =
                                    Intent(applicationContext, BreedActivity::class.java)
                                intentDogDescription.putExtra("breedName", breed)
                                startActivity(intentDogDescription)
                            }

                    } else {
                       Toast.makeText(baseContext, "not found breed...", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }.start()

    }

    fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions.forEach{ permission ->
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                    return false
            }
        }
        return true
    }

    fun updateBreedList() {
        count++

        if(count < breedsChunked!!.size) {
            progressBar?.visibility = View.VISIBLE

            object : Thread() {
                override fun run() {

                    handler2.post(Runnable {
                            listToShow?.addAll(breedsChunked!![count])
                            recyclerView.adapter!!.notifyDataSetChanged()
                            progressBar?.visibility = View.GONE
                    })
                }
            }.start()
        }
    }
}
