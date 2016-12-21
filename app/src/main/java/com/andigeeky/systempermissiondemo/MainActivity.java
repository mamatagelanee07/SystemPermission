package com.andigeeky.systempermissiondemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btnPermission;
    private TextView txtPermissionStatus;
    public static final int REQUEST_PERMISSION = 101;
    private String msgGranted = "READ_CONTACTS permission is granted!",
            msgDenied = "READ_CONTACTS permission is denied!",
            msgExplanation = "App needs to use contact for sharing a data. Please grant READ_CONTACT " +
                    "permission.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPermission = (Button) findViewById(R.id.btn_ask_permission);
        txtPermissionStatus = (TextView) findViewById(R.id.txt_permission_status);
        btnPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If Android do not have granted permission then app will ask for
                // read contact permission.
                if (!checkPermission()) {
                    txtPermissionStatus.setText(msgExplanation);
                    requestPermission();
                } else {
                    // Read contact permission is already granted! Yey!!
                    txtPermissionStatus.setText(msgGranted);
                }
            }
        });
    }

    /**
     * Check if read contact permission is already granted by Android System or not.
     *
     * @return true if permission is granted or false.
     */
    private boolean checkPermission() {
        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CONTACTS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    /**
     * Request a read contact permission to the user.
     */
    private void requestPermission() {

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.READ_CONTACTS)) {
            // If a user keeps trying to use functionality that requires a permission,
            // but keeps turning down the permission request, You can show an explanation dialog
            // here that why your app needs permission.
            txtPermissionStatus.setText(msgExplanation);
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setMessage(msgExplanation)
                    .setTitle("Read contact permission")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.READ_CONTACTS},
                                    REQUEST_PERMISSION);
                        }
                    }).create();
            alertDialog.show();
        } else {
            // REQUEST_PERMISSION is an app-defined int constant.
            // The callback method #onRequestPermissionsResult() gets the result of the request.
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Handle the permissions request response
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    txtPermissionStatus.setText(msgGranted);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    txtPermissionStatus.setText(msgDenied);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
