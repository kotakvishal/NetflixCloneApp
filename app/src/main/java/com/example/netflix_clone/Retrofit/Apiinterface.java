package com.example.netflix_clone.Retrofit;

import static com.example.netflix_clone.Retrofit.RetrofitClient.BASE_URL;

import com.example.netflix_clone.Model.AllCategory;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface Apiinterface {
    @GET(BASE_URL)
    Observable<List<AllCategory>> getAllCategoryMovies();
}
