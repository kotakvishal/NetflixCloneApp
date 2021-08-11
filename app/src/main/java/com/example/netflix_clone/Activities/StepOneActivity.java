package com.example.netflix_clone.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netflix_clone.R;

public class StepOneActivity extends AppCompatActivity {
    TextView signInTextView,spanTextView;
    Button seeYourPlansButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_one);
        getSupportActionBar().hide();
        signInTextView=findViewById(R.id.signinstepone);
        seeYourPlansButton=findViewById(R.id.seeyourplanbutton);
        spanTextView=findViewById(R.id.stepTextView);
        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToSignInActivity=new Intent(StepOneActivity.this,SignInActivity.class);
                startActivity(intentToSignInActivity);
            }
        });
        seeYourPlansButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToChoosePlanActivity=new Intent(StepOneActivity.this,ChooseYourPlanActivity.class);
                startActivity(intentToChoosePlanActivity);
            }
        });

        SpannableString spannableString=new SpannableString("STEP 1 OF 3");
        StyleSpan boldspan=new StyleSpan(Typeface.BOLD);
        StyleSpan boldspan1=new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(boldspan,5,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(boldspan1,10,11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanTextView.setText(spannableString);
    }
}