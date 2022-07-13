package com.vnpt.room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface XaDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Xa xa);

    @Delete
    void delete(Xa xa);

    @Update
    void update(Xa xa);

    @Query("SELECT * FROM XA_TABLE")
    List<Xa> getAllXa();

    @Query("DELETE FROM XA_TABLE")
    void cleanTable();
}
