package lorm.ipsum;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Call_activity extends AppCompatActivity {

    @BindView(R.id.edt_caller_id)
    EditText edtCallerID;
    @BindView(R.id.edt_dest_one)
    EditText edtDestOne;
    @BindView(R.id.edt_dest_two)
    EditText edtDestTwo;
    @BindView(R.id.edt_number)
    EditText edtNumber;
    @BindView(R.id.btn_make_call)
    Button btnMakeCall;
    @BindView(R.id.btn_log_out)Button btnLogOut;

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
            Toast.makeText(Call_activity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_activity);
        ButterKnife.bind(this);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                handleUncaughtException(thread, ex);
            }

        });


      //  edtCallerID.setText("79020000002");
       // edtDestOne.setText("79010000001");
       // edtDestTwo.setText("79020000002");
       // edtNumber.setText("2");

        btnMakeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCallerID.getText().toString().trim().length() != 0 &&
                        edtDestOne.getText().toString().trim().length() != 0 &&
                        edtDestTwo.getText().toString().trim().length() != 0 &&
                        edtNumber.getText().toString().trim().length() != 0) {
                    MakeCall();
                } else {
                    Toast.makeText(Call_activity.this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Call_activity.this,Avtorisation_activity.class));
                finish();
            }
        });


    }


    private void MakeCall() {
        String destOne = edtDestOne.getText().toString().trim();
        String destTwo = edtDestTwo.getText().toString().trim();
        String callerId = edtCallerID.getText().toString().trim();
        String number = edtNumber.getText().toString().trim();

        try {
            AndroidNetworking.get("https://rdx.narayana.im/billing/api/2.2/14b0ca8299/createCallback?destination1=" + destOne + "&destination2=" + destTwo + "&epitch=" + number + "&callerid=" + callerId)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String res = response.getString("result");
                                if (res.equals("true")) {
                                    Toast.makeText(Call_activity.this, "Success", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Call_activity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(Call_activity.this, String.valueOf(anError), Toast.LENGTH_SHORT).show();
                        }
                    });


        } catch (Exception e) {
            Log.i("TAG", String.valueOf(e));
        }


    }
}
