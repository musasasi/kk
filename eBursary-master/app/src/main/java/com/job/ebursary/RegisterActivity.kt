package com.job.ebursary


import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.job.ebursary.commoners.Tools
import kotlinx.android.synthetic.main.activity_signup.*

class RegisterActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, RegisterActivity::class.java)
    }

    private var parent_view: View? = null

    private var auth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        parent_view = findViewById(android.R.id.content)

        Tools.setSystemBarColor(this, android.R.color.white)
        Tools.setSystemBarLight(this)


    }

    fun toMainActivity(v:View){
        register()
    }

    private fun register() {
        val progressBar = ProgressDialog(this)
        progressBar.setMessage("Please wait...")
        progressBar.setTitle("Creating account")
        progressBar.show()


        val email = register_email.text.toString()
        val password = register_password.text.toString()
        val name = register_name.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    progressBar.dismiss()

                    Toast.makeText(this@RegisterActivity,"Successful",Toast.LENGTH_SHORT).show()

                    val user = auth.currentUser

                    val userMap = hashMapOf(
                        "email" to email,
                        "name" to name
                    )

                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                        .build()

                    user?.updateProfile(profileUpdates)

                    firestore.collection("Users").document(user!!.uid).set(userMap)

                    startActivity(LoginActivity.newIntent(this))
                    finish()

                } else {
                    // If sign in fails, display a message to the user.

                    progressBar.setTitle("Logging failed")
                    Handler().postDelayed({progressBar.dismiss()},1500)

                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
    }

}
