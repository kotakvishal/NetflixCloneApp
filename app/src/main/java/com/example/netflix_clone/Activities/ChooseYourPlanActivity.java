package com.example.netflix_clone.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netflix_clone.R;

public class ChooseYourPlanActivity extends AppCompatActivity {
    TextView signInTextView;
    Button continueButton;
    RadioButton basicRadioButton,standardRadioButton,premiumRadioButton;
    String planName,planCost,planFormatOfCost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_your_plan);
        getSupportActionBar().hide();
        signInTextView=findViewById(R.id.signinstepone);
        continueButton=findViewById(R.id.continuebuttonchooseplan);
        basicRadioButton=findViewById(R.id.radiobuttonforbasic);
        standardRadioButton=findViewById(R.id.radiobuttonforstandard);
        premiumRadioButton=findViewById(R.id.radiobuttonforpremium);
        basicRadioButton.setOnCheckedChangeListener(new Radio_check());
        standardRadioButton.setOnCheckedChangeListener(new Radio_check());
        premiumRadioButton.setOnCheckedChangeListener(new Radio_check());
        premiumRadioButton.setChecked(true);
        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToSignInActivity=new Intent(ChooseYourPlanActivity.this,SignInActivity.class);
                startActivity(intentToSignInActivity);
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToFinishUpAccount=new Intent(ChooseYourPlanActivity.this,FinishUpAccountActivity.class);
                intentToFinishUpAccount.putExtra("PlanName",planName);
                intentToFinishUpAccount.putExtra("PlanCost",planCost);
                intentToFinishUpAccount.putExtra("PlanFormatCost",planFormatOfCost);
                startActivity(intentToFinishUpAccount);
            }
        });
    }
    class Radio_check implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked)
            {
                if(buttonView.getId()== R.id.radiobuttonforbasic){
                    planName=getString(R.string.basic);
                    planCost=getString(R.string.basicPrice);
                    planFormatOfCost=getString(R.string.basicPriceFormat);
                    standardRadioButton.setChecked(false);
                    premiumRadioButton.setChecked(false);
                }
                if(buttonView.getId()== R.id.radiobuttonforstandard){
                    planName=getString(R.string.standard);
                    planCost=getString(R.string.standardPrice);
                    planFormatOfCost=getString(R.string.standardPriceFormat);
                    basicRadioButton.setChecked(false);
                    premiumRadioButton.setChecked(false);
                }
                if(buttonView.getId()== R.id.radiobuttonforpremium){
                    planName=getString(R.string.premium);
                    planCost=getString(R.string.premiumPrice);
                    planFormatOfCost=getString(R.string.premiumPriceFormat);
                    standardRadioButton.setChecked(false);
                    basicRadioButton.setChecked(false);
                }
            }
        }
    }
}