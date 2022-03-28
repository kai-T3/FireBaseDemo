package com.example.firebasedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebasedemo.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("myDB")

        if(auth.currentUser!=null){
            binding.tvDisplay.text = auth.currentUser!!.email
        }


        binding.btnRegister.setOnClickListener(){
            registerUser("tankl-wm19@student.tarc.edu.my", "123456")
        }

        binding.btnSignIn.setOnClickListener(){
            signIn("tankl-wm19@student.tarc.edu.my", "123456")
        }

        binding.btnInsert.setOnClickListener(){
            val newStud = Student("W223", "Lik", "RSF")
            addNewStudent(newStud)
        }

        binding.btnRead.setOnClickListener(){
            readData("W123")
        }

        binding.btnDelete.setOnClickListener(){
            deleteData("W123")
        }

        binding.btnSignout.setOnClickListener(){
            signOut()
        }



    }

    private fun deleteData(id: String) {
        database.child("Student").child(id).removeValue()
            .addOnSuccessListener {
                binding.tvDisplay.text = "Removed"
            }.addOnFailureListener{ e->
                binding.tvDisplay.text = e.message
            }
    }

    private fun readData(id:String) {
        database.child("Student").child(id).get()
            .addOnSuccessListener {rec->
                if(rec!=null){
                    binding.tvDisplay.text = rec.child("name").value.toString()
                }else{
                    binding.tvDisplay.text = "record not found"
                }

            }
            .addOnFailureListener {
                binding.tvDisplay.text = "record not found"
            }

    }

    private fun addNewStudent(newStud: Student) {
        database.child("Student").child(newStud.id).setValue(newStud)
            .addOnSuccessListener {
                binding.tvDisplay.text = "new Student added"
            }.addOnFailureListener{ e->
                binding.tvDisplay.text = e.message
            }
    }

    private fun signOut() {
        Firebase.auth.signOut()
        binding.tvDisplay.text = "Signed out"
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                binding.tvDisplay.text = email
            }
            .addOnFailureListener{ e->
                binding.tvDisplay.text = e.message
            }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                binding.tvDisplay.text = "New user: ${email}"
            }
            .addOnFailureListener{ e->
                binding.tvDisplay.text = e.message
            }
    }
}