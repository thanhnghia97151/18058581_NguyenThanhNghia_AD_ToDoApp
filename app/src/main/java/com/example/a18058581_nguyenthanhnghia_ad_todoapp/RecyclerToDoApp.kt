package com.example.a18058581_nguyenthanhnghia_ad_todoapp

import android.graphics.Color
import android.os.Bundle
import android.os.FileUtils
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
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
import kotlinx.android.synthetic.main.update_new_task.*
import kotlinx.android.synthetic.main.update_new_task.view.*

class RecyclerToDoApp :AppCompatActivity() {

    lateinit var signInEmail: String
    lateinit var signInPassword: String
    lateinit var signInInputsArray: Array<EditText>

    private var arrayList = ArrayList<User>()
    private val check:IntArray= IntArray(10000)
    var count:Long =0
    private lateinit var databaseReference: DatabaseReference
    private var note=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        setContentView(R.layout.activity_recyclerview_todoapp)
        //
        reloadCheck()
        //
        signInUser()
        //
        loadData()
        //
        showDialog.setOnClickListener {
            showDialogAdd()
        }

        //
        imgChecked.setOnClickListener{
            var newList=arrayList
            for(item in check.indices){
                if (check[item]==1){
                    if (newList[item].checked=="0")
                        newList[item].checked="1"
                    else newList[item].checked="0"
                }
                check[item]=-1
            }
            reloadCheck()
            callbackRecyclerview(newList)
            loadRecyclerView()
            AddRealTimeDataBase()
        }
        //
        imgEdit.setOnClickListener{

            var dem=0
            for (item in check.indices){
                if (check[item]==1)
                    dem++
            }
            if (dem>1){
                toast("You chose many works!")
            }
            else{
                if (dem==0){
                    toast("Chosse work to update")
                }
                else{
                    for (item in check.indices){
                        if (check[item]==1){
                            note=item
                        }
                    }
                    if (note!=-1)
                        showDialogUpdate(note)
                    else toast("Chosse work to update!")

                }
            }

        }

        imgDelete.setOnClickListener{
            var newList=ArrayList<User>()
            for(item in arrayList.indices){
                if (check[item]!=1)
                    newList.add(arrayList[item])
                else check[item]==-1
            }

            arrayList=newList
            reloadCheck()
            callbackRecyclerview(arrayList)
            loadRecyclerView()
            AddRealTimeDataBase()
        }


    }
    private  fun showDialogUpdate(index:Int)
    {


        val mDialogView = LayoutInflater.from(this).inflate(R.layout.update_new_task, null)
        var mBuilder = AlertDialog.Builder(this@RecyclerToDoApp)
        mBuilder.setView(mDialogView)
        if(index!=-1)
            mDialogView.etWork_Update.setText(arrayList[index].work.toString())
        val mAlertDialog = mBuilder.show()
        mAlertDialog.btnSave_Update.setOnClickListener {
            mAlertDialog.dismiss()
            val check = arrayList[index].checked.toString()
            val work = mDialogView.etWork_Update.text.toString()
            val user = User(work,check)
            arrayList.set(index,user)

            reloadCheck()
            callbackRecyclerview(arrayList)
            loadRecyclerView()
            AddRealTimeDataBase()
        }
        mDialogView.btnCancel_Update.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }
    private fun showDialogAdd()
    {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_new_task, null)
        var mBuilder = AlertDialog.Builder(this@RecyclerToDoApp)
        mBuilder.setView(mDialogView)

        val mAlertDialog = mBuilder.show()
        mAlertDialog.btnSave.setOnClickListener {
            mAlertDialog.dismiss()
            val work = mDialogView.etWork.text.toString()
            val user = User(work)
            arrayList.add(user)

            reloadCheck()
            loadRecyclerView()
            AddRealTimeDataBase()
        }
        mDialogView.btnCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }
    private fun loadRecyclerView(){
        reloadCheck()
        var adapter = RecyclerviewAdapter(arrayList)
        rcvWork.adapter = adapter
        rcvWork.layoutManager = LinearLayoutManager(this)
        rcvWork.setHasFixedSize(true)
        adapter.ItemClickListener={position, checked, cardView ->
            cardView.setOnClickListener{

                cardView.isSelected=!cardView.isSelected
                if (cardView.isSelected){
                    check[position]=1

                }
                else{
                    check[position]=-1

                }
            }
        }
        adapter.updateList(arrayList)
    }



    private fun callbackRecyclerview(newList:ArrayList<User>)
    {
        reloadCheck()
        var adapter = CallBackAdapter(newList)
        rcvWork.adapter = adapter
        rcvWork.layoutManager = LinearLayoutManager(this)
        rcvWork.setHasFixedSize(true)
        adapter.ItemClickListener={position, checked, cardView ->
            cardView.setOnClickListener{
                cardView.isSelected=!cardView.isSelected
                if (cardView.isSelected){
                    check[position]=1
                }
                else{
                    check[position]=-1

                }
            }
        }
        adapter.updateList(newList)
    }


    fun AddRealTimeDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
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
    fun loadData(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Works")
        val uid = FirebaseUtils.firebaseAuth.currentUser?.uid

        if (uid!=null){
            databaseReference.child(uid).get().addOnSuccessListener {
                    count=it.childrenCount
                    for (i in count downTo 1){

                        var work = it.child("${count-i}").child("work").value.toString()
                        var checked = it.child("${count-i}").child("checked").value.toString()
                        var user = User(work,checked)
                        arrayList.add(user)
                    }
                loadRecyclerView()


            }

        }

    }
    fun reloadCheck(){
        for(i in 0..100)
            check[i]=-1
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