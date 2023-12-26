package com.vnpt.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TruBomDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TruBom truBom);

    @Delete
    void delete(TruBom truBom);

    @Update
    void update(TruBom truBom);

    @Query("SELECT * FROM TRUBOM_TABLE")
    List<TruBom> getAllTruBom();

    @Query("DELETE FROM TRUBOM_TABLE")
    void cleanTable();
}
