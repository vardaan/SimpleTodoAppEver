package com.example.vardansharma.simplesttodoappever.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.annotation.MainThread
import android.support.v7.app.AppCompatActivity
import com.example.vardansharma.simplesttodoappever.R
import com.example.vardansharma.simplesttodoappever.ui.taskList.TaskListActivity
import com.example.vardansharma.simplesttodoappever.utils.toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.ResultCodes
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 2020
    private val GOOGLE_TOS_URL = "https://www.google.com/policies/terms/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firebase = FirebaseAuth.getInstance()
        if (firebase.currentUser != null) {
            launchMainScreen()
        } else {
            makeUserLogin()
        }
    }

    private fun launchMainScreen() {
        startActivity<TaskListActivity>()
        finish()
    }

    private fun makeUserLogin() {
        val emailProvider = AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER)
                .build()

        val gmailProvider = AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                .build()

        val providers = listOf<AuthUI.IdpConfig>(emailProvider, gmailProvider)

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(AuthUI.getDefaultTheme())
                        .setLogo(R.mipmap.ic_launcher_round)
                        .setProviders(providers)
                        .setTosUrl(GOOGLE_TOS_URL)
                        .setIsSmartLockEnabled(false)
                        .setAllowNewEmailAccounts(true)
                        .build(),
                RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data)
            return
        }
        toast(R.string.unknown_response)
    }

    @MainThread
    private fun handleSignInResponse(resultCode: Int, data: Intent) {
        val response = IdpResponse.fromResultIntent(data)
        when (resultCode) {
            ResultCodes.OK -> {
                launchMainScreen()
                finish()
                return
            }
            else -> when {
                response == null -> {
                    // User pressed back button
                    toast(R.string.sign_in_cancelled)
                    return
                }
                response.errorCode == ErrorCodes.NO_NETWORK -> {
                    toast(R.string.no_internet_connection)
                    return
                }
                response.errorCode == ErrorCodes.UNKNOWN_ERROR -> {
                    toast(R.string.unknown_error)
                    return
                }
                else -> {
                    toast(R.string.unknown_error)
                }
            }
        }
        toast(R.string.unknown_sign_in_response)
    }
}
