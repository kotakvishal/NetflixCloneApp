package com.example.netflix_clone.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netflix_clone.R;

public class FinishUpAccountActivity extends AppCompatActivity {
    TextView signInTextView,spannableTextView;
    Button continueButon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_up_account);
        getSupportActionBar().hide();
        signInTextView=findViewById(R.id.signinstepone);
        continueButon=findViewById(R.id.continueButtonFinishActivity);
        spannableTextView=findViewById(R.id.step1of3finish);
        Intent intentFromChoosePlan=getIntent();
        Bundle planInfo=intentFromChoosePlan.getExtras();
        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToSignInActivity=new Intent(FinishUpAccountActivity.this,SignInActivity.class);
                startActivity(intentToSignInActivity);
            }
        });
        continueButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToStepTwoActivity=new Intent(FinishUpAccountActivity.this,StepTwoActivity.class);
                intentToStepTwoActivity.putExtras(planInfo);
                startActivity(intentToStepTwoActivity);
            }
        });
        SpannableString spannableString=new SpannableString("STEP 2 OF 3");
        StyleSpan boldSpan=new StyleSpan(Typeface.BOLD);
        StyleSpan boldSpan2=new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(boldSpan,5,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(boldSpan2,10,11,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableTextView.setText(spannableString);
    }
}