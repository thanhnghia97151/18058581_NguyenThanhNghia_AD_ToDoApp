package com.example.a18058581_nguyenthanhnghia_ad_todoapp

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a18058581_nguyenthanhnghia_ad_todoapp.Extensions.toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_recyclerview_todoapp.*
import kotlinx.android.synthetic.main.activity_todoapp.*
import kotlinx.android.synthetic.main.activity_todoapp.showDialog
import kotlinx.android.synthetic.main.add_new_task.*
import kotlinx.android.synthetic.main.add_new_task.view.*

class RecyclerToDoApp :AppCompatActivity() {

    lateinit var signInEmail: String
    lateinit var signInPassword: String
    lateinit var signInInputsArray: Array<EditText>

    private lateinit var databaseReference: DatabaseReference
    private val arrayList = ArrayList<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview_todoapp)
        signInUser()




        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        var adapter = RecyclerviewAdapter(arrayList)
        showDialog.setOnClickListener {

            val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_new_task, null)
            var mBuilder = AlertDialog.Builder(this@RecyclerToDoApp)
            mBuilder.setView(mDialogView)

            val mAlertDialog = mBuilder.show()
            mAlertDialog.btnSave.setOnClickListener {
                mAlertDialog.dismiss()
                val work = mDialogView.etWork.text.toString()
                val status = "Not Complete"
                val user = User(work, status)
                arrayList.add(user)
                rcvWork.adapter = adapter
                rcvWork.layoutManager = LinearLayoutManager(this)

                AddRealTimeDataBase()
            }
            mDialogView.btnCancel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
        adapter.ItemClickListener={position, checked, cardView ->
            cardView.setOnClickListener{
                if ((!cardView.isSelected)&&(arrayList[position].status.equals("Not Complete"))){
                    arrayList[position].checked="1"
                    arrayList[position].status="Complete"
                }
                else if((!cardView.isSelected)&&(arrayList[position].status.equals("Complete"))) {
                    arrayList[position].checked="0"
                    arrayList[position].status="Not Complete"
                }
                cardView.isSelected=!cardView.isSelected

            }

        }
        imgChecked.setOnClickListener {
            rcvWork.clearFocus()
            rcvWork.adapter= adapter
            rcvWork.layoutManager = LinearLayoutManager(this)
            AddRealTimeDataBase()
        }




    }






    fun AddRealTimeDataBase(){
        val uid = FirebaseUtils.firebaseAuth.currentUser?.uid

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
    fun itemclick(){


    }
    private fun notEmpty(): Boolean = signInEmail.isNotEmpty() && signInPassword.isNotEmpty()
    private fun signInUser() {
        signInEmail = "abc@gmail.com"
        signInPassword = "123456"

        if (notEmpty()) {
            FirebaseUtils.firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword)
                .addOnCompleteListener { signIn ->
                    if (signIn.isSuccessful) {

                        toast("signed in successfully")
                        
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