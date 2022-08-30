package com.taorika.appsurvey.mitra;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.taorika.appsurvey.LoginActivity;
import com.taorika.appsurvey.R;

public class SettingActivity extends AppCompatActivity {
    Button btn_logout;
    TextView txt_id, txt_username, txt_appbar;
    EditText eT_Username, eT_Nama, eT_Password;
    String id, username, kd_user, nama;
    SharedPreferences sharedpreferences;

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_KODE_USER = "kd_user";
    public static final String TAG_NAMA = "nama";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.biru_status_bar));

//        txt_id = (TextView) findViewById(R.id.textViewId);
//        txt_username = (TextView) findViewById(R.id.textViewUsername);
        txt_appbar = (TextView) findViewById(R.id.txt_appbar);
        eT_Username = (EditText) findViewById(R.id.eT_Username);
        eT_Nama = (EditText) findViewById(R.id.eT_Nama);


        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        id = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);
        kd_user = getIntent().getStringExtra(TAG_KODE_USER);
        nama = getIntent().getStringExtra(TAG_NAMA);

        eT_Username.setText(username);
        eT_Nama.setText(nama);
//        txt_username.setText("USERNAME : " + username);
//        txt_appbar.setText(username);
        ImageView img = (ImageView) findViewById(R.id.buttonLogout);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(LoginActivity.session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_USERNAME, null);
                editor.putString(TAG_KODE_USER, null);
                editor.commit();

                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
