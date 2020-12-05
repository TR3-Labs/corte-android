package dev.skrilltrax.corte

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.ui.tooling.preview.Preview
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dev.skrilltrax.corte.ui.CorteTheme


class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      CorteTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
          Button(onClick = { signIn() }) {
            Text(text = "Sign In", modifier = Modifier.align(Alignment.CenterVertically))
          }
        }
      }
    }
  }

  @RequiresApi(Build.VERSION_CODES.M)
  fun signIn() {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestIdToken("1048424827536-7u4frmc4a57d2jru3e1bsnt0gqfdqjqb.apps.googleusercontent.com")
      .requestProfile()
      .requestEmail()
      .requestId()
      .build()

    val a = GoogleSignInOptionsExtension.GAMES

    val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    val account = GoogleSignIn.getLastSignedInAccount(this)
    if (account is GoogleSignInAccount) {
      Toast.makeText(this, account.idToken, Toast.LENGTH_LONG).show()
      Log.d("tag", account.idToken!!)
      copyAuthCode(account.idToken)
    } else {
      val signInIntent = mGoogleSignInClient.signInIntent
      startActivityForResult(signInIntent, 100)
    }
  }

  @RequiresApi(Build.VERSION_CODES.M)
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    Log.d("tag", "onActivityResult")
    if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
      val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
      try {
        val account = task.getResult(ApiException::class.java)
        account.
        Toast.makeText(this, account?.idToken, Toast.LENGTH_LONG).show()
        Log.d("tag", account?.idToken!!)
        copyAuthCode(account.idToken)
      } catch (e: ApiException) {
        Toast.makeText(this, "Auth failed", Toast.LENGTH_LONG).show()
      }
    }
  }

  @RequiresApi(Build.VERSION_CODES.M)
  fun copyAuthCode(data: String?) {
    val clipboardManager: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.setPrimaryClip(ClipData.newPlainText("auth", data))
  }
}

@Composable
fun Greeting(name: String) {
  Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  CorteTheme {
    Greeting("Android")
  }
}
