package com.taorika.appsurvey;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taorika.appsurvey.app.AppController;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.taorika.appsurvey.mitra.DashboardActivity;

import es.dmoral.toasty.Toasty;

/**
 * Created by taorika on 16/07/2022.
 */
public class LoginActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    Button btn_register, btn_login;
    EditText txt_username, txt_password;
    Intent intent;

    int success;
    ConnectivityManager conMgr;

    private String url = Server.URL + "login.php";

    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public final static String TAG_USERNAME = "username";
    public final static String TAG_ID = "id";
    public final static String TAG_KODE_USER = "kd_user";
    public final static String TAG_NAMA = "nama";

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, username, kd_user, nama;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.biru_status_bar_login));
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toasty.error(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG, true).show();
            }
        }

        btn_login = (Button) findViewById(R.id.btnLogin);
        //btn_register = (Button) findViewById(R.id.btnLogin);
        txt_username = (EditText) findViewById(R.id.eT_Username);
        txt_password = (EditText) findViewById(R.id.eT_Password);

        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        kd_user = sharedpreferences.getString(TAG_KODE_USER, null);
        nama = sharedpreferences.getString(TAG_NAMA, null);

        if (session) {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            intent.putExtra(TAG_ID, id);
            intent.putExtra(TAG_USERNAME, username);
            intent.putExtra(TAG_KODE_USER, kd_user);
            intent.putExtra(TAG_NAMA, nama);
            finish();
            startActivity(intent);
        }


        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String username = txt_username.getText().toString();
                String password = txt_password.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    txt_username.setError("Please enter your username");
                    txt_username.requestFocus();
                    Toasty.error(getApplicationContext(), "Please enter your username", Toast.LENGTH_LONG, true).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    txt_password.setError("Please enter your password");
                    txt_password.requestFocus();
                    Toasty.error(getApplicationContext(), "Please enter your password", Toast.LENGTH_LONG, true).show();
                    return;
                }
                // mengecek kolom yang kosong
                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        //Toast.makeText(getApplicationContext() ,"u: "+username+" p: "+password, Toast.LENGTH_LONG).show();
                        checkLogin(username, password);
                    } else {

                        Toasty.error(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG, true).show();

                    }
                } else {
                    // Prompt user to enter credentials
                    Toasty.error(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG, true).show();
                }
            }
        });

//        btn_register.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                intent = new Intent(LoginActivity.this, MainActivity.class);
//                finish();
//                startActivity(intent);
//            }
//        });

    }

    private void checkLogin(final String username, final String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in ...");
        showDialog();
        //Toast.makeText(getApplicationContext() ,"u: "+username+" p: "+password, Toast.LENGTH_LONG).show();


        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        String username = jObj.getString(TAG_USERNAME);
                        String id = jObj.getString(TAG_ID);
                        String kd_user = jObj.getString(TAG_KODE_USER);
                        String nama = jObj.getString(TAG_NAMA);

                        Log.e("Successfully Login!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        // menyimpan login ke session
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, true);
                        editor.putString(TAG_ID, id);
                        editor.putString(TAG_USERNAME, username);
                        editor.putString(TAG_KODE_USER, kd_user);
                        editor.putString(TAG_NAMA, nama);
                        editor.commit();

                        // Memanggil main activity
                        if (id.equals("1")) {
                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            intent.putExtra(TAG_ID, id);
                            intent.putExtra(TAG_USERNAME, username);
                            intent.putExtra(TAG_KODE_USER, kd_user);
                            intent.putExtra(TAG_NAMA, nama);
                            finish();
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            intent.putExtra(TAG_ID, id);
                            intent.putExtra(TAG_USERNAME, username);
                            intent.putExtra(TAG_KODE_USER, kd_user);
                            intent.putExtra(TAG_NAMA, nama);
                            finish();
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toasty.error(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG, true).show();
                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}