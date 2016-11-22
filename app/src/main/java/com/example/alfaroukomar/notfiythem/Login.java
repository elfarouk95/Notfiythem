package com.example.alfaroukomar.notfiythem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Firebase data ;
    FirebaseAuth log;
    EditText Email;
    EditText password;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        data = new Firebase("https://notify-them.firebaseio.com/");
        log = FirebaseAuth.getInstance();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
       if (prefs.getBoolean("Check",false)&&isInternetAvailable())
       {
           Toast.makeText(Login.this, "Online Mode", Toast.LENGTH_SHORT).show();
           login();
       }
       else if(!isInternetAvailable()&&prefs.getBoolean("Check",false))
       {
           Intent n = new Intent(Login.this,News.class);
           startActivity(n);
           Toast.makeText(Login.this, "Offline Mode", Toast.LENGTH_SHORT).show();
       }
        else
       {
           setContentView(R.layout.activity_login);
           Email=(EditText)findViewById(R.id.Email2);
           password=(EditText)findViewById(R.id.password);
       }







    }

    public boolean isInternetAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
    public void loginfunction(View view) {

        String name =Email.getText().toString();
        String pass = password.getText().toString();
        if ( TextUtils.isEmpty(name))
        {
            Toast.makeText(Login.this, "Email not found", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(pass))
        {
            Toast.makeText(Login.this, "Password not found", Toast.LENGTH_SHORT).show();

        }
     else {
            log.signInWithEmailAndPassword(name, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Login.this, "Log in ....", Toast.LENGTH_SHORT).show();
                        Intent n = new Intent(Login.this,News.class);
                        startActivity(n);

                    } else if (!task.isSuccessful()) {
                        Toast.makeText(Login.this, "something wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void login()
    {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String name =prefs.getString("Email","l");
        String pass =prefs.getString("Password","g");
        log.signInWithEmailAndPassword(name, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "Registration...", Toast.LENGTH_SHORT).show();
                    Intent n = new Intent(Login.this,News.class);
                    startActivity(n);

                } else if (!task.isSuccessful()) {
                    Toast.makeText(Login.this, "something wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void Risg(View view) {
        Intent n = new Intent(this,Registration.class);
        startActivity(n);

    }
}
