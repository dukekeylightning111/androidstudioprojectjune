package com.example.myapplication;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactMeActivity extends AppCompatActivity {
    private Button contactViaPhone;
    private Customer customer;
    private Button contactViaEmail;
    private BroadcastReceiver broadcastReceiver;
    private EditText editTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_me);
        init();
        func_contact_via_email();
    }

    public void init() {
        Intent intent = getIntent();
        customer = (Customer) intent.getSerializableExtra("customer");
        contactViaEmail = findViewById(R.id.contactViaEmailBtn);
        contactViaPhone = findViewById(R.id.contactViaPhoneBtn);
        editTextMessage = findViewById(R.id.editTextMessage);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if (!isConnected) {
                    showNoDataConnectionDialog();
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    Toast.makeText(context, "משתמש בחיבור Wi-Fi", Toast.LENGTH_SHORT).show();
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    Toast.makeText(context, "משתמש בחיבור נתונים סלולרי", Toast.LENGTH_SHORT).show();
                }
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void showNoDataConnectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("אין קליטה")
                .setMessage("האם אתם מעוניינים להמשיך?")
                .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    public void func_contact_via_email() {
        String recipientEmail = "drukeramit90@gmail.com";
        String subject = "יצירת קשר - אפליקצית ספורט";
        String message = editTextMessage.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("שליחת דואר אלקטרוני")
                .setMessage("אשרו שליחת הודעת דואר אלקטרוני זה\n" + message)
                .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendEmail(recipientEmail, subject, message);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    public void sendEmail(String recipientEmail, String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + recipientEmail));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            String customerEmail = customer.getEmail();
            if (customerEmail != null && !customerEmail.isEmpty()) {
                Intent fallbackIntent = new Intent(Intent.ACTION_SENDTO);
                fallbackIntent.setData(Uri.parse("mailto:" + customerEmail));
                fallbackIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                fallbackIntent.putExtra(Intent.EXTRA_TEXT, message);

                if (fallbackIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(fallbackIntent);
                } else {
                    Toast.makeText(this, "No email client found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No email client found", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
