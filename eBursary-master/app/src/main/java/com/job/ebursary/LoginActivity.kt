package com.job.ebursary


import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.job.ebursary.commoners.Tools
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, LoginActivity::class.java)
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser != null) {
            startActivity(MainActivity.newIntent(this))
            finish()
        }
    }

    private var auth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()

    private var parent_view: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        parent_view = findViewById(android.R.id.content)

        Tools.setSystemBarColor(this, android.R.color.white)
        Tools.setSystemBarLight(this)

        (findViewById(R.id.forgot_password) as View).setOnClickListener {
            Snackbar.make(
                parent_view!!,
                "Sorry mate! create a new account.",
                Snackbar.LENGTH_SHORT
            ).show()
        }
        (findViewById(R.id.sign_up) as View).setOnClickListener {

            startActivity(RegisterActivity.newIntent(this@LoginActivity))
        }
    }

    fun login() {
        val progressBar = ProgressDialog(this)
        progressBar.setMessage("Please wait...")
        progressBar.setTitle("Logging in")
        progressBar.show()


        val email = login_email.text.toString()
        val password = login_password.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    progressBar.dismiss()

                    val user = auth.currentUser

                    val userMap = mapOf<String, Any>()
                    userMap.plus(Pair("email",email))

                    firestore.collection("Users").document(user!!.uid).set(userMap)

                    startActivity(MainActivity.newIntent(this))
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

    fun toRegisterActivity(v: View) {
        startActivity(RegisterActivity.newIntent(this))
    }

    fun toMainActivity(v: View) {
        login()
    }
}
