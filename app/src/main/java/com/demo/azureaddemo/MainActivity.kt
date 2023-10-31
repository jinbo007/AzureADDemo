package com.demo.azureaddemo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.demo.azureaddemo.ui.theme.AzureADDemoTheme
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.IMultipleAccountPublicClientApplication
import com.microsoft.identity.client.IPublicClientApplication.IMultipleAccountApplicationCreatedListener
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.exception.MsalClientException
import com.microsoft.identity.client.exception.MsalException
import com.microsoft.identity.client.exception.MsalServiceException
import java.io.File
import kotlin.math.log


class MainActivity : ComponentActivity() {
    var mMultipleAccountApp: IMultipleAccountPublicClientApplication? = null
    lateinit var tvContent: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        findViewById<Button>(R.id.btn_create_application).setOnClickListener {
            createMultipleAccountApplication()
        }
        tvContent = findViewById(R.id.tv_content)

        findViewById<Button>(R.id.btn_request_token).setOnClickListener {
            createMultipleAccountApplication()
//            requestToken()
        }
    }

    private fun createMultipleAccountApplication() {
        val scopes = arrayOf("User.Read")
        val mFirstAccount: IAccount? = null
        val config = File(
            "/msal_config.json"
        )

        PublicClientApplication.createMultipleAccountPublicClientApplication(
            applicationContext,
            R.raw.corporate,
            object : IMultipleAccountApplicationCreatedListener {
                override fun onCreated(application: IMultipleAccountPublicClientApplication) {
                    mMultipleAccountApp = application
//                    val account: IAccount = application.getAccount(mFirstAccount?.getId()?:"")
//                    if (account != null) {
//                        val newScopes = arrayOf("Calendars.Read")
//                        val authority: String =
//                            application.configuration.defaultAuthority.authorityURL
//                                .toString()
//
//                        val result: IAuthenticationResult =
//                            application.acquireTokenSilent(newScopes, account, authority)
//                    }
                    requestToken()
                }

                override fun onError(exception: MsalException) {
                    Log.d("TAG", "onError: ${exception.message}")
                    //Log Exception Here
                    exception.message.showToast()
                }
            })


    }


    private fun requestToken() {
        val scopes = arrayOf("User.Read")
        mMultipleAccountApp?.acquireToken(this, scopes, object : AuthenticationCallback {
            override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                val accessToken = authenticationResult?.accessToken;
                // Record account used to acquire token
                val mFirstAccount = authenticationResult?.account;
                ("success token: \n" + authenticationResult?.accessToken).showToast()

            }

            override fun onError(exception: MsalException?) {
                if (exception is MsalClientException) {
                    //And exception from the client (MSAL)
                } else if (exception is MsalServiceException) {
                    //An exception from the server
                }
                exception?.message?.showToast()
            }

            override fun onCancel() {
                "user cancle".showToast()
            }

        });


    }


    private fun String?.showToast() {
        runOnUiThread {
            Toast.makeText(this@MainActivity, this ?: "", Toast.LENGTH_LONG).show()
            tvContent.text = this
        }
    }

    private fun accessTokenSilence() {
//        val account: IAccount = mMultipleAccountApp?.getAccount("mFirstAccount.getId()")
//
//        if (account != null) {
//            //Now that we know the account is still present in the local cache or not the device (broker authentication)
//
//            //Request token silently
//            val newScopes = arrayOf("Calendars.Read")
//            val authority: String =
//                mMultipleAccountApp.getConfiguration().getDefaultAuthority().getAuthorityURL()
//                    .toString()
//
//            //Use default authority to request token from pass null
//            val result: IAuthenticationResult =
//                mMultipleAccountApp.acquireTokenSilent(newScopes, account, authority)
//        }

    }


}




