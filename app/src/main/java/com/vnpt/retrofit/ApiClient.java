package com.vnpt.retrofit;

import com.vnpt.room.LoaiPhi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiClient {

    @GET("xem/getthongtinphi")
    Call<List<LoaiPhi>> getListLoaiPhi(@Query("mst") String mst);

    @GET("xem/getthongtintk")
    Call<CompanyInfo> getCompanyInfo(@Query("mst") String mst);
}
