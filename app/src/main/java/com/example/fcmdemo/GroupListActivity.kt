package com.example.fcmdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*


class GroupListActivity  : AppCompatActivity() {
    private var adapter: GroupListAdapter?=null
    var rl_list:RecyclerView?=null
    var msg=HashMap<String,String>()
    private var firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group_list_layout)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.getReference("EventInfo")
       //  msg= intent.getStringArrayListExtra("list")!!
     //   Log.d("msg string is", msg!![0].toString())
        rl_list = findViewById<RecyclerView>(R.id.rl_list);
        rl_list!!.layoutManager = LinearLayoutManager(this)
        adapter= GroupListAdapter(msg)
        databaseReference!!.addChildEventListener(
            object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                     if(msg.isEmpty()) {
                         msg!!.put(
                             (snapshot.key.toString()),
                             snapshot.getValue(String::class.java).toString()
                         )
                         adapter!!.notifyDataSetChanged()
                     }else
                     {
                         Toast.makeText(
                             this@GroupListActivity,
                             "No event created yet",
                             Toast.LENGTH_LONG
                         )
                             .show()
                     }

                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    // this method is called when the new child is added.
                    // when the new child is added to our list we will be
                    // notifying our adapter that data has changed.
                    adapter!!.notifyDataSetChanged()
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    // below method is called when we remove a child from our database.
                    // inside this method we are removing the child from our array list
                    // by comparing with it's value.
                    // after removing the data we are notifying our adapter that the
                    // data has been changed.
                    msg!!.remove(snapshot.getValue(String::class.java))
                    adapter!!.notifyDataSetChanged()
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    // this method is called when we move our
                    // child in our database.
                    // in our code we are note moving any child.
                }

                override fun onCancelled(error: DatabaseError) {
                    // this method is called when we get any
                    // error from Firebase with error.
                }
            })
        rl_list!!.adapter = adapter
    }
}




