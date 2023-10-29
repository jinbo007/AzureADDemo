package com.demo.azureaddemo

import android.R
import android.os.Bundle
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
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.IMultipleAccountPublicClientApplication
import com.microsoft.identity.client.IPublicClientApplication.IMultipleAccountApplicationCreatedListener
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.exception.MsalException
import java.io.File


class MainActivity : ComponentActivity() {
    var mMultipleAccountApp: IMultipleAccountPublicClientApplication? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AzureADDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    private fun createMultipleAccountApplication(){
        val scopes = arrayOf("User.Read")
        val mFirstAccount: IAccount? = null
        val config = File(
            "/msal_config.json")

        PublicClientApplication.createMultipleAccountPublicClientApplication(
            applicationContext,
            config,
            object : IMultipleAccountApplicationCreatedListener {
                override fun onCreated(application: IMultipleAccountPublicClientApplication) {
                    mMultipleAccountApp = application
                    val account: IAccount = application.getAccount(mFirstAccount?.getId()?:"")
                    if (account != null) {
                        val newScopes = arrayOf("Calendars.Read")
                        val authority: String =
                            application.configuration.defaultAuthority.authorityURL
                                .toString()

                        val result: IAuthenticationResult =
                            application.acquireTokenSilent(newScopes, account, authority)
                    }
                }

                override fun onError(exception: MsalException) {
                    //Log Exception Here
                }
            })


    }



    private fun accessTokenSilence(){
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

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Button(
            onClick = {
                // 在这里定义按钮的点击事件
                // 例如，你可以导航到另一个屏幕
                // navController.navigate("destination")
                createMultipleAccountApplication()
            },
            modifier = Modifier.fillMaxSize()
        ) {
            // 按钮上的文本
            Text(text = "点击事件")
        }
    }



    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        AzureADDemoTheme {
            Greeting("Android")
        }
    }
}




