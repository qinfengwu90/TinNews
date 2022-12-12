package com.qinfengwu.tinnews.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.qinfengwu.tinnews.model.Article;

import java.util.List;

@Dao
public interface ArticleDao {
    @Insert
    void saveArticle(Article article);

    @Query("SELECT * FROM article")
    LiveData<List<Article>> getAllArticles(); // Using LiveData will let Room automatically execute asyncly

    @Delete
    void deleteArticle(Article article);
}
