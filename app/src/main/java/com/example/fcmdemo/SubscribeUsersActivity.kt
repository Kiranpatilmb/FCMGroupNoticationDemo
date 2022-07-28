package com.example.fcmdemo

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fcmdemo.APIManager.OperationModel
import com.example.fcmdemo.APIManager.RestClient
import com.google.firebase.messaging.FirebaseMessaging
import android.view.ViewGroup

import android.widget.LinearLayout

class SubscribeUsersActivity : AppCompatActivity() {
    private var btnUnScribe: Button? = null
    private var btnSubscribe: Button? = null
    private var btnNotify: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subscribe_user_layout)
        val msg= intent!!.getStringExtra("msg")!!
        Log.d("msg string is", msg)
        btnSubscribe = findViewById<Button>(R.id.btn_subcribe);
        btnUnScribe =findViewById<Button>(R.id.btn_unsubcribe);
        btnNotify = findViewById<Button>(R.id.btn_notify);
        initFCM(msg)
    }




    private fun initFCM(msg: String) {


        btnSubscribe?.setOnClickListener {
            FirebaseMessaging.getInstance().token
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result != null && !TextUtils.isEmpty(task.result)) {
                            val token: String = task.result!!
                            Log.d("token is", token);
                            FirebaseMessaging.getInstance().subscribeToTopic(msg)
                            Toast.makeText(
                                this,
                                "You are subscribed to $msg successfully!!",
                                Toast.LENGTH_LONG
                            ).show();
                            ;
                            Log.d("messssssage", msg);
                            var intent = Intent(this,MainActivity::class.java)
//                            intent.putExtra("isSubscribed",true)
//                            intent.putExtra("msg",msg)
                            startActivity(intent)
                        }

                    }

                }

        }

        btnUnScribe?.setOnClickListener {
            FirebaseMessaging.getInstance().token
                .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            FirebaseMessaging.getInstance()
                                .unsubscribeFromTopic(msg!!);
                            Toast.makeText(
                                this,
                                android.os.Build.MODEL+" unSubscribed to " + msg,
                                Toast.LENGTH_LONG
                            ).show();

                        }
                    }
                }

    }
}