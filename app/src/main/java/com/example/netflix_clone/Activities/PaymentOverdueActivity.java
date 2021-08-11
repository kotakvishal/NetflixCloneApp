package com.example.netflix_clone.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.netflix_clone.MainScreens.MainScreenActivity;
import com.example.netflix_clone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymentOverdueActivity extends AppCompatActivity implements PaymentResultListener {
    private static final String TAG = SignInActivity.class.getName();
    TextView signIn;
Button payButton;
RadioButton radioBasic,radio,radioStandard,radioPremium;
String planName,planCost,planFormatCost,intentFirstName,
    intentLastName,intentUid,intentEmail,intentContactNumber;
Date today,validDate;
FirebaseAuth mAuth;
FirebaseFirestore mFirestore;
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_overdue);
        getSupportActionBar().hide();
        Intent intentForPaymentOverdue=getIntent();
        intentFirstName=intentForPaymentOverdue.getStringExtra("firstName");
        intentLastName=intentForPaymentOverdue.getStringExtra("lastName");
        intentUid=intentForPaymentOverdue.getStringExtra("uId");
        intentContactNumber=intentForPaymentOverdue.getStringExtra("contactNumber");
        signIn=findViewById(R.id.signinstepone);
        payButton=findViewById(R.id.payButton);
        radioBasic=findViewById(R.id.radiobuttonforbasic_pay_overdue);
        radioStandard=findViewById(R.id.radiobuttonforstandard_pay_overdue);
        radioPremium=findViewById(R.id.radiobuttonforpremium_pay_overdue);
        radioBasic.setOnCheckedChangeListener(new Radio_check());
        radioStandard.setOnCheckedChangeListener(new Radio_check());
        radioPremium.setOnCheckedChangeListener(new Radio_check());
        radioPremium.setChecked(true);
        mAuth=FirebaseAuth.getInstance();
        mFirestore=FirebaseFirestore.getInstance();
        Calendar calendar=Calendar.getInstance();
        today=calendar.getTime();
        calendar.add(Calendar.MONDAY,1);
        validDate=calendar.getTime();
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToSignInActivity=new Intent(PaymentOverdueActivity.this,SignInActivity.class);
                startActivity(intentToSignInActivity);
            }
        });
    }
    public void startPayment() {
        progressDialog=new ProgressDialog(PaymentOverdueActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Checkout checkout=new Checkout();
        final Activity activity=this;
        String name=intentFirstName+intentLastName;
        try
        {

            JSONObject options=new JSONObject();
            options.put("name",name);
            options.put("description","APP PAYMENT");
            options.put("currency","INR");
            String payment=planCost;
            double total=Double.parseDouble(payment);
            total=total*100;
            options.put("amount",total);
            options.put("prefill.email",intentEmail);
            options.put("prefill.contact",intentContactNumber);
            checkout.open(activity,options);

        }
        catch (Exception e)
        {
            Log.e(TAG,"Payment Failed!!!",e);
        }
    }
    @Override
    public void onPaymentSuccess(String s) {
        DocumentReference documentReference=mFirestore.collection("Users")
                .document(intentUid);
        Map<String,Object> user=new HashMap<>();
        user.put("Email",intentFirstName);
        user.put("First_Name",intentFirstName);
        user.put("Last_Name",intentLastName);
        user.put("Plan_Cost",planCost);
        user.put("Contact_Number",intentContactNumber);
        user.put("Valid_Date",validDate);
        documentReference.set(user).addOnSuccessListener(PaymentOverdueActivity.this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intentToMainScreen=new Intent(PaymentOverdueActivity.this, MainScreenActivity.class);
                startActivity(intentToMainScreen);
                progressDialog.cancel();
                finish();
            }
        }).addOnFailureListener(PaymentOverdueActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            if(e instanceof FirebaseNetworkException)
            {
                Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Values not stored",Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this,"Payment Failed",Toast.LENGTH_SHORT).show();
        progressDialog.cancel();
    }

    class Radio_check implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.isChecked()) {
                if (buttonView.getId() == R.id.radiobuttonforbasic_pay_overdue) {
                    planName = getString(R.string.basic);
                    planCost = getString(R.string.basicPrice);
                    planFormatCost = getString(R.string.basicPriceFormat);
                    radioStandard.setChecked(false);
                    radioPremium.setChecked(false);
                }
                if (buttonView.getId() == R.id.radiobuttonforstandard) {
                    planName = getString(R.string.standard);
                    planCost = getString(R.string.standardPrice);
                    planFormatCost = getString(R.string.standardPriceFormat);
                    radioBasic.setChecked(false);
                    radioPremium.setChecked(false);
                }
                if (buttonView.getId() == R.id.radiobuttonforpremium) {
                    planName = getString(R.string.premium);
                    planCost = getString(R.string.premiumPrice);
                    planFormatCost = getString(R.string.premiumPriceFormat);
                    radioStandard.setChecked(false);
                    radioBasic.setChecked(false);
                }
            }
        }
    }

}