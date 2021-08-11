package com.example.netflix_clone.MainScreens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflix_clone.Adapters.MainRecyclerAdapter;
import com.example.netflix_clone.Model.AllCategory;
import com.example.netflix_clone.R;
import com.example.netflix_clone.Retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class TvSeriesActivity extends AppCompatActivity {
BottomNavigationView bottomNavigationView;
    MainRecyclerAdapter mainRecyclerAdapter;
    RecyclerView MainRecycler;
    List<AllCategory> allCategoryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_series);
        getSupportActionBar().hide();
        bottomNavigationView=findViewById(R.id.bottom_navigationTvSeries);
        Menu menu=bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeicon:
                        Intent tvIntentToMainScreen=new Intent(TvSeriesActivity.this, MainScreenActivity.class);
                        startActivity(tvIntentToMainScreen);
                        break;
                    case R.id.serachicon:
                        Intent tvIntentToSearchActivity=new Intent(TvSeriesActivity.this, SearchActivity.class);
                        startActivity(tvIntentToSearchActivity);
                        break;
                    case R.id.settingsicon:
                        Intent tvIntentToSettingActivity=new Intent(TvSeriesActivity.this, SettingsActivity.class);
                        startActivity(tvIntentToSettingActivity);
                        break;
                }
                return false;
            }
        });
        ConnectivityManager connectivityManager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null||!networkInfo.isConnected()||!networkInfo.isAvailable()){
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Please turn on your internet connection to continue.");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    recreate();
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);
        }
        else{
            allCategoryList=new ArrayList<>();
            getAllMovieData(2);
        }
    }
    public void setMainRecycler(List<AllCategory> allCategoryList){
        MainRecycler=findViewById(R.id.tvRecyclerView);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        MainRecycler.setLayoutManager(layoutManager);
        mainRecyclerAdapter= new MainRecyclerAdapter(this,allCategoryList);
        MainRecycler.setAdapter(mainRecyclerAdapter);
    }

    private void getAllMovieData(int categoryId) {
        CompositeDisposable compositeDisposable= new CompositeDisposable();
        compositeDisposable.add(RetrofitClient.getRetrofitClient().getAllCategoryMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<AllCategory>>(){

                    @Override
                    public void onNext(List<AllCategory> allCategoryList) {
                        setMainRecycler(allCategoryList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(String.valueOf(TvSeriesActivity.this),e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                })

        );
    }
}