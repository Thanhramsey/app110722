package com.vnpt.room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface LoaiPhiDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LoaiPhi loaiPhi);

    @Delete
    void delete(LoaiPhi loaiPhi);

    @Update
    void update(LoaiPhi loaiPhi);

    @Query("SELECT * FROM LOAI_PHI_TABLE")
    List<LoaiPhi> getAllLoaiPhi();

    @Query("DELETE FROM LOAI_PHI_TABLE")
    void cleanTable();
}
