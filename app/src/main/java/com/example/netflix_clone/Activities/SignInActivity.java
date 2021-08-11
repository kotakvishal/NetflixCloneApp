package com.example.netflix_clone.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class SignInActivity extends AppCompatActivity {
    public TextView forgotPassWordTextView,signUpTextView;
    public Button signInButton;
    public ProgressBar signInProgressBar;
    EditText userEmailEdittext,userPasswordEdittext;
    String userEmailString,userPassWordString,userId,fireStoreContact
            ,firestoreFirstName,firestoreLastName;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    Date validDate,today;
    DocumentReference userRef;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();
        forgotPassWordTextView=findViewById(R.id.forgotPassWordTextView);
        signUpTextView=findViewById(R.id.signUpTextView);
        signInButton=findViewById(R.id.signin_button);
        signInProgressBar=findViewById(R.id.signinprogressbar);
        signInProgressBar.setVisibility(View.GONE);
        userEmailEdittext=findViewById(R.id.email_edit_text);
        userPasswordEdittext=findViewById(R.id.password_edit_text);
        mAuth=FirebaseAuth.getInstance();
        mFirestore=FirebaseFirestore.getInstance();
        Calendar calendarInstance=Calendar.getInstance();
        today=calendarInstance.getTime();
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intetToSwipeScreen=new Intent(SignInActivity.this,SwipeScreenActivity.class);
                startActivity(intetToSwipeScreen);
            }
        });
        forgotPassWordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmailString=userEmailEdittext.getText().toString();
                if(userEmailString.length()>8 && userEmailString.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"))
                {
                    AlertDialog.Builder passwordResetDialogueBox =new AlertDialog.Builder(SignInActivity.this);
                    passwordResetDialogueBox.setTitle("Reset Password?");

                    passwordResetDialogueBox.setMessage("Press YES to receive the reset link");
                    passwordResetDialogueBox.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            signInProgressBar.setVisibility(View.VISIBLE);
                           mAuth.sendPasswordResetEmail(userEmailString).addOnSuccessListener(SignInActivity.this, new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   Toast.makeText(getApplicationContext(),"Reset Link sent to your email.",Toast.LENGTH_SHORT).show();
                                   signInProgressBar.setVisibility(View.GONE);
                               }
                           }).addOnFailureListener(SignInActivity.this, new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception exception) {
                                if(exception instanceof FirebaseNetworkException)
                                {
                                    Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();
                                    signInProgressBar.setVisibility(View.GONE);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"User does not exits",Toast.LENGTH_SHORT).show();
                                    signInProgressBar.setVisibility(View.GONE);
                                }
                               }
                           });
                        }
                    });
                    passwordResetDialogueBox.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        }
                    });
                    passwordResetDialogueBox.show();
                }
                else
                {
                    userEmailEdittext.setError("Enter a valid email");
                }
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                signInProgressBar.setVisibility(View.VISIBLE);
                userEmailString=userEmailEdittext.getText().toString();
                userPassWordString=userPasswordEdittext.getText().toString();
                if(userEmailString.length()>8 && userEmailString.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")
                && userPassWordString.length()>7)
                {
                    mAuth.signInWithEmailAndPassword(userEmailString,userPassWordString)
                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        userId=mAuth.getCurrentUser().getUid();
                                        userRef=mFirestore.collection("Users").document(userId);
                                        userRef.get().addOnSuccessListener(SignInActivity.this, new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                validDate=documentSnapshot.getDate("Valid_date");
                                                firestoreFirstName=documentSnapshot.getString("First_name");
                                                firestoreLastName=documentSnapshot.getString("Last_name");
                                                fireStoreContact=documentSnapshot.getString("Contact_number");
                                                if(validDate.compareTo(today)>0)
                                                {
                                                    Intent intentToMainScreen =new Intent(SignInActivity.this, MainScreenActivity.class);
                                                    startActivity(intentToMainScreen);
                                                    signInProgressBar.setVisibility(View.GONE);
                                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                }
                                                else
                                                {
                                                    Intent intentToPaymentOverdue=new Intent(SignInActivity.this,PaymentOverdueActivity.class);
                                                    intentToPaymentOverdue.putExtra("firstName",firestoreFirstName);
                                                    intentToPaymentOverdue.putExtra("lastName",firestoreLastName);
                                                    intentToPaymentOverdue.putExtra("contactNumber",fireStoreContact);
                                                    intentToPaymentOverdue.putExtra("uId",userId);
                                                    startActivity(intentToPaymentOverdue);
                                                    signInProgressBar.setVisibility(View.GONE);
                                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                }
                                            }
                                        });
                                    }
                                    else
                                    {
                                        if(task.getException() instanceof FirebaseNetworkException)
                                        {
                                            Toast.makeText(SignInActivity.this,"No Internet Connecion",Toast.LENGTH_SHORT).show();
                                            signInProgressBar.setVisibility(View.GONE);
                                        }
                                        if(task.getException() instanceof FirebaseAuthInvalidUserException)
                                        {
                                            userEmailEdittext.setError("Invalid User");
                                            signInProgressBar.setVisibility(View.GONE);
                                        }
                                        if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                                        {
                                            userPasswordEdittext.setError("Invalid Password");
                                            signInProgressBar.setVisibility(View.GONE);
                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        }
                                    }
                                }});
                }
                else
                {
                    if(userEmailString.length()<=7 ||! userEmailString.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"))
                    {
                        userEmailEdittext.setError("Enter a valid E-mail id");
                        signInProgressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                    if(userPassWordString.length()<=7)
                    {
                        userPasswordEdittext.setError("Wrong Password");
                        signInProgressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                    if(userEmailString.length()==0)
                    {
                        userEmailEdittext.setError("Field can't be empty.");
                        signInProgressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                    if(userPassWordString.length()==0)
                    {
                        userPasswordEdittext.setError("Field can't be empty");
                        signInProgressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                    else
                    {
                        userPasswordEdittext.setError("Wrong Password");
                    }
                    signInProgressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

            }
        });
    }
}