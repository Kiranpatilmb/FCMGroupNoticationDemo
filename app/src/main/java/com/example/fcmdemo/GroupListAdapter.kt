package com.example.fcmdemo

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.fcmdemo.APIManager.Data
import com.example.fcmdemo.APIManager.Notification
import com.example.fcmdemo.APIManager.OperationModel
import com.example.fcmdemo.APIManager.RestClientPost
import com.google.firebase.messaging.FirebaseMessaging


class GroupListAdapter(private val mList: HashMap<String, String>) : RecyclerView.Adapter<GroupListAdapter.ViewHolder>() {
    private var isDone: Boolean=false

    //  var key=Array<String>();
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            holder.textView.setText(mList.keys.elementAt(position))
           holder.txt_value.setText(mList.values.elementAt(position))
           holder.btn_join.setOnClickListener {

               FirebaseMessaging.getInstance().token
                   .addOnCompleteListener { task ->
                       if (task.isSuccessful) {
                           if (task.result != null && !TextUtils.isEmpty(task.result)) {
                               val token: String = task.result!!
                               Log.d("token is", token);
                               FirebaseMessaging.getInstance().subscribeToTopic(holder.textView!!.text.toString())
                               val msg=holder.textView!!.text.toString()
                               Toast.makeText(it.context,
                                   "You are joined to $msg successfully!!", Toast.LENGTH_LONG).show();
                               isDone=true
                           }
                           if(isDone){
                               var operationModel=OperationModel()
                               var restClientPost=RestClientPost()
                               operationModel.priority = "high"
                               operationModel.condition =
                                   "\'" + holder.textView!!.text.toString() + "\'" + " in topics"
                               operationModel.notification = Notification()
                               operationModel.notification!!.title = "FCM Demo APP"
                               operationModel.notification!!.body =
                                   " user joined to the " + holder.textView!!.text.toString() + " now..."
                               operationModel.data = Data()
                               operationModel.data!!.customId = "2"
                               operationModel.data!!.badge = 1
                               operationModel.data!!.alert = "Alert"
                               restClientPost.postTopic(operationModel)
                               isDone=false

                           }
                       }
                   }


           }


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val txt_value: TextView = itemView.findViewById(R.id.txt_value)
        val btn_join: TextView = itemView.findViewById(R.id.btn_join)

    }

}
