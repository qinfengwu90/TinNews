package com.qinfengwu.tinnews.repository;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qinfengwu.tinnews.TinNewsApplication;
import com.qinfengwu.tinnews.database.TinNewsDatabase;
import com.qinfengwu.tinnews.model.Article;
import com.qinfengwu.tinnews.model.NewsResponse;
import com.qinfengwu.tinnews.network.NewsApi;
import com.qinfengwu.tinnews.network.RetrofitClient;

import java.nio.channels.AsynchronousChannelGroup;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {
    // webservice
    private final NewsApi newsApi;
    private final TinNewsDatabase database;

    public NewsRepository() {
        database = TinNewsApplication.getDatabase();
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

    public LiveData<List<Article>> getAllSavedArticles() {
        return database.articleDao().getAllArticles();
    }

    public void deleteSavedArticles(Article article) {
        AsyncTask.execute(() -> database.articleDao().deleteArticle(article));
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

    public LiveData<Boolean> favoriteArticle(Article article) {
        MutableLiveData<Boolean> saveResultLiveData = new MutableLiveData<>();

        FavoriteAsyncTask favoriteTask = new FavoriteAsyncTask(database, saveResultLiveData);
        favoriteTask.execute(article);
        return saveResultLiveData;
    }

    private static class FavoriteAsyncTask extends AsyncTask<Article, Void, Boolean> {

        private final TinNewsDatabase database;
        private final MutableLiveData<Boolean> liveData;

        private FavoriteAsyncTask(TinNewsDatabase database, MutableLiveData<Boolean> liveData) {
            this.database = database;
            this.liveData = liveData;
        }
        // background thread
        @Override
        protected Boolean doInBackground(Article... articles) {
            Article article = articles[0];
            try {
                database.articleDao().saveArticle(article);
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            liveData.setValue(b);
        }
    }
}
