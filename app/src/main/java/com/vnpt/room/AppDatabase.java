package com.vnpt.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {LoaiPhi.class,Xa.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LoaiPhiDAO getLoaiPhiDAO();
    public abstract XaDAO getXaDAO();
}
