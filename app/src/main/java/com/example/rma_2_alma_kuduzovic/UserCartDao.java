package com.example.rma_2_alma_kuduzovic;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserCartDao {
    @Insert
    void insert(UserCartModel userCartModel);

    @Query("SELECT * FROM user_cart WHERE userId = :userId")
    List<UserCartModel> getUserCart(String userId);

    @Query("DELETE FROM user_cart WHERE userId = :userId")
    void deleteUserCart(String userId);
}
