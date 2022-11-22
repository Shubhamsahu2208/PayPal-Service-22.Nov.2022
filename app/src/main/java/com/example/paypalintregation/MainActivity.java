package com.example.paypalintregation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {


    public static final int PAYPAL_REQUEST_CODE = 123;
    public static final String clientID = "AcuNvcJrid_StWOIHOfCsowaSSf-79aTMH8rSJx_qnwAAW0-zhlK3f9_pFJwHcq1hK1mPhzbL_W88mdJ";
    public static final String USD = "USD";


    private EditText amountEdt;
    private TextView paymentTV;
    private Button payment;

    private String paymentAmount;

    public static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(clientID);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amountEdt = findViewById(R.id.et_amount);
        paymentTV = findViewById(R.id.tv_textView);
        payment = findViewById(R.id.btn_payment);


        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPayment();

            }
        });

    }

    private void getPayment() {

        String amount = amountEdt.getText().toString();

        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), USD, "Learn", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, PAYPAL_REQUEST_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE) {

            PaymentConfirmation config = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

            if (config != null) {

                try {

                    String paymentDetails = config.toJSONObject().toString(4);

                    JSONObject payObj = new JSONObject(paymentDetails);

                } catch (JSONException e) {

                    e.printStackTrace();

                    Log.e("Error", "Something Went Wrong");

                }

            }
        } else if (requestCode == Activity.RESULT_CANCELED) {

            Log.i("Error", "Something Went Wrong");


        } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {

            Log.i("Payment", "Invalid Payment");

        }


    }
}



