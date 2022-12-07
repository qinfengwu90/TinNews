package com.qinfengwu.tinnews.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qinfengwu.tinnews.model.NewsResponse;
import com.qinfengwu.tinnews.network.NewsApi;
import com.qinfengwu.tinnews.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {
    // webservice
    private final NewsApi newsApi;

    public NewsRepository() {
        newsApi = RetrofitClient.newInstance().create(NewsApi.class);
    }

    public LiveData<NewsResponse> getTopHeadlines(String country) {
        Call<NewsResponse> call = newsApi.getTopHeadlines(country);
        MutableLiveData<NewsResponse> topHeadlinesLiveData = new MutableLiveData<>();
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) { //200, 202
                    topHeadlinesLiveData.setValue(response.body()); // setState
                } else {
                    topHeadlinesLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                topHeadlinesLiveData.setValue(null);
            }
        });
        return topHeadlinesLiveData;
    }

    public LiveData<NewsResponse> searchNews(String query) {
        Call<NewsResponse> call = newsApi.getEverything(query, 40);
        MutableLiveData<NewsResponse> everyThingLiveData = new MutableLiveData<>();
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) { //200, 202
                    everyThingLiveData.setValue(response.body()); // setState
                } else {
                    everyThingLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                everyThingLiveData.setValue(null);
            }
        });
        return everyThingLiveData;
    }

    // db
}
