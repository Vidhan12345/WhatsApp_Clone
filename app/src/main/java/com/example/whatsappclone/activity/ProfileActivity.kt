package com.example.whatsappclone.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.whatsappclone.MainActivity
import com.example.whatsappclone.R
import com.example.whatsappclone.databinding.ActivityOtpBinding
import com.example.whatsappclone.databinding.ActivityProfileBinding
import com.example.whatsappclone.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storage : FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var dialog: AlertDialog.Builder
    private lateinit var selectedImg : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dialog = AlertDialog.Builder(this)
            .setMessage("Updating Profile")
            .setCancelable(false)

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.userImg.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,1)
        }

        binding.continuebtn.setOnClickListener {
            if (binding.username.text!!.isEmpty()){
                Toast.makeText(this,"Please Enter your Name First",Toast.LENGTH_SHORT).show()
            }else{
                uploadInfo();
            }
        }
    }

    private fun uploadInfo() {
        val  user = UserModel(auth.uid.toString(),binding.username.text.toString(),auth.currentUser?.phoneNumber.toString())

        database.reference.child("Users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
    }

    fun OnActivityResult(requestCode : Int,resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode,resultCode,data)

        if (data!=null){
            if (data.data!=null){
                selectedImg = data.data!!

                binding.userImg.setImageURI(selectedImg)
            }
        }
    }
}