package com.taorika.appsurvey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    Button btn_logout;
    TextView txt_id, txt_username, txt_appbar;
    String id, username;
    SharedPreferences sharedpreferences;

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.biru_status_bar));

//        txt_id = (TextView) findViewById(R.id.textViewId);
//        txt_username = (TextView) findViewById(R.id.textViewUsername);
        txt_appbar = (TextView) findViewById(R.id.txt_appbar);


        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        id = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);

//        txt_id.setText("ID : " + id);
//        txt_username.setText("USERNAME : " + username);
        txt_appbar.setText(username);

        ImageView img = (ImageView) findViewById(R.id.buttonLogout);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(LoginActivity.session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_USERNAME, null);
                editor.commit();

                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }


}
