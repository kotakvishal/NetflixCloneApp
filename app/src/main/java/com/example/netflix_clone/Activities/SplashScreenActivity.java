package com.example.netflix_clone.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.netflix_clone.MainScreens.MainScreenActivity;
import com.example.netflix_clone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore mFireStore;
    String firstName,lastName,contactNumber,uId,emilId;
    DocumentReference documentReference;
    Date today,validDate;
    ProgressBar progressBar;
    static int counter=0;
    static int duration=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseApp.initializeApp(getApplicationContext());
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().hide();
        }
        progressBar=findViewById(R.id.progress_bar);
        mAuth=FirebaseAuth.getInstance();
        mFireStore=FirebaseFirestore.getInstance();
        Calendar calendar=Calendar.getInstance();
        today=calendar.getTime();
        progress();
        start();
    }
    public void progress() {
        final Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                counter++;
                progressBar.setProgress(counter);
                if(counter>=2000)
                {
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask,0,100);
    }
    public void start() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mAuth.getCurrentUser()!=null)
                {
                    uId=mAuth.getCurrentUser().getUid();
                    documentReference=mFireStore.collection("Users")
                            .document(uId);
                    documentReference.get().addOnSuccessListener(SplashScreenActivity.this, new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            validDate=documentSnapshot.getDate("Valid_date");
                            firstName=documentSnapshot.getString("First_name");
                            lastName=documentSnapshot.getString("Last_name");
                            contactNumber=documentSnapshot.getString("Contact_number");
                            emilId=documentSnapshot.getString("Email");
                            if(validDate.compareTo(today)>0)
                            {
                                Intent intentToMainScreen=new Intent(SplashScreenActivity.this, MainScreenActivity.class);
                                startActivity(intentToMainScreen);
                                finish();
                            }
                            else
                            {
                                Intent intentToPaymentOverdue=new Intent(SplashScreenActivity.this,PaymentOverdueActivity.class);
                                intentToPaymentOverdue.putExtra("firstName",firstName);
                                intentToPaymentOverdue.putExtra("lastName",lastName);
                                intentToPaymentOverdue.putExtra("contactNumber",contactNumber);
                                intentToPaymentOverdue.putExtra("uId",uId);
                                startActivity(intentToPaymentOverdue);
                            }
                        }
                    }).addOnFailureListener(SplashScreenActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(e instanceof FirebaseNetworkException)
                            {
                                Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Error while fetching the data",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Intent intentToSignInActivity=new Intent(SplashScreenActivity.this,SignInActivity.class);
                    startActivity(intentToSignInActivity);
                    finish();
                }
            }
        },duration);
    }
}