package com.vnpt.staffhddt.pos58;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.izettle.html2bitmap.Html2Bitmap;
import com.izettle.html2bitmap.content.WebViewContent;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vnpt.common.Common;
import com.vnpt.common.ModelEvent;
import com.vnpt.dto.BienLai;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.printproject.ImageUtils;
import com.vnpt.printproject.QRCodeGenerator;
import com.vnpt.printproject.pos58bus.command.sdk.Command;
import com.vnpt.printproject.pos58bus.command.sdk.PrintPicture;
import com.vnpt.printproject.pos58bus.command.sdk.PrinterCommand;
import com.vnpt.retrofit.ApiClient;
import com.vnpt.room.AppDataHelper;
import com.vnpt.room.KhachHang;
import com.vnpt.room.LoaiPhi;
import com.vnpt.room.Product;
import com.vnpt.room.TruBom;
import com.vnpt.room.TruBomDAO;
import com.vnpt.room.Xa;
import com.vnpt.room.XaDAO;
import com.vnpt.staffhddt.DetailsActivity;
import com.vnpt.staffhddt.LoginActivity;
import com.vnpt.staffhddt.MainPos58Activity;
import com.vnpt.staffhddt.R;
import com.vnpt.staffhddt.fragment.BaseFragment;
import com.vnpt.utils.DialogUtils;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.utils.StringBienLai;
import com.vnpt.webservice.AppServices;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.vnpt.staffhddt.MainPos58Activity.mPOSPrinter;
import static com.vnpt.utils.Helper.hideSoftKeyboard;

