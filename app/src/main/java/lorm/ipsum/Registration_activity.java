package lorm.ipsum;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import lorm.ipsum.Model.User;

public class Registration_activity extends AppCompatActivity {

    @BindView(R.id.edt_login)
    EditText edtLogin;
    @BindView(R.id.edt_mail)
    EditText edtMail;
    @BindView(R.id.edt_pass)
    EditText edtPass;
    @BindView(R.id.btn_reg)
    Button btnReg;

    private DatabaseReference ref;

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
            Toast.makeText(Registration_activity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        ButterKnife.bind(this);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                handleUncaughtException(thread, ex);
            }

        });


        FirebaseApp.initializeApp(this);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("");

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = edtMail.getText().toString().trim();
                String login = edtLogin.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                if (mail.length() != 0 && login.length() != 0 && pass.length() != 0) {
                    writeNewUser(login, login, mail, pass);
                } else {
                    Toast.makeText(Registration_activity.this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void writeNewUser(String userId, String name, String email, String pass) {
        User user = new User(name, email, pass);

        ref.child("users").child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Registration_activity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registration_activity.this, "Error!!!", Toast.LENGTH_LONG).show();
            }
        });
    }


}
