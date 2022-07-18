package com.vnpt.retrofit;

import com.vnpt.room.KhachHang;
import com.vnpt.room.LoaiPhi;
import com.vnpt.room.Xa;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiClient {

    @GET("xem/getthongtinphi")
    Call<List<LoaiPhi>> getListLoaiPhi(@Query("mst") String mst);

    @GET("xem/getthongtintk")
    Call<CompanyInfo> getCompanyInfo(@Query("mst") String mst);

    @GET("/API/GetThongTinXa")
    Call<List<Xa>> getXa();

    @GET("/API/GetThongTinPhuong/{id}")
    Call<List<Xa>> getPhuong(@Path("id") Integer id);

    @GET("/API/GetThongTinXaPhuong/{id}")
    Call<List<KhachHang>> getKhachHangs(@Path("id") Integer id);
}
