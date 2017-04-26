package com.randev.elegantmediaassignment.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.randev.elegantmediaassignment.R;

/**
 * Created by jse on 2/7/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkLoginStatus();

        callbackManager = CallbackManager.Factory.create();
        LoginButton btnLogin = (LoginButton) findViewById(R.id.btn_login);
        btnLogin.setReadPermissions("email", "user_friends");

        btnLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, loginResult.getAccessToken().toString());
                showFriendList();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "User cancelled the login");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e(TAG, "There was an error logging the user in", exception);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void checkLoginStatus() {
        if (AccessToken.getCurrentAccessToken() != null) {
            showFriendList();
        }
    }

    private void showFriendList() {
        Intent intent = new Intent(LoginActivity.this, FriendListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