import androidx.annotation.RequiresApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XuatVeXePos58Fragment extends BaseFragment implements View.OnClickListener, OnEventControlListener {

    private static final int REQUEST_BLUE_ADMIN = 888;
    Spinner spMenhGia, spPhuong;
    Button btnInThu, btnSearch, button1, button2, button3, button4, button5, button6, button7, btnCheckTB;

    RadioButton radio_a, radio_b, radio_c, radio_d, radio_e;
    TextView txtDemo;

    EditText soTien;

    //    ImageButton btnClear;
    MaterialEditText edtSearch;
    private AwesomeProgressDialog dg;
    StoreSharePreferences preferences = null;

    ProductListViewAdapter productListViewAdapter;
    ListView listViewProduct;

    List<LoaiPhi> loaiPhiList;
    List<Xa> xaList;
    List<Xa> phuongList;
    List<KhachHang> khachHangList;
    LoaiPhi loaiPhi = null;
    Xa xa = null;
    Xa phuong = null;


    ArrayList<KhachHang> listKhachHang;

    private GetInvTask getInvTask = null;

    public static String TAG = XuatVeXePos58Fragment.class.getName();

    private int type;

    private static final String[] PRICES = new String[]{
            "10.000"
    };

    public XuatVeXePos58Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_xuat_ve_xe, container, false);

        setupUI(layout.findViewById(R.id.layout_frament_xuat_ve_xe));
        init(layout);

        setEventForMembers();
        setValueForMembers();
        ((MainPos58Activity) getActivity()).showProccessbar(false);

        preferences = StoreSharePreferences.getInstance(getContext());

        return layout;
    }

    @Override
    protected void init(View layout) {
        button1 = layout.findViewById(R.id.button1);
        button2 = layout.findViewById(R.id.button2);
        button3 = layout.findViewById(R.id.button3);
        button4 = layout.findViewById(R.id.button4);
        button5 = layout.findViewById(R.id.button5);
        button6 = layout.findViewById(R.id.button6);
        button7 = layout.findViewById(R.id.button7);

        radio_a = layout.findViewById(R.id.radio_a);
        radio_b = layout.findViewById(R.id.radio_b);
        radio_c = layout.findViewById(R.id.radio_c);
        radio_d = layout.findViewById(R.id.radio_d);
        radio_e = layout.findViewById(R.id.radio_e);

        soTien = layout.findViewById(R.id.soTien);
        btnCheckTB = layout.findViewById(R.id.btnCheck);

        txtDemo = layout.findViewById(R.id.txtDemo);
    }

    @Override
    protected void setValueForMembers() {
        String name = StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_COMPANY_NAME);
        String mst = StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_MST);

        type = getArguments().getInt(Common.KEY_COMPANY_TYPE);
        String url = StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences("WEBSITE");
        loadTruBom(url);
    }

    private void loadTruBom(String url) {
        ApiClient apiClient2 = AppDataHelper.getApiClient2(url);
        apiClient2.getThongTinTruBom().enqueue(new Callback<List<TruBom>>() {
            @Override
            public void onResponse(Call<List<TruBom>> call, Response<List<TruBom>> response) {

                List<TruBom> truBomList = response.body();
                radio_a.setText(truBomList.get(0).getName());
                radio_b.setText(truBomList.get(1).getName());
                radio_c.setText(truBomList.get(2).getName());
                if (truBomList.size() > 3 && truBomList.get(3) != null) {
                    radio_d.setEnabled(true);
                    radio_d.setText(truBomList.get(3).getName());
                }
                if (truBomList.size() > 4 && truBomList.get(4) != null) {
                    radio_e.setEnabled(true);
                    radio_e.setText(truBomList.get(4).getName());
                }

                showProgress(false);
            }

            @Override
            public void onFailure(Call<List<TruBom>> call, Throwable t) {
                showProgress(false);
                Toast.makeText(getContext(), "Có lỗi xảy ra !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void xuatHoaDon(String fkey, int idTru, String soTien, String user) {
        String url = StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences("WEBSITE");
        ApiClient apiClient2 = AppDataHelper.getApiClient2(url);
        apiClient2.xuatPhieuThu(fkey, idTru, soTien, user).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String traVe = response.body();
                if (traVe.equals("1")) {
                    printInvoice(fkey, soTien, idTru);
                }
                Toast.makeText(getContext(), "Thành Công", Toast.LENGTH_SHORT).show();
                showProgress(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showProgress(false);
                Toast.makeText(getContext(), "Có lỗi xảy ra !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void setEventForMembers() {
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        btnCheckTB.setOnClickListener(this);
    }

    @Override
    public void handleModelViewEvent(ModelEvent modelEvent) {
    }

    @Override
    public void handleErrorModelViewEvent(ModelEvent modelEvent) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            loaiPhiList = (List<LoaiPhi>) intent.getSerializableExtra("KEY_LIST_FEE");
        }
    }

//    private void attemptGetInv() {
//        if (getInvTask != null) {
//            return;
//        }
//
//        String num = edtSoLuong.getText().toString().trim();
//        String bsx = edtBSX.getText().toString().trim();
//
//        boolean cancel = false;
//        View focusView = null;
//
//        if (TextUtils.isEmpty(num) || TextUtils.isEmpty(bsx) || bsx.length() < 7) {
//            edtSoLuong.setError(getString(R.string.error_empty_input));
//            focusView = edtSoLuong;
//            cancel = true;
//        }
//
//        if (TextUtils.isEmpty(bsx) || bsx.length() < 7) {
//            edtBSX.setError(getString(R.string.error_empty_input_bsx));
//            focusView = edtBSX;
//            cancel = true;
//        }
//
//        if (cancel) {
//            focusView.requestFocus();
//        } else {
//            showProgress(true);
//            int numOfInv = Integer.parseInt(num);
//            StringBuilder sb = new StringBuilder();
//
//            //lap lai so ve
//            for (int i = 1; i <= numOfInv; i++) {
//                try {
//                    TimeUnit.MILLISECONDS.sleep(1);
//                    long currentTime = System.currentTimeMillis();
//                    String keyData = i + "_INVE" + currentTime;
//                    // mau XML o day
//                    String xmlChildData = "";
//                    if (type == Common.TYPE_VE) {
//                        xmlChildData = "<Inv><key>" + keyData + "</key><Invoice><CusCode>" + StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME) + "</CusCode><Buyer></Buyer><CusName></CusName><CusAddress></CusAddress><CusPhone></CusPhone><CusTaxCode></CusTaxCode><PaymentMethod>TM, CK </PaymentMethod><KindOfService></KindOfService><Products><Product><ProdName>" + loaiPhi.getNAME() + "</ProdName><ProdUnit></ProdUnit><ProdQuantity></ProdQuantity><ProdPrice>" + loaiPhi.getTOTAL() + "</ProdPrice><Amount>" + loaiPhi.getTOTAL() + "</Amount><Extra1></Extra1><Extra2></Extra2></Product></Products><Total>" + loaiPhi.getTOTAL() + "</Total><VATRate>"+
//                                loaiPhi.getVAT_RATE() + "</VATRate><VATAmount>" + loaiPhi.getVAT_AMOUNT() + "</VATAmount><Amount>" + loaiPhi.getAMOUNT() + "</Amount><AmountInWords>"+ StringBienLai.docSo(loaiPhi.getAMOUNT()) +"</AmountInWords><Extra></Extra></Invoice></Inv>";
//                    } else {
//                        xmlChildData = "<Inv><key>" + keyData + "</key><Invoice><CusCode><![CDATA[" + keyData + "]]></CusCode><CusName><![CDATA[" + StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME) + "]]></CusName><CusAddress><![CDATA[]]></CusAddress><CusPhone></CusPhone><CusTaxCode></CusTaxCode><PaymentMethod><![CDATA[]]></PaymentMethod><Products><Product><Code><![CDATA[]]></Code><ProdName>"+loaiPhi.getNAME()+"</ProdName><ProdUnit>Lần</ProdUnit><ProdQuantity>1</ProdQuantity><ProdPrice>"+loaiPhi.getAMOUNT()+"</ProdPrice><Amount>1</Amount></Product></Products><KindOfService><![CDATA[]]></KindOfService><Total>"+loaiPhi.getTOTAL()+"</Total><Amount>"+loaiPhi.getAMOUNT()+"</Amount><AmountInWords>"+StringBienLai.docSo(loaiPhi.getAMOUNT())+"</AmountInWords></Invoice></Inv>";
//                    }
//                    /////////////////////////
//                    sb.append(xmlChildData);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            String xmlData = "<Invoices>" + sb.toString() + "</Invoices>";
//
//            getInvTask = new GetInvTask(
//                    StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME),
//                    StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_PASS),
//                    loaiPhi.getPATTERN(),
//                    loaiPhi.getSERIAL(),
//                    0,
//                    xmlData);
//            getInvTask.setBsx(bsx);
//            getInvTask.execute((Void) null);
//        }
//    }

    private void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1: {
                if (mPOSPrinter.isBTActivated()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                        long currentTime = System.currentTimeMillis();
                        String keyData = "INVE_" + currentTime;
                        int check = getRaioCheck();
                        if (check == 0) {
                            Toast.makeText(getContext(), "Chưa chọn trụ", Toast.LENGTH_SHORT).show();
                        }
                        String soTien = button1.getText().toString().replaceAll("[^0-9]+", "");
                        xuatHoaDon(keyData, check, soTien, StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(getContext(), "Vui lòng bật bluetooth và kết nối máy in", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.button2: {
                if (mPOSPrinter.isBTActivated()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                        long currentTime = System.currentTimeMillis();
                        String keyData = "INVE_" + currentTime;
                        int check = getRaioCheck();
                        if (check == 0) {
                            Toast.makeText(getContext(), "Chưa chọn trụ", Toast.LENGTH_SHORT).show();
                        }
                        String soTien = button2.getText().toString().replaceAll("[^0-9]+", "");
                        xuatHoaDon(keyData, check, soTien, StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(getContext(), "Vui lòng bật bluetooth và kết nối máy in", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.button3: {
                if (mPOSPrinter.isBTActivated()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                        long currentTime = System.currentTimeMillis();
                        String keyData = "INVE_" + currentTime;
                        int check = getRaioCheck();
                        if (check == 0) {
                            Toast.makeText(getContext(), "Chưa chọn trụ", Toast.LENGTH_SHORT).show();
                        }
                        String soTien = button3.getText().toString().replaceAll("[^0-9]+", "");
                        xuatHoaDon(keyData, check, soTien, StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(getContext(), "Vui lòng bật bluetooth và kết nối máy in", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.button4: {
                if (mPOSPrinter.isBTActivated()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                        long currentTime = System.currentTimeMillis();
                        String keyData = "INVE_" + currentTime;
                        int check = getRaioCheck();
                        if (check == 0) {
                            Toast.makeText(getContext(), "Chưa chọn trụ", Toast.LENGTH_SHORT).show();
                        }
                        String soTien = button4.getText().toString().replaceAll("[^0-9]+", "");
                        xuatHoaDon(keyData, check, soTien, StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(getContext(), "Vui lòng bật bluetooth và kết nối máy in", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.button5: {
                if (mPOSPrinter.isBTActivated()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                        long currentTime = System.currentTimeMillis();
                        String keyData = "INVE_" + currentTime;
                        int check = getRaioCheck();
                        if (check == 0) {
                            Toast.makeText(getContext(), "Chưa chọn trụ", Toast.LENGTH_SHORT).show();
                        }
                        String soTien = button5.getText().toString().replaceAll("[^0-9]+", "");
                        xuatHoaDon(keyData, check, soTien, StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(getContext(), "Vui lòng bật bluetooth và kết nối máy in", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.button6: {
                if (mPOSPrinter.isBTActivated()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                        long currentTime = System.currentTimeMillis();
                        String keyData = "INVE_" + currentTime;
                        int check = getRaioCheck();
                        if (check == 0) {
                            Toast.makeText(getContext(), "Chưa chọn trụ", Toast.LENGTH_SHORT).show();
                        }
                        String soTien = button6.getText().toString().replaceAll("[^0-9]+", "");
                        xuatHoaDon(keyData, check, soTien, StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(getContext(), "Vui lòng bật bluetooth và kết nối máy in", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.button7: {
                if (mPOSPrinter.isBTActivated()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    long currentTime = System.currentTimeMillis();
                        String keyData = "INVE_" + currentTime;
                        int check = getRaioCheck();
                        if (check == 0) {
                            Toast.makeText(getContext(), "Chưa chọn trụ", Toast.LENGTH_SHORT).show();
                        }
                    String soTienText = soTien.getText().toString().replaceAll("[^0-9]+", "")+"000";
                    xuatHoaDon(keyData, check, soTienText, StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME));
                } else {
                    Toast.makeText(getContext(), "Vui lòng bật bluetooth và kết nối máy in", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.btnCheck: {
                checkPrinter();
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void searhKhachHang() {
        String searchText = edtSearch.getText().toString().trim();
        String searchText2 = StringBienLai.removeAccent(searchText);
        List<KhachHang> khachHangSearch = khachHangList.stream().filter(p ->
                StringBienLai.removeAccent(p.getNAME().substring(p.getNAME().lastIndexOf(',') + 1).trim().toLowerCase()).contains(searchText2.trim().toLowerCase())).collect(Collectors.toList());
        if (khachHangSearch.size() > 0 && khachHangSearch != null) {
            listKhachHang = new ArrayList<>();
            for (KhachHang kh : khachHangSearch) {
                if (kh.getDIACHI() == null || kh.getDIACHI().length() == 0) {
                    kh.setDIACHI(phuong.getNAME() + " - " + xa.getNAME());
                }
                listKhachHang.add(new KhachHang(kh));
            }
            productListViewAdapter = new ProductListViewAdapter(listKhachHang);
            listViewProduct.setAdapter(productListViewAdapter);
            listViewProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    KhachHang khachHang = (KhachHang) productListViewAdapter.getItem(position);
//                            Toast.makeText(getContext(), khachHang.getNAME(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), DetailsActivity.class);
                    intent.putExtra("KEY_KHACHHANG", khachHang);
                    intent.putExtra("KEY_PHI", (Serializable) loaiPhiList);
                    startActivity(intent);
                }
            });
        } else {
            listViewProduct.setAdapter(null);
        }
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {
        ((MainPos58Activity) getActivity()).showProccessbar(false);
    }

    //gui request len webservices de lay inv data
    public class GetInvTask extends AsyncTask<Void, Void, String> {

        //String username, String password, String strPattern, String strSerial, int convert, String strXmlInvData
        private String userName;
        private String password;
        private String strPattern;
        private String strSerial;
        private int convert;
        private String strXmlInvData;

        private String bsx;

        public void setBsx(String bsx) {
            this.bsx = bsx;
        }

        public GetInvTask(String userName, String password, String strPattern,
                          String strSerial, int convert, String strXmlInvData) {
            this.userName = userName;
            this.password = password;
            this.strPattern = strPattern;
            this.strSerial = strSerial;
            this.convert = convert;
            this.strXmlInvData = strXmlInvData;
        }

        @Override
        protected String doInBackground(Void... voids) {
            AppServices appServices = new AppServices(getActivity());
            String result = appServices.importAndPublishInv(userName, password, strPattern, strSerial, convert, strXmlInvData);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println(s);

            if (!s.split(":")[0].equals("ERR")) {

                String[] listResults = s.substring(s.lastIndexOf("-") + 1).split(",");

                for (String inv : listResults) {
//                    printInvoice(inv, bsx);
                    Print_Test();
                }
            } else {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
            getInvTask = null;
            showProgress(false);
            if (dg != null) {
                //if (dg.isShowing())
                dg.hide();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            showProgress(false);
            if (dg != null) {
                //if (dg.isShowing())
                dg.hide();
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (show) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                dg = DialogUtils.showLoadingDialog(getString(R.string.text_proccessing), getContext(), null);
            } else {
                dg = DialogUtils.showLoadingDialog(getString(R.string.text_proccessing), getContext(), null);
            }
        } else {
            if (dg != null) {
                //if (dg.isShowing())
                dg.hide();
            }
        }

    }

    public void printInvoice(String fkey, String soTien, int idTru) {

        String[] soBL = fkey.split("_");
//        BienLai bienLai = new BienLai();
//        bienLai.setMoTa(loaiPhi.getNAME());
//        bienLai.setGiaTien(loaiPhi.getAMOUNT());
//        bienLai.setMau(loaiPhi.getPATTERN());
//        bienLai.setSo(soBL[2].trim());
//        bienLai.setKyHieu(loaiPhi.getSERIAL());
//        bienLai.setPortal(StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_COMPANY_PORTAL));

        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        String name = (type == Common.TYPE_VE) ? "VÉ ĐIỆN TỬ" : "BIÊN LAI ĐIỆN TỬ";
        String search_des = (type == Common.TYPE_VE) ? "Để tra cứu vé gốc kính mời truy cập vào link : " : "Để tra cứu biên lai gốc kính mời truy cập vào link : ";
        // Tạo mã QR từ dữ liệu fkey
        String strTraCuu = StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_COMPANY_PORTAL) + "?strfkey=" + fkey;
        Bitmap qrCodeBitmap = QRCodeGenerator.generateQRCode(strTraCuu, 300, 300);

        // Chuyển đổi Bitmap thành chuỗi Base64 để chèn vào HTML
        String qrCodeBase64 = ImageUtils.bitmapToBase64(qrCodeBitmap);

        // Chèn mã QR vào HTML
        String qrCodeImageTag = "<img src='data:image/png;base64," + qrCodeBase64 + "' width='150' height='150'/>";
        String html = "<html>\n" +
                "  <body>\n" +
                "    <style>\n" +
                "      body {\n" +
                "        line-height: 1; \n" +
                "      }\n" +
                "    </style>\n" +
                "<div style=\"text-align:center;\">" +
                "                   <h2 style=\\\"text-align: center;font-size: 40px\\\">" + StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_COMPANY_NAME) + "</h2>\n" +
                "                       <h2 style=\\\"text-align: center;font-size: 40px\\\">MST: " + StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_MST) +
//                "               <h1 style=\\\"text-align: center;font-size: 40px\\\">" + name + "</h1>\n" +
                "<h3 style='text-align:center;font-size: 30px'>(Phiếu thu tiền xăng dầu)</h3>" +
                "</div>" +
                "                \n" + "<h2 style=\\\"text-align: center;font-size: 40px\\\">Ngày: " + date + "</h2>" +
                "                 <h2>Giá: " + Math.round(Float.parseFloat(soTien)) + " VND (" + StringBienLai.docSo(Double.parseDouble(soTien)) + ")</h2>\n" +
                "               <h1 style=\\\"text-align: center\\\">-------------------------- </h1>" +
                "<h3 style='text-align:right;font-size: 20px'>" + search_des + "</h3>" +
                "<h3>" + strTraCuu + "</h3>" +
                "<div style=\"text-align:center;\">" +
                "" + qrCodeImageTag + "" +
                "</div>" +
                "                </body>\n" +
                "                </html>";

        new converHTMLTask().execute(html);

    }

    private class converHTMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... str) {
            String html = str[0];
            //Toast.makeText(MainActivity.this, "Converting...", Toast.LENGTH_SHORT).show();
            return new Html2Bitmap.Builder()
                    .setContext(getContext())
                    .setContent(WebViewContent.html(html))
                    .setBitmapWidth(384)
                    .build()
                    .getBitmap();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                Print_BMP(bitmap);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getContext(), "Quá trình in bị huỷ", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadPhuong(int id) {
        ApiClient apiClient2 = AppDataHelper.getApiClient2("");
        apiClient2.getPhuong(id, StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME)).enqueue(new Callback<List<Xa>>() {
            @Override
            public void onResponse(Call<List<Xa>> call, Response<List<Xa>> response) {
                //Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                phuongList = response.body();
                if (phuongList != null && phuongList.size() > 0) {
                    ArrayAdapter<Xa> adapter = new ArrayAdapter<Xa>(getContext(),
                            android.R.layout.simple_dropdown_item_1line, phuongList);
//                    phuongList = filterPhuong(phuongList);
                    spPhuong.setAdapter(adapter);
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_dropdown_item_1line, PRICES);
                    spPhuong.setAdapter(adapter);
                }
                spPhuong.setSelection(0);
                showProgress(false);
            }

            @Override
            public void onFailure(Call<List<Xa>> call, Throwable t) {
                showProgress(false);
            }
        });
    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public List<Xa> filterPhuong(List<Xa> phuong){
//        List<Xa> phuongFilter = (List<Xa>) phuong.stream().filter(p -> p.getUSERNAME() == Common.KEY_USER_NAME);
//        return phuongFilter;
//    }

    private void loadKhachHang(int id) {
        ApiClient apiClient2 = AppDataHelper.getApiClient2("");
        apiClient2.getKhachHangs(id).enqueue(new Callback<List<KhachHang>>() {
            @Override
            public void onResponse(Call<List<KhachHang>> call, Response<List<KhachHang>> response) {
                //Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                khachHangList = response.body();
//                showProgress(false);

                if (khachHangList.size() > 0 && khachHangList != null) {
                    listKhachHang = new ArrayList<>();
                    for (KhachHang kh : khachHangList) {
                        if (kh.getDIACHI() == null || kh.getDIACHI().length() == 0) {
                            kh.setDIACHI(phuong.getNAME() + " - " + xa.getNAME());
                        }
                        listKhachHang.add(new KhachHang(kh));
                    }
                    productListViewAdapter = new ProductListViewAdapter(listKhachHang);
                    listViewProduct.setAdapter(productListViewAdapter);
                    listViewProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            KhachHang khachHang = (KhachHang) productListViewAdapter.getItem(position);
//                            Toast.makeText(getContext(), khachHang.getNAME(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getContext(), DetailsActivity.class);
                            intent.putExtra("KEY_KHACHHANG", khachHang);
                            intent.putExtra("KEY_PHI", (Serializable) loaiPhiList);
                            startActivity(intent);
                        }
                    });
                } else {
                    listViewProduct.setAdapter(null);
                }
            }

            @Override
            public void onFailure(Call<List<KhachHang>> call, Throwable t) {
                showProgress(false);
            }
        });
    }

    private void Print_BMP(Bitmap mBitmap) {
        //	byte[] buffer = PrinterCommand.POS_Set_PrtInit();
        int nMode = 0;
        int nPaperWidth = 384;

        if (mBitmap != null) {
            byte[] data = PrintPicture.POS_PrintBMP(mBitmap, nPaperWidth, nMode);
            //	SendDataByte(buffer);
            SendDataByte(Command.ESC_Init);
            SendDataByte(Command.LF);
            SendDataByte(data);
            SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(50));
            SendDataByte(PrinterCommand.POS_Set_Cut(1));
            SendDataByte(PrinterCommand.POS_Set_PrtInit());
        }
    }


    /*
     *SendDataByte
     */
    private void SendDataByte(byte[] data) {

        if (mPOSPrinter.getState() != com.vnpt.printproject.pos58bus.BluetoothService.STATE_CONNECTED) {
            Toast.makeText(getContext(), R.string.not_connected, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        mPOSPrinter.write(data);
    }

    private Integer getRaioCheck() {
        if (radio_a.isChecked()) {
            return 1;
        } else if (radio_b.isChecked()) {
            return 2;
        } else if (radio_c.isChecked()) {
            return 3;
        } else if (radio_d.isChecked()) {
            return 4;
        } else if (radio_e.isChecked()) {
            return 5;
        } else
            return 0;
    }

    private void alertDemo(String menhGia) {
        if (radio_a.isChecked()) {
            txtDemo.setText("trụ 1 - Số tiền: " + menhGia + StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences("WEBSITE"));
        } else if (radio_b.isChecked()) {
            txtDemo.setText("trụ 2 - Số tiền: " + menhGia + StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences("WEBSITE"));
        } else if (radio_c.isChecked()) {
            txtDemo.setText("trụ 3 - Số tiền: " + menhGia + StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences("WEBSITE"));
        }
    }

    private void Print_Test() {
        String msg = "<html>\n" +
                "<body>\n" +
                "<style>\n" +
                "  </style>\n" +
                "<h1 style=\"text-align: center;font-size: 40px\">BIÊN LAI ĐIỆN TỬ</h1>\n" +
                "<h1 style=\"text-align: left;font-size: 40px\">STT: 1</h1>\n" +
                "<table style=\"text-align: center;width:100%;\">\n" +
                "  <tr>\n" +
                "    <th style=\"font-size:30px\">Phí:  Biên lai in thử</th>\n" +
                "  </tr>\n" +
                "</table>  <h1 style=\"text-align: center\">\n" +
                "    -----------------------------\n" +
                "  </h1>" +
                "\n" +
                "</body>\n" +
                "</html>";
        new converHTMLTask().execute(msg);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUE_ADMIN) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Cấp quyền thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Vui lòng cấp quyền cho ứng dụng", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkPrinter() {
        if (mPOSPrinter.getState() != com.vnpt.printproject.pos58bus.BluetoothService.STATE_CONNECTED) {
            Toast.makeText(getContext(), R.string.not_connected, Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(getContext(), R.string.connected, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    class ProductListViewAdapter extends BaseAdapter {

        //Dữ liệu liên kết bởi Adapter là một mảng các sản phẩm
        final ArrayList<KhachHang> listKhachHang;

        ProductListViewAdapter(ArrayList<KhachHang> listKhachHang) {
            this.listKhachHang = listKhachHang;
        }

        @Override
        public int getCount() {
            //Trả về tổng số phần tử, nó được gọi bởi ListView
            return listKhachHang.size();
        }

        @Override
        public Object getItem(int position) {
            //Trả về dữ liệu ở vị trí position của Adapter, tương ứng là phần tử
            //có chỉ số position trong listProduct
            return listKhachHang.get(position);
        }

        @Override
        public long getItemId(int position) {
            //Trả về một ID của phần
            return listKhachHang.get(position).getID();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //convertView là View của phần tử ListView, nếu convertView != null nghĩa là
            //View này được sử dụng lại, chỉ việc cập nhật nội dung mới
            //Nếu null cần tạo mới

            View viewProduct;
            if (convertView == null) {
                viewProduct = View.inflate(parent.getContext(), R.layout.product_view, null);
            } else viewProduct = convertView;

            //Bind sữ liệu phần tử vào View
            KhachHang khachHang = (KhachHang) getItem(position);
            ((TextView) viewProduct.findViewById(R.id.idproduct)).setText(String.format("STT : %d", khachHang.getID()));
            if (khachHang.getNAME().length() == 0 && khachHang.getCUSNAME().length() > 0) {
                ((TextView) viewProduct.findViewById(R.id.nameproduct)).setText(String.format("Tên KH : %s", khachHang.getCUSNAME()));
            } else {
                ((TextView) viewProduct.findViewById(R.id.nameproduct)).setText(String.format("Tên KH : %s", khachHang.getNAME()));
            }
            ((TextView) viewProduct.findViewById(R.id.diachi)).setText(String.format("Địa chỉ : %s", khachHang.getDIACHI()));
            ((TextView) viewProduct.findViewById(R.id.priceproduct)).setText(String.format("Mệnh giá: %d", khachHang.getSOTIEN()));


            return viewProduct;
        }
    }

}