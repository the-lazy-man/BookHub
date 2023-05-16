package com.example.mybooks.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mybooks.R
import com.example.mybooks.adaptor.DashboardRecyclerAdaptor
import com.example.mybooks.model.Book
import com.example.mybooks.util.ConnectionManager
import org.json.JSONException

class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard : RecyclerView
    lateinit var layoutManager : RecyclerView.LayoutManager
    val BookList = arrayListOf(
        "P.S I Love You",
        "The Great gatsby",
        "Middle march",
        "War and Peace",
        "Lolita",
        "Madame",
        "Anna kare",
        "Adventurer Finn",
        "The lord of the rings",
        "Moby-dick"
    )
    lateinit var recyclerAdaptor : DashboardRecyclerAdaptor
    lateinit var progressLayout : RelativeLayout
    lateinit var progressbar : ProgressBar
    val bookInfoList = arrayListOf<Book>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressbar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE
        layoutManager = LinearLayoutManager(activity)
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v1/book/fetch_books/"
        if(ConnectionManager().checkConnectivity(activity as Context)){
            val jsonObjectRequest = object : JsonObjectRequest (Request.Method.GET,url,null, Response.Listener {
                // Here we will handle the Response
//            println("Response is $it")
                try {
                    progressLayout.visibility = View.GONE
                    val success = it.getBoolean("success")
                    if(success){
                        val data = it.getJSONArray("data")
                        for(i in 0 until data.length()){
                            val bookJsonObject = data.getJSONObject(i)
                            val bookObject = Book (
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image"),
                            )
                            bookInfoList.add(bookObject)
                        }
                        recyclerAdaptor = DashboardRecyclerAdaptor(activity as Context,bookInfoList)
                        recyclerDashboard.adapter = recyclerAdaptor
                        recyclerDashboard.layoutManager = layoutManager

                    } else {
                        Toast.makeText(activity as Context,"Some error has Occurred !! ",Toast.LENGTH_SHORT).show()
                    }
                } catch(e : JSONException){
                    Toast.makeText(activity as Context,"Some unexpected error occurred !!",Toast.LENGTH_SHORT).show()
                }

            }, Response.ErrorListener {
                // Here we will hanle the errors
                Toast.makeText(activity as Context,"Volley error occurred !! ",Toast.LENGTH_SHORT).show()
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "0a0d793cbb0d24"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not found")
            dialog.setPositiveButton("Open Settings"){text,listner->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Cancel"){text,listner->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }
}