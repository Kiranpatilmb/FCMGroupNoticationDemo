package com.example.fcmdemo

import android.content.Intent

import android.content.BroadcastReceiver
import android.content.Context


class MessageReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val activityIntent = Intent(context, SubscribeUsersActivity::class.java)
        activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        var msg= intent!!.getStringExtra("msg")!!
        activityIntent.putExtra("msg",msg);
        context.startActivity(activityIntent)
    }
}