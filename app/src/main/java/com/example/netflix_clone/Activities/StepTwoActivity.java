package com.example.netflix_clone.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.netflix_clone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class StepTwoActivity extends AppCompatActivity {
    Boolean allGood;
    FirebaseAuth mAuth;
    TextView signInTextView,spannableTextView;
    EditText userEmailEditText,userPassWordEditText;
    Button continueButton;
    ProgressDialog progressDialog;
    String planName,planCost,planCostFomat,userEmailString,userPassWordString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_two);
        getSupportActionBar().hide();
        signInTextView=findViewById(R.id.signinstepone);
        userEmailEditText=findViewById(R.id.emailedittextsteptwo);
        userPassWordEditText=findViewById(R.id.passwordedittextsteptwo);
        spannableTextView=findViewById(R.id.step2of3);
        continueButton=findViewById(R.id.continuesteptwo);
        Intent intentFromFinishUpActivity=getIntent();
        Bundle planInfo=intentFromFinishUpActivity.getExtras();
        planName=planInfo.getString("PlanName");
        planCost=planInfo.getString("PlanCost");
        planCostFomat=planInfo.getString("PlanFormatCost");
        mAuth=FirebaseAuth.getInstance();
        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToSignInActivity=new Intent(StepTwoActivity.this,SignInActivity.class);
                startActivity(intentToSignInActivity);
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                if(inputMethodManager.isAcceptingText())
                {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

                }
                userEmailString=userEmailEditText.getText().toString().trim();
                userPassWordString=userPassWordEditText.getText().toString().trim();
                allGood=true;
                if(userEmailString.length()<10 ||!userEmailString.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"))
                {
                   userEmailEditText.setError("Enter a valid email id");
                   allGood=false;
                }
                if(userPassWordString.length()<8)
                {
                    userPassWordEditText.setError("Password too short");
                    allGood=false;
                }
                if(allGood)
                {
                    progressDialog=new ProgressDialog(StepTwoActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    mAuth.signInWithEmailAndPassword(userEmailString,userPassWordString)
                            .addOnCompleteListener(StepTwoActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    allGood=false;
                                    Toast.makeText(StepTwoActivity.this,"Please enter via the main login screen",Toast.LENGTH_SHORT).show();
                                    Intent intentToSignInActivity=new Intent(StepTwoActivity.this,SignInActivity.class);
                                    startActivity(intentToSignInActivity);
                                }
                                else
                                {
                                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                                    {
                                        userEmailEditText.setError("Email already registered.");
                                        allGood=false;
                                        progressDialog.cancel();
                                    }
                                    if(task.getException() instanceof FirebaseNetworkException)
                                    {
                                        Toast.makeText(StepTwoActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                                        allGood=false;
                                        progressDialog.cancel();
                                    }
                                    if(userEmailString.length()>9 && userPassWordString.length()>7 && userEmailString.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")&& allGood)
                                    {
                                        Intent intentToActivityThree=new Intent(StepTwoActivity.this,StepThreeActivity.class);
                                        intentToActivityThree.putExtra("PlanName",planName);
                                        intentToActivityThree.putExtra("PlanCost",planCost);
                                        intentToActivityThree.putExtra("PlanFormatCost",planCostFomat);
                                        intentToActivityThree.putExtra("UserEmail",userEmailString);
                                        intentToActivityThree.putExtra("UserPassWord",userPassWordString);
                                        startActivity(intentToActivityThree);
                                        progressDialog.cancel();
                                    }
                                }
                                }
                            });
                }

            }
        });
        SpannableString spannableString=new SpannableString("STEP 2 OF 3");
        StyleSpan boldSpan=new StyleSpan(Typeface.BOLD);
        StyleSpan boldSpan2=new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(boldSpan,5,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(boldSpan2,10,11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableTextView.setText(spannableString);
    }
}