package lorm.ipsum;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lorm.ipsum.Model.User;

public class Avtorisation_activity extends AppCompatActivity {

    @BindView(R.id.edt_login)
    EditText edtLogin;
    @BindView(R.id.edt_pass)
    EditText edtPass;
    @BindView(R.id.btn_enter)
    Button btnEnter;
    @BindView(R.id.btn_reg)
    Button btnReg;
    private DatabaseReference refUser;
    private DatabaseReference refPass;
    private FirebaseDatabase database;

    public void handleUncaughtException(Thread thread, Throwable e) {
        String stackTrace = Log.getStackTraceString(e);
        String message = e.getMessage();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"andrij2198@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "App Crash log file");
        intent.putExtra(Intent.EXTRA_TEXT, stackTrace);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        try {
            startActivity(Intent.createChooser(intent, "Отправте ошибку по електронной почте..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Avtorisation_activity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtorisation_activity);
        ButterKnife.bind(this);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                handleUncaughtException(thread, ex);
            }

        });

        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();


        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidationUser();
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Avtorisation_activity.this,Registration_activity.class));
                finish();

            }
        });


    }



    private void ValidationUser() {
        btnEnter.setEnabled(false);
      //  edtPass.setText("test_pass2");
       // edtLogin.setText("test_name2");
        final String login = edtLogin.getText().toString().trim();
        final String pass = edtPass.getText().toString().trim();
        refUser = database.getReference();   //.child("test3");
        refPass = database.getReference().child("pass");

        Query query = refUser.child("users").orderByChild("username");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user1 = userSnapshot.getValue(User.class);
                        if (user1.getPass()!=null){
                            if (user1.getPass().equals(pass)&&user1.getUsername().equals(login)) {
                                Toast.makeText(Avtorisation_activity.this, "Success login", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Avtorisation_activity.this,Call_activity.class));
                                finish();
                            } else {

                            }
                        }

                    }
                }else {
                    Toast.makeText(Avtorisation_activity.this, "Please registration", Toast.LENGTH_SHORT).show();
                    btnEnter.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Avtorisation_activity.this, "Please registration", Toast.LENGTH_SHORT).show();
                btnEnter.setEnabled(true);
            }
        });


 /*
        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if (data.child(pass).exists()) {
                        Toast.makeText(Avtorisation_activity.this,"Success login",Toast.LENGTH_SHORT).show();
                        //do ur stuff
                    } else {
                        //do something if not exists
                        Toast.makeText(Avtorisation_activity.this,"Please registration",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Avtorisation_activity.this,"Error",Toast.LENGTH_SHORT).show();


            }
        });



        refUser.orderByChild("username").equalTo(login).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //bus number exists in Database
                    Toast.makeText(Avtorisation_activity.this,"Success login",Toast.LENGTH_SHORT).show();
                } else {
                    //bus number doesn't exists.
                    Toast.makeText(Avtorisation_activity.this,"Please registration",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Avtorisation_activity.this,"Error",Toast.LENGTH_SHORT).show();

            }
        });

        refPass.orderByChild("pass").equalTo(pass).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //bus number exists in Database
                    Toast.makeText(Avtorisation_activity.this,"Success pass",Toast.LENGTH_SHORT).show();
                } else {
                    //bus number doesn't exists.
                    Toast.makeText(Avtorisation_activity.this,"Please registration",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Avtorisation_activity.this,"Error",Toast.LENGTH_SHORT).show();

            }
        });
        */

    }
}
