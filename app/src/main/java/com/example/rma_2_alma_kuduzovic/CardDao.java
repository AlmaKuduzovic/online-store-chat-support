package com.example.rma_2_alma_kuduzovic;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CardDao {

    @Insert
    void insert(CardModel card);

    @Query("SELECT * FROM cards")
    List<CardModel> getAll();
    @Query("DELETE FROM cards")
    void deleteAll();

    @Query("SELECT * FROM cards WHERE title LIKE '%' || :search || '%'")
    LiveData<List<CardModel>> searchCards(String search);
}
