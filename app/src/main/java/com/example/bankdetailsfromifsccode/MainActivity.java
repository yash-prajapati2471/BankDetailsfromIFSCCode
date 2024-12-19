package com.example.bankdetailsfromifsccode;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    // creating variables for edit text
    // and our text views.
    private EditText ifscCodeEdt;
    private TextView bankDetailsTV;

    // creating a variable for
    // our ifsc code string.
    String ifscCode;

    // creating a variable for request queue.
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // initializing our variables.
        ifscCodeEdt = findViewById(R.id.idedtIfscCode);
        Button getBankDetailsBtn = findViewById(R.id.idBtnGetBankDetails);
        bankDetailsTV = findViewById(R.id.idTVBankDetails);

        // initializing our request queue variable with request queue
        // and passing our context to it.
        mRequestQueue = Volley.newRequestQueue(MainActivity.this);

        // initializing on click listener for our button.
        getBankDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting string  from edittext.
                ifscCode = ifscCodeEdt.getText().toString();

                // validating if the edit text
                // is empty or not.
                if (TextUtils.isEmpty(ifscCode)) {
                    // displaying a toast message if the text field is empty
                    Toast.makeText(MainActivity.this, "Please enter valid IFSC code", Toast.LENGTH_SHORT).show();
                } else {
                    // calling a method to display
                    // our ifsc code details.
                    getDataFromIFSCCode(ifscCode);
                }
            }
        });
    }

    private void getDataFromIFSCCode(String ifscCode) {

        // clearing our cache of request queue.
        mRequestQueue.getCache().clear();

        // below is the url from where we will be getting
        // our response in the json format.
        String url = "https://ifsc.razorpay.com/" + ifscCode;

        // below line is use to initialize our request queue.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        // creating a json object request for our API.
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // this method is used to get
                // the response from the API.
                try {
                    // if the status is successful we are
                    // extracting data from JSON file
                    String state = response.optString("STATE");
                    String bankName = response.optString("BANK");
                    String branch = response.optString("BRANCH");
                    String address = response.optString("ADDRESS");
                    String contact = response.optString("CONTACT");
                    String micrcode = response.optString("MICR");
                    String city = response.optString("CITY");
                    // after extracting this data we are displaying
                    // that data in our text view.
                    bankDetailsTV.setText("Bank Name : " + bankName + "\n\nBranch : " + branch
                            + "\n\nAddress : " + address + "\n\nMICR Code : " + micrcode
                            + "\n\nCity : " + city + "\n\nState : " + state + "\n\nContact : " + contact);
                } catch (Exception e) {
                    // if we get any error while loading data
                    // we are setting our text as invalid IFSC code.
                    e.printStackTrace();
                    bankDetailsTV.setText("Invalid IFSC Code");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // if we get any error while loading json
                // data we are setting our text to invalid IFSC code.
                bankDetailsTV.setText("Invalid IFSC Code");
            }
        });
        // below line is use for adding object
        // request to our request queue.
        queue.add(objectRequest);
    }
}