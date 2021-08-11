package com.example.netflix_clone.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netflix_clone.R;

public class StepThreeActivity extends AppCompatActivity {
    TextView signOut,spannableTextView;
    LinearLayout paymentLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_three);
        getSupportActionBar().hide();
        Intent intentFromActivityTwo=getIntent();
        Bundle userPlanInfo=intentFromActivityTwo.getExtras();
        signOut=findViewById(R.id.signoutstepthree);
        paymentLinearLayout=findViewById(R.id.paymentlinearlayout);
        spannableTextView=findViewById(R.id.stepthree);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToSignInActivity=new Intent(StepThreeActivity.this,SignInActivity.class);
                startActivity(intentToSignInActivity);
            }
        });
        paymentLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToPaymentGateway=new Intent(StepThreeActivity.this,PaymentGatewayActivity.class);
                intentToPaymentGateway.putExtras(userPlanInfo);
                startActivity(intentToPaymentGateway);
            }
        });
        SpannableString spannableString=new SpannableString("STEP 3 OF 3");
        StyleSpan boldSpan=new StyleSpan(Typeface.BOLD);
        StyleSpan boldSpan2=new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(boldSpan,5,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(boldSpan2,10,11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableTextView.setText(spannableString);
    }
}