package com.riact.ricart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.riact.ricart.utils.AppSingleton;
import com.riact.ricart.utils.Constants;
import com.riact.ricart.utils.RiactDbHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class SignUp extends AppCompatActivity {
    RiactDbHandler userDb=new RiactDbHandler(this);
    String resp;
    EditText email,password,confirmPassword,address,customername,phone;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        submit=(Button)findViewById(R.id.btn_login);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        confirmPassword=(EditText)findViewById(R.id.confirm_password);
        address=(EditText)findViewById(R.id.address);
        customername=(EditText)findViewById(R.id.name);
        phone=(EditText)findViewById(R.id.phone);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailTxt=email.getText().toString();
                String passwordTxt=password.getText().toString();
                String confirmPwdTxt=confirmPassword.getText().toString();
                String addresstxt=address.getText().toString();
                String nameTxt=customername.getText().toString();
                String phoneTxt=phone.getText().toString();
                if(emailTxt.equals("")||passwordTxt.equals("")||nameTxt.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter all data",Toast.LENGTH_LONG).show();
                }
                else {
                    if(isValidEmailAddress(emailTxt))
                    if (passwordTxt.equals(confirmPwdTxt)) {

                       String url=new String();
                        try {
                            url=Constants.webAddress+ "set_customers.php?cust_name="+URLEncoder.encode(nameTxt,"UTF-8")+"&cust_email="+emailTxt+"&cust_address="+URLEncoder.encode(addresstxt,"UTF-8")+"&cust_phone="+phoneTxt+"&user_password="+passwordTxt;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        System.out.println(url);
                        volleyStringRequst(url);

                    } else {
                        Toast.makeText(getApplicationContext(), "Password Mismatch", Toast.LENGTH_LONG).show();

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Enter valid mail id", Toast.LENGTH_LONG).show();

                    }
                }

            }
        });


    }

    public void  volleyStringRequst(String url){

        String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                resp= response.toString();
                progressDialog.hide();
                if(resp.equals("SUCCESS"))
                {
                    getUserRequest(Constants.webAddress+"get_customers.php?user_code="+email.getText().toString());


                }
                else {
                    Toast.makeText(getApplicationContext(),"Failed to Signup",Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                resp="Some error occured";
                progressDialog.hide();
                Toast.makeText(getApplicationContext(),resp,Toast.LENGTH_LONG).show();


            }
        });
        // Adding String request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);


    }
    public void getUserRequest(String url)
    {
        String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";



        StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                resp= response.toString();
                try{
                    JSONArray userData=new JSONArray(resp);
                    for (int i = 0; i < userData.length(); i++) {
                        JSONObject user = userData.getJSONObject(i);
                        String name=user.getString("cust_name");
                        String email=user.getString("cust_email");
                        String address=user.getString("cust_address");
                        String phone=user.getString("cust_phone");
                        userDb.addUsers(name,phone,address,email);
                        Constants.userData=(ArrayList<String>) userDb.getUser();
                        getPastOrders(Constants.webAddress+"get_orders_by_cust.php?cust_code="+email);

                    }



                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                resp="SignUp failed";
                Toast.makeText(getApplicationContext(),resp,Toast.LENGTH_LONG).show();


            }
        });
        // Adding String request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);

    }

    public void getPastOrders(String url)
    {
        String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";



        StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                Constants.pastOrders = response;
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                finish();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                resp="Failed to fetch data";
                Toast.makeText(getApplicationContext(),resp,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                finish();


            }
        });
        // Adding String request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);

    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }


}
