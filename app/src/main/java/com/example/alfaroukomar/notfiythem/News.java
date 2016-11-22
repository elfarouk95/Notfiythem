package com.example.alfaroukomar.notfiythem;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class News extends AppCompatActivity {

    Firebase firebase;
    ArrayList<Newsobject> newsobjects = new ArrayList<>();
    RecyclerView recyclerView;
    AdpterForRecy adaptero;
    DBhandler dp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Firebase.setAndroidContext(this);

        firebase = new Firebase("https://notify-them.firebaseio.com/");
        recyclerView = (RecyclerView) findViewById(R.id.rec);
        dp = new DBhandler(this);
        Intent service = new Intent(this,notify_them.class);
        startService(service);
        if (!isInternetAvailable()) {
            newsobjects = dp.getallwithnew();
            adaptero = new AdpterForRecy(this, convertarraylisttoarray(newsobjects));

            GridLayoutManager llm = new GridLayoutManager(this, 1);
            llm.setOrientation(GridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(adaptero);

        }

      firebase.addChildEventListener(new ChildEventListener() {
          @Override
          public void onChildAdded(DataSnapshot dataSnapshot, String s) {

              getupdatefromnet(dataSnapshot);
          c=c%100;
          }

          @Override
          public void onChildChanged(DataSnapshot dataSnapshot, String s) {

          }

          @Override
          public void onChildRemoved(DataSnapshot dataSnapshot) {

          }

          @Override
          public void onChildMoved(DataSnapshot dataSnapshot, String s) {

          }

          @Override
          public void onCancelled(FirebaseError firebaseError) {

          }
      });



    }

    int c=0;
    private void getupdatefromnet(DataSnapshot dt) {
        Newsobject m = new Newsobject();
        newsobjects.clear();

        for (DataSnapshot dataSnapshot : dt.getChildren())
       {


             m=dt.getValue(Newsobject.class);
             m.setID(String.valueOf(c));
            newsobjects.add(m);
            dp.insertnewdata(m);

         //Toast.makeText(News.this, String.valueOf(c)+m.getName(), Toast.LENGTH_SHORT).show();


        }

        if (newsobjects.size() > 0) {


            adaptero = new AdpterForRecy(this, convertarraylisttoarray(dp.getallwithnew()));

            GridLayoutManager llm = new GridLayoutManager(this, 1);
            llm.setOrientation(GridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(adaptero);
        }
    }

    public Newsobject[] convertarraylisttoarray(ArrayList<Newsobject> a) {
        Newsobject[] tmp = new Newsobject[a.size()];

        for (int i = 0; i < a.size(); i++) {
            tmp[i] = a.get(i);
        }

        return tmp;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case  R.id.Blood_type :displayInputDialog();
        }
        return super.onOptionsItemSelected(item);
    }
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    Newsobject n = new Newsobject();
    Dialog d;
    private void displayInputDialog() {
         d = new Dialog(this);
        d.setTitle("طلب فصيلة");
        d.setContentView(R.layout.dialog);
        final EditText nameEditTxt = (EditText) d.findViewById(R.id.typeEditText);
        final EditText newEditTxt = (EditText) d.findViewById(R.id.newEditText);
        Button saveBtn = (Button) d.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = nameEditTxt.getText().toString();
                type.toUpperCase();
                boolean flag =false;
                switch (type)
                {
                    case "A+":
                    case "B+":
                    case "O+":
                    case "AB+":
                    case "A-":
                    case "B-":
                    case "AB-":
                    case "O-": flag=true;break;
                    default: flag=false;break;


                }
                          if (flag) {
                              String news = newEditTxt.getText().toString();
                              SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                              String phone = prefs.getString("phone", "no phone number");
                              String name = prefs.getString("name", "no name");
                              n.setType(type);
                              n.setPhone(phone);
                              n.setName(name);
                              n.setOther(news);

                              firebase.push().setValue(n);
                              d.hide();
                          }
                else
                          {
                              Toast.makeText(News.this, "Error in Blood type", Toast.LENGTH_SHORT).show();
                          }



            }
        });
        d.show();
    }


}
