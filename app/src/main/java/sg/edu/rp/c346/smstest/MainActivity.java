package sg.edu.rp.c346.smstest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();


        btnSend = findViewById(R.id.buttonSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("5556", null,"Hello World", null,null);
            }
        });


    }

    private void checkPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int permissionRecvSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&
                permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }

        }
        public void onReceive(Context context, Intent intent) {
        // SMS messages are retrieved from the intent's extra using the key "pdus"
            Bundle bundle = intent.getExtras();
            try {
                if (bundle!=null) {
                    Object[] pdusObj = (Object[]) bundle.get("pdus");
                    SmsMessage currentMessage;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = bundle.getString("format");
                        currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[0], format);

                    } else {
                        currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[0]);
                    }
                    // Obtain the originating phone number (sender's number)
                    String senderNum = currentMessage.getDisplayOriginatingAddress();
                    // Obtain the message body
                    String message = currentMessage.getDisplayMessageBody();
                    // Display in Toast
                    Toast.makeText(context, "Sender Number: " + senderNum +
                            ", Message: " + message, Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                Log.e("SmsReceiver", "Error: " + e);
            }
        }
}
