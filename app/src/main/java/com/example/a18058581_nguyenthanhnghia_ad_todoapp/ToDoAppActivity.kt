package com.example.a18058581_nguyenthanhnghia_ad_todoapp

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a18058581_nguyenthanhnghia_ad_todoapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*

class ToDoAppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private val arrayList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        auth = FirebaseAuth.getInstance()
        var uid = auth.currentUser?.uid

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")



//        fab.setOnClickListener {
//            val work = edit_work.text.toString()
//            val status = "Not Complete"
//            val user = User(work, status)
//            if (uid != null) {
//
//                databaseReference.child(uid).setValue(user)
//                arrayList.add(work + "      " + status)
//                listView.adapter =
//                    ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList)
//                edit_work.setText("")
//            }
//        }




        listView.clearFocus()

        fab.setOnClickListener {


            val work = edit_work.text.toString()
            val status = "Not Complete"



            val user = User(work,status)

            arrayList.add(work+status)
            listView.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList)
            if (uid != null) {

                databaseReference.child(uid).setValue(user).addOnCompleteListener {
                    if (it.isSuccessful) {

                        Toast.makeText( this@ToDoAppActivity, "Successfullly", Toast.LENGTH_SHORT ).show()

                    } else {

                        Toast.makeText( this@ToDoAppActivity, "Failed to update profile", Toast.LENGTH_SHORT ).show()
                    }
                }
            }

//



        }


    }
}