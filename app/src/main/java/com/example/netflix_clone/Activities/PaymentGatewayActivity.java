package com.example.netflix_clone.Activities;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.netflix_clone.MainScreens.MainScreenActivity;
import com.example.netflix_clone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
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

public class PaymentGatewayActivity extends AppCompatActivity implements PaymentResultListener {
    String planName,planCost,planCostFormat,
            userEmail,userPassWord,userId;
    TextView spannableTextView;
    TextView toPutUrlTextView;
    TextView changeTextView,costToSet,nameOfPlan;
    EditText firstNameEdittext,lastNameEdittext,
            contactNumberEdittext;
    Button startMembership;
    FirebaseAuth mAuth;
    String firstName,lastName,contactNumber;
    FirebaseFirestore mFirestore;
    CheckBox iAgreeCheckBox;
    Date today,validDate;
    ProgressDialog progressDialog;
    public static final String TAG=PaymentGatewayActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);
        getSupportActionBar().hide();
        Intent intentFromStepThree=getIntent();
        Bundle planInfo=intentFromStepThree.getExtras();
        planName=planInfo.getString("PlanName");
        planCost=planInfo.getString("PlanCost");
        planCostFormat=planInfo.getString("PlanFormatCost");
        userEmail=planInfo.getString("UserEmail");
        userPassWord=planInfo.getString("UserPassWord");
        spannableTextView=findViewById(R.id.stepthreeOfThreePaymentGateway);
        startMembership=findViewById(R.id.startmembershipbutton);
        changeTextView=findViewById(R.id.Change);
        iAgreeCheckBox=findViewById(R.id.iagree);
        costToSet=findViewById(R.id.costToSet);
        nameOfPlan=findViewById(R.id.planNametoSet);
        toPutUrlTextView=findViewById(R.id.toputurltext);
        firstNameEdittext=findViewById(R.id.firstNameEdittext);
        lastNameEdittext=findViewById(R.id.lastNameEdittext);
        contactNumberEdittext=findViewById(R.id.contactNumbertext);
        costToSet.setText(planCostFormat);
        nameOfPlan.setText(planName);
        SpannableString spannableString=new SpannableString("STEP 3 OF 3");
        StyleSpan boldSpan=new StyleSpan(Typeface.BOLD);
        StyleSpan boldSpan1=new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(boldSpan,5,6,SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(boldSpan1,10,11,SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableTextView.setText(spannableString);
        mFirestore=FirebaseFirestore.getInstance();
        Calendar calendarInstance=Calendar.getInstance();
        today=calendarInstance.getTime();
        calendarInstance.add(Calendar.MONDAY,1);
        validDate=calendarInstance.getTime();
        SpannableString spanForTandCAndPrivacy= new SpannableString(String.valueOf("By checking the checkbox below, you agree to our Terms of Use, Privacy Statement, and that you are over 18. Netflix will automatically continue your membership and charge the monthly membership fee to your payment method until you cancel. You may cancel at any time to avoid future charges."));
        ClickableSpan tAndCSpan =new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intentToHelp=new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/en"));
                startActivity(intentToHelp);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
            }
        };
        ClickableSpan privacySpan =new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intentToPrivacy=new Intent(Intent.ACTION_VIEW,Uri.parse("https://help.netflix.com/legal/privacy"));
                startActivity(intentToPrivacy);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
            }
        };
        spanForTandCAndPrivacy.setSpan(tAndCSpan,49,61, SPAN_EXCLUSIVE_EXCLUSIVE);
        spanForTandCAndPrivacy.setSpan(privacySpan,63,80, SPAN_EXCLUSIVE_EXCLUSIVE);
        toPutUrlTextView.setText(spanForTandCAndPrivacy);
        toPutUrlTextView.setMovementMethod(LinkMovementMethod.getInstance());
        changeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToChoosePlan=new Intent(PaymentGatewayActivity.this,ChooseYourPlanActivity.class);
                startActivity(intentToChoosePlan);
            }
        });
        startMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstNameEdittext.getText().toString().length()>3 && lastNameEdittext.getText().toString().length()>3
                        && firstNameEdittext.getText().toString().matches("[a-z A-Z]+")
                        && lastNameEdittext.getText().toString().matches("[a-z A-Z]+")
                        && contactNumberEdittext.getText().toString().length()==10
                        && iAgreeCheckBox.isChecked())
                {
                    startPayment();
                }
                else
                {
                   if(firstNameEdittext.getText().toString().length()<=3 || !firstNameEdittext.getText().toString().matches("[a-z A-Z]+"))
                   {
                       firstNameEdittext.setError("First name should be valid");
                   }
                   if(lastNameEdittext.getText().toString().length()<=3 || !lastNameEdittext.getText().toString().matches("[a-z A-Z]+"))
                   {
                       lastNameEdittext.setError("Last name should be valid");
                   }
                   if(contactNumberEdittext.getText().toString().length()!=10)
                   {
                       contactNumberEdittext.setError("Enter a valid 10 digit contact number");
                   }
                   if(!iAgreeCheckBox.isChecked())
                   {
                       Toast.makeText(getApplicationContext(),"Please agree the policy",Toast.LENGTH_SHORT).show();
                   }
                   else
                   {
                       Toast.makeText(getApplicationContext(),"Please fill the correct user information",Toast.LENGTH_SHORT).show();
                   }
                }

            }
        });
    }
    public void startPayment()
    {   progressDialog=new ProgressDialog(PaymentGatewayActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Checkout checkout=new Checkout();
        final Activity activity=this;
         firstName=firstNameEdittext.getText().toString();
         lastName=lastNameEdittext.getText().toString();
         contactNumber=contactNumberEdittext.getText().toString();
        String name=firstName+lastName;
        Log.e("Tag",userEmail+contactNumber+name.toString());
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
            options.put("prefill.email",userEmail);
            options.put("prefill.contact",contactNumber);
            checkout.open(activity,options);

        }
        catch (Exception e)
        {
            Log.e(TAG,"Payment Failed!!!",e);
        }
    }


    @Override
    public void onPaymentSuccess(String s)
    {
        mAuth=FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(userEmail,userPassWord)
                .addOnCompleteListener(PaymentGatewayActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            userId=mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=mFirestore.collection("Users")
                                    .document(userId);
                            Map<String,Object> user=new HashMap<>();
                            user.put("Email",userEmail);
                            user.put("First_name",firstName);
                            user.put("Last_name",lastName);
                            user.put("Plan_cost",planCost);
                            user.put("contact_number",contactNumber);
                            user.put("Valid_date",validDate);
                            documentReference.set(user).addOnSuccessListener(PaymentGatewayActivity.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intentToMainActivity=new Intent(PaymentGatewayActivity.this,MainScreenActivity.class);
                                    startActivity(intentToMainActivity);
                                    progressDialog.cancel();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    if(exception instanceof FirebaseNetworkException)
                                    {
                                        Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
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
                    }
                });
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(),"Payment Failed",Toast.LENGTH_LONG).show();
        progressDialog.cancel();
    }
}
