package com.example.fcmdemo

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fcmdemo.APIManager.*
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private var isGroupCreated: Boolean = false
    private var firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private var isFirstClick: Boolean? = false
    private var isDone: Boolean? = false
    private var token: String? = null
    private var edtTopic: EditText? = null
    private var btnNotifyAll: Button? = null
    private var btnNotify: Button? = null
    private var btn_list: Button? = null
    private var btn_create_group: Button? = null
    var operationModel = OperationModel()
    private var topicListModel = TopicListModel()
    var restclient = RestClient()
    var restClientPost = RestClientPost()
    var grouplst = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtTopic = findViewById<EditText>(R.id.edt_topic)
        btnNotifyAll = findViewById<Button>(R.id.btn_notify_all)
        btnNotify = findViewById<Button>(R.id.btn_notify)
        btn_create_group = findViewById<Button>(R.id.btn_create_group)
        btn_list = findViewById<Button>(R.id.btn_list)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.getReference("EventInfo")


        RegisterToDefaultFCMTopic()
        CreateGroup()
        NotifyAll()
        NotifyMe()
        getGroupList()

    }

    override fun onResume() {
        super.onResume()
        try {
            if (grouplst != null) {
                grouplst.clear()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getGroupList() {

        btn_list!!.setOnClickListener {
            //         isFirstClick = true
            //          btn_create_group?.performClick()
//            if (topicListModel.rel != null) {
//                    topicListModel.rel!!.topics!!.forEach { (key, value) ->
//                        grouplst.add(key as String)
//                        Log.d("key:", key)
//                        isDone = true
//                    }
//                } else {
//                    Toast.makeText(this, "No Group Available Yet", Toast.LENGTH_LONG).show()
//                }
//                if (isDone == true) {
            var intent = Intent(this, GroupListActivity::class.java)
            // intent.putStringArrayListExtra("list", grouplst)
            startActivity(intent)
//                    isDone = false
//                }


        }
    }

    private fun NotifyMe() {
        btnNotify!!.setOnClickListener {
            if (edtTopic!!.text.isNotEmpty() || edtTopic!!.text.isNotBlank()) {
                databaseReference!!.addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                if (snapshot.hasChild(edtTopic!!.text.toString())) {
                                    operationModel.priority = "high"
                                    operationModel.condition =
                                        "\'" + edtTopic!!.text.toString() + "\'" + " in topics"
                                    operationModel.notification = Notification()
                                    operationModel.notification!!.title = "FCM Demo APP"
                                    operationModel.notification!!.body =
                                        " user subscribed to the " + edtTopic!!.text.toString() + " now..."
                                    operationModel.data = Data()
                                    operationModel.data!!.customId = "2"
                                    operationModel.data!!.badge = 1
                                    operationModel.data!!.alert = "Alert"
                                    restClientPost.postTopic(operationModel)
                                } else {
                                    FirebaseMessaging.getInstance()
                                        .unsubscribeFromTopic(edtTopic!!.text.toString())
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Group Not Available",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            } else {

                                android.widget.Toast.makeText(
                                    this@MainActivity,
                                    "Group Not Available",
                                    android.widget.Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })


            } else {
                Toast.makeText(
                    this,
                    "Please Enter the topic Name",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }

    }

    private fun CreateGroup() {

        btn_create_group!!.setOnClickListener {
            if (isFirstClick == true) {
                isFirstClick = false
                FirebaseMessaging.getInstance().token
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            if (task.result != null && !TextUtils.isEmpty(task.result)) {
                                token = task.result!!
                                Log.d("token is", token!!)
                            }
                            FirebaseMessaging.getInstance()
                                .subscribeToTopic(edtTopic!!.text.toString())
                            val thread = Thread(Runnable {
                                topicListModel =
                                    token?.let { it1 -> restclient.getTopics(it1) }!!
                                Log.d("jn", topicListModel.toString())


                            })
                            thread.start()
                        }
                    }
            } else {
                if (edtTopic!!.text.isNotEmpty() || edtTopic!!.text.isNotBlank()) {
                    var event_name: String = edtTopic!!.text.toString()
                    FirebaseMessaging.getInstance().token
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                if (task.result != null && !TextUtils.isEmpty(task.result)) {
                                    token = task.result!!
                                    Log.d("token is", token!!)
                                }
                                FirebaseMessaging.getInstance()
                                    .subscribeToTopic(event_name)
                                isGroupCreated = true
                                if (isFirstClick == true) {
                                    isFirstClick = false

                                } else {
                                    addDatabaseFirebase(
                                        event_name,
                                        Calendar.getInstance().time.toString()
                                    )

                                    Toast.makeText(
                                        this,
                                        "Group created successfully",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                                val thread = Thread(Runnable {
                                    topicListModel =
                                        token?.let { it1 -> restclient.getTopics(it1) }!!
                                    Log.d("jn", topicListModel.toString())


                                })
                                thread.start()

                            }
                        }

                } else {
                    Toast.makeText(
                        this,
                        "Please Enter the topic Name",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }

    }

    private fun addDatabaseFirebase(eventName: String, dateTime: String) {

        var eventInfoModel = EventInfoModel()
        eventInfoModel.date_time = dateTime
        databaseReference!!.child(eventName).setValue(dateTime);

        Log.d("Main", "ondatachange")
        // after adding this data we are showing toast message.
        Toast.makeText(this@MainActivity, "data added", Toast.LENGTH_SHORT).show()
    }


    private fun NotifyAll() {
        btnNotifyAll?.setOnClickListener {

            if ((edtTopic!!.text.isNotEmpty() || edtTopic!!.text.isNotBlank())) {
                if (isGroupCreated) {
                    operationModel.priority = "high"
                    operationModel.condition = "'all' in topics"
                    operationModel.notification = Notification()
                    operationModel.notification!!.title = "FCM Demo APP"
                    operationModel.notification!!.body =
                        edtTopic!!.text.toString() + " Group successfully created please click to subscribe"
                    operationModel.data = Data()
                    operationModel.data!!.customId = "2"
                    operationModel.data!!.badge = 1
                    operationModel.data!!.alert = "Alert"
                    restClientPost.postTopic(operationModel)
                    isGroupCreated = false

                } else {
                    Toast.makeText(
                        this,
                        "Please create the event",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }

            } else {
                Toast.makeText(
                    this,
                    "Please Enter the event Name",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
//
//
        }
    }

    private fun RegisterToDefaultFCMTopic() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result != null && !TextUtils.isEmpty(task.result)) {
                        val token: String = task.result!!
                        Log.d("token is", token)
                        //    restclient.getTopics(token)
                        val thread = Thread(Runnable {
                            topicListModel = restclient.getTopics(token)!!
                            Log.d("jn", topicListModel.toString())
                        })
                        thread.start()


                    }
                    FirebaseMessaging.getInstance()
                        .subscribeToTopic("all")
                }


            }

    }

}
