package com.example.a18058581_nguyenthanhnghia_ad_todoapp

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.a18058581_nguyenthanhnghia_ad_todoapp.Extensions.toast
import com.example.a18058581_nguyenthanhnghia_ad_todoapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.login.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private val arrayList = ArrayList<String>()

    lateinit var signInEmail: String
    lateinit var signInPassword: String
    lateinit var signInInputsArray: Array<EditText>

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        signInInputsArray = arrayOf(etMail, etPass)

        btnLogin.setOnClickListener{
            signInUser()
        }


//        auth = FirebaseAuth.getInstance()
//        var uid = auth.currentUser?.uid
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
//
//
//        listView.clearFocus()
//
//        fab.setOnClickListener {
//
//
//            val work = edit_work.text.toString()
//            val status = "Not Complete"
//
//
//
//            val user = User(work,status)
//
//            if (uid != null) {
//
//                databaseReference.child(uid).setValue(user).addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        arrayList.add(work+status)
//                        listView.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList)
//                        Toast.makeText( this@MainActivity, "Successfullly", Toast.LENGTH_SHORT ).show()
//
//                    } else {
//
//                        Toast.makeText( this@MainActivity, "Failed to update profile", Toast.LENGTH_SHORT ).show()
//                    }
//                }
//            }
//
////
//
//
//
//        }

    }

    private fun notEmpty(): Boolean = signInEmail.isNotEmpty() && signInPassword.isNotEmpty()

    private fun signInUser() {
        signInEmail = etMail.text.toString().trim()
        signInPassword = etPass.text.toString().trim()

        if (notEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword)
                .addOnCompleteListener { signIn ->
                    if (signIn.isSuccessful) {
                        startActivity(Intent(this, ToDoAppActivity::class.java))
                        toast("signed in successfully")
                        finish()
                    } else {
                        toast("sign in failed")
                    }
                }
        } else {
            signInInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        }
    }

}