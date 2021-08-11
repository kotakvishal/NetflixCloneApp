package com.example.netflix_clone.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.netflix_clone.Adapters.ViewPagerAdapter;
import com.example.netflix_clone.R;

public class SwipeScreenActivity extends AppCompatActivity {
    TextView signIn,help,privacy;
    Button getStarted;
    ViewPager viewPagerSwipe;
    LinearLayout sliderdots;
    private int dotsCount;
    private ImageView[] dots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_screen);
        getSupportActionBar().hide();
        signIn=findViewById(R.id.signInTextView);
        help=findViewById(R.id.helpTextView);
        privacy=findViewById(R.id.privacyTextView);
        getStarted=findViewById(R.id.getStartedButton);
        viewPagerSwipe=findViewById(R.id.viewPagerSwipeScreen);
        sliderdots=findViewById(R.id.silderdots);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this);
        viewPagerSwipe.setAdapter(viewPagerAdapter);
        dotsCount=viewPagerAdapter.getCount();
        dots=new ImageView[dotsCount];
        for(int i=0;i<dotsCount;i++)
        {
            dots[i]=new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.inactivedots));
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            sliderdots.addView(dots[i],params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.activedots));
        viewPagerSwipe.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i=0;i<dotsCount;i++)
                {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.inactivedots));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.activedots));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToSignInActivity=new Intent(SwipeScreenActivity.this,SignInActivity.class);
                startActivity(intentToSignInActivity);
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriToPrivacy=Uri.parse("https://help.netflix.com/en/node/100628");
                Intent intentToPrivacy=new Intent(Intent.ACTION_VIEW,uriToPrivacy);
                startActivity(intentToPrivacy);
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriToHelp=Uri.parse("https://help.netflix.com/en/");
                Intent intentToHelp=new Intent(Intent.ACTION_VIEW,uriToHelp);
                startActivity(intentToHelp);
            }
        });
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToStepOne=new Intent(SwipeScreenActivity.this,StepOneActivity.class);
                startActivity(intentToStepOne);
            }
        });
    }
}