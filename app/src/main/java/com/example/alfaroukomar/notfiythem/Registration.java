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
import android.widget.RadioButton;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText name;
    EditText Password;
    EditText CPassword;
    EditText Email;
    EditText Phone;
    EditText Day;
    RadioButton [] radioButton = new RadioButton[8];
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Firebase.setAndroidContext(this);
        firebaseAuth = FirebaseAuth.getInstance();
        name =(EditText)findViewById(R.id.name);
        Email=(EditText)findViewById(R.id.Email2);
        Password=(EditText)findViewById(R.id.Password2);
        CPassword=(EditText)findViewById(R.id.Cpassword);
        Phone=(EditText)findViewById(R.id.Phone);
        Day=(EditText)findViewById(R.id.Day);
        radioButton[0] =(RadioButton)findViewById(R.id.Aplus);
        radioButton[1] =(RadioButton)findViewById(R.id.Bplus);
        radioButton[2] =(RadioButton)findViewById(R.id.Oplus);
        radioButton[3] =(RadioButton)findViewById(R.id.ABNeg);
        radioButton[4] =(RadioButton)findViewById(R.id.BNeg);
        radioButton[5] =(RadioButton)findViewById(R.id.ABNeg);
        radioButton[6] =(RadioButton)findViewById(R.id.ABplus);
        radioButton[7] =(RadioButton)findViewById(R.id.ONeg);





    }
    SharedPreferences.Editor editor;
    public void signup(View view) {
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        String email=  Email.getText().toString();
        String pass=  Password.getText().toString();
        String Cpass=  CPassword.getText().toString();
        int c=0;


        if (!TextUtils.isEmpty(email)&&(pass.equals(Cpass))&&!TextUtils.isEmpty(pass)) {

            firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful())
                    {
                        Toast.makeText(Registration.this, "Something Wrong in your mail || in your connection", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        editor.putString("Email", Email.getText().toString());
                        editor.putString("Password",Password.getText().toString());
                        editor.putString("name",name.getText().toString());
                        editor.putString("phone",Phone.getText().toString());
                        editor.putString("Day",Day.getText().toString());
                         for ( int i = 0 ; i<radioButton.length;i++)
                         {
                             if (radioButton[i].isChecked())
                             {
                                 editor.putString("Type",radioButton[i].getText().toString());
                                 editor.putBoolean("Check",true);
                                 editor.commit();
                                 Intent n = new Intent(Registration.this,News.class);
                                 startActivity(n);

                                 Toast.makeText(Registration.this, Email.getText().toString()+Password.getText().toString()+name.getText().toString()+Phone.getText().toString()+Day.getText().toString()+radioButton[i].getText().toString(), Toast.LENGTH_SHORT).show();

                                 break;
                             }
                         }



                    }

                }
            });

        }
        Toast.makeText(Registration.this,String.valueOf(c), Toast.LENGTH_SHORT).show();



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
}
