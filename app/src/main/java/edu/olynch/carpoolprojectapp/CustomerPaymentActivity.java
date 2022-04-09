package edu.olynch.carpoolprojectapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomerPaymentActivity extends AppCompatActivity {

    Button mPayDriver, mContinue;
    ImageButton mPayWithPayPal, mPayWithRevolut;
    String SECRET_KEY = "sk_test_51Kk3pyItK01kKtlB6VZop8XtamdUAaoffRyxB7MKlbVXrlvfwtOrYbzMJOjXbLJSAb6UHVLIcg0jHdBIS89TqdvV00q79LLCIi";
    String PUBLISH_KEY = "pk_test_51Kk3pyItK01kKtlBhb2Iu3RwOSLXYLzAWdj5xO04lNP4aZqpPynJGq0BIkOKV5872vleq2KuQVJGfKsuOooVDCB300iP9NYCf3";
    PaymentSheet paymentSheet;

    String customerId;
    String EphericalKey;
    String ClientSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_payment);

        mPayDriver = findViewById(R.id.btnPayDriver);
        mPayWithPayPal = findViewById(R.id.btnPayWithPayPal);
        mPayWithRevolut = findViewById(R.id.btnPayWithRevolut);
        mContinue = findViewById(R.id.btnContinueToRating);

        PaymentConfiguration.init(this, PUBLISH_KEY);

        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {
        onPaymentResult(paymentSheetResult);
        });

        mPayDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentFlow();
            }
        });

        mPayWithPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.paypal.android.p2pmobile");
                startActivity(intent);
            }
        });

        mPayWithRevolut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.revolut.revolut");
                startActivity(intent);
            }
        });

        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerPaymentActivity.this, RateDriverActivity.class);
                startActivity(intent);
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            customerId = object.getString("id");
                            //Toast.makeText(CustomerPaymentActivity.this, customerId, Toast.LENGTH_SHORT).show();

                            getEphericalKey(customerId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SECRET_KEY);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CustomerPaymentActivity.this);
        requestQueue.add(stringRequest);
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if(paymentSheetResult instanceof PaymentSheetResult.Completed){
            Toast.makeText(this, "Payment Complete", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEphericalKey(String customerId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            EphericalKey = object.getString("id");
                            //Toast.makeText(CustomerPaymentActivity.this, EphericalKey, Toast.LENGTH_SHORT).show();

                            getClientSecret(customerId, EphericalKey);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SECRET_KEY);
                headers.put("Stripe-Version", "2020-08-27");
                return headers;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CustomerPaymentActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getClientSecret(String customerId, String ephericalKey) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            ClientSecret = object.getString("client_secret");
                            //Toast.makeText(CustomerPaymentActivity.this, ClientSecret, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SECRET_KEY);
                return headers;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerId);
                params.put("amount", "10" + "00");
                params.put("currency", "eur");
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CustomerPaymentActivity.this);
        requestQueue.add(stringRequest);
    }

    private void PaymentFlow() {
        paymentSheet.presentWithPaymentIntent(
                ClientSecret, new PaymentSheet.Configuration("Dropoff",
                        new PaymentSheet.CustomerConfiguration(
                                customerId,
                                EphericalKey
                        ))
        );
    }
}