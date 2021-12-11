package com.example.a18058581_nguyenthanhnghia_ad_todoapp

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.a18058581_nguyenthanhnghia_ad_todoapp.Extensions.toast
import com.example.a18058581_nguyenthanhnghia_ad_todoapp.FirebaseUtils.firebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_todoapp.*
import kotlinx.android.synthetic.main.add_new_task.*
import kotlinx.android.synthetic.main.add_new_task.view.*

class ToDoAppActivity: AppCompatActivity()  {
    private lateinit var databaseReference: DatabaseReference
    private val arrayList = ArrayList<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todoapp)



        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        fac_recyclerview.setOnClickListener {

            val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_new_task,null)
            var mBuilder = AlertDialog.Builder(this@ToDoAppActivity)
            mBuilder.setView(mDialogView)

            val mAlertDialog = mBuilder.show()
            mAlertDialog.btnSave.setOnClickListener {
                mAlertDialog.dismiss()
                val work = mDialogView.etWork.text.toString()
                val status = "Not Complete"
                val user = User(work,status)
                arrayList.add(user)
                listView.adapter = ListAdapter(this,R.layout.item_work,arrayList)
                AddRealTimeDataBase()
            }
            mDialogView.btnCancel.setOnClickListener {
                mAlertDialog.dismiss()
            }


//            val user = User("abc","test")
//            if (uid!=null){
//                databaseReference.child(uid).setValue(user).addOnCompleteListener {
//                    if (it.isSuccessful){
//                        toast("Successfully")
//                    }else{
//                        toast("Failed")
//                    }
//                }
//            }
        }
    }
    fun AddRealTimeDataBase(){
        val uid = firebaseAuth.currentUser?.uid

            if (uid!=null){
                databaseReference.child(uid).setValue(arrayList).addOnCompleteListener {
                    if (it.isSuccessful){
                        toast("Successfully")
                    }else{
                        toast("Failed")
                    }
                }
            }
    }
}