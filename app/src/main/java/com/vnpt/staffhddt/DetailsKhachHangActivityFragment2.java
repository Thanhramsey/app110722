package com.vnpt.staffhddt;

import static com.vnpt.staffhddt.MainPos58Activity.mPOSPrinter;
import static com.vnpt.utils.Helper.hideSoftKeyboard;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.vnpt.printproject.pos58bus.command.sdk.Command;
import com.vnpt.printproject.pos58bus.command.sdk.PrintPicture;
import com.vnpt.printproject.pos58bus.command.sdk.PrinterCommand;
import com.vnpt.room.KhachHang;
import com.vnpt.room.LoaiPhi;
import com.vnpt.staffhddt.fragment.BaseFragment;
import com.vnpt.utils.DialogUtils;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.utils.StringBienLai;
import com.vnpt.webservice.AppServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsKhachHangActivityFragment2 extends BaseFragment implements View.OnClickListener, OnEventControlListener {
    private static final int REQUEST_BLUE_ADMIN = 888;
    Spinner spMenhGia,tuThang,denThang;
    MaterialEditText edtSoLuong, edtDiaChi, edtTenKhachHang, edtSoLuongThang, edtMenhgia;
    EditText tuNam,denNam;
    Button btnXuatBL, btnInThu, btnCheckTB;
    TextView txtCompanyInfo;
    private AwesomeProgressDialog dg;
    StoreSharePreferences preferences = null;

    CheckBox checkBox1,checkBox2,checkBox3,checkBox4,checkBox5,checkBox6,checkBox7,checkBox8,checkBox9,checkBox10,checkBox11,checkBox12,qui1,qui2,qui3,qui4;

    List<LoaiPhi> loaiPhiList;
    LoaiPhi loaiPhi = null;List<Integer> listThang=new ArrayList<>();

    int tuThangVal,denThangVal ;
    KhachHang khachHang;

    int soThang = 0;


    private DetailsKhachHangActivityFragment2.GetInvTask getInvTask = null;

    public static String TAG = DetailsKhachHangActivityFragment2.class.getName();

    private static final String[] PRICES = new String[]{
            "10.000"
    };

    private int type;

    public DetailsKhachHangActivityFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_detail_khachhang_2, container, false);
        Bundle args = getArguments();
        if (args != null) {
            khachHang = (KhachHang) args.getSerializable("KEY_DATA_KHACHHANG");
        }

        setupUI(layout.findViewById(R.id.layout_frament_detaikhachhang_2));
        init(layout);

        setEventForMembers();
        setValueForMembers();
//        ((DetailsActivity) getActivity()).showProccessbar(false);

        preferences = StoreSharePreferences.getInstance(getContext());

        return layout;
    }

    @Override
    protected void init(View layout) {
//        spMenhGia = layout.findViewById(R.id.spMenhGia);
        tuThang = layout.findViewById(R.id.tuThang);
        denThang= layout.findViewById(R.id.denThang);
//        edtSoLuong = layout.findViewById(R.id.edtSoLuong);
        edtSoLuongThang = layout.findViewById(R.id.edtSoLuongThang);

        edtTenKhachHang = layout.findViewById(R.id.edtTenKhachHang);
        edtDiaChi = layout.findViewById(R.id.edtDiaChi);
        edtMenhgia = layout.findViewById(R.id.edtMenhGia);

        checkBox1 = layout.findViewById(R.id.checkbox_id1);
        checkBox2 = layout.findViewById(R.id.checkbox_id2);
        checkBox3 = layout.findViewById(R.id.checkbox_id3);
        checkBox4 = layout.findViewById(R.id.checkbox_id4);
        checkBox5 = layout.findViewById(R.id.checkbox_id5);
        checkBox6 = layout.findViewById(R.id.checkbox_id6);
        checkBox7 = layout.findViewById(R.id.checkbox_id7);
        checkBox8 = layout.findViewById(R.id.checkbox_id8);
        checkBox9 = layout.findViewById(R.id.checkbox_id9);
        checkBox10 = layout.findViewById(R.id.checkbox_id10);
        checkBox11 = layout.findViewById(R.id.checkbox_id11);
        checkBox12 = layout.findViewById(R.id.checkbox_id12);

        qui1 = layout.findViewById(R.id.qui_1);
        qui2 = layout.findViewById(R.id.qui_2);
        qui3 = layout.findViewById(R.id.qui_3);
        qui4 = layout.findViewById(R.id.qui_4);



//        tuThang = layout.findViewById(R.id.tuThang);
        tuNam = layout.findViewById(R.id.tuNam);
//        denThang = layout.findViewById(R.id.denThang);
        denNam = layout.findViewById(R.id.denNam);

        btnXuatBL = layout.findViewById(R.id.btnXuatBienLai);
        btnInThu = layout.findViewById(R.id.btnInThu);
        btnCheckTB = layout.findViewById(R.id.btnCheck);
//        txtCompanyInfo = layout.findViewById(R.id.txtCompanyInfo);

//        tuNam.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
//        denNam.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
//        tuNam.setEnabled(false);
//        denNam.setEnabled(false);
//        tuNam.setVisibility(View.GONE);
//        denNam.setVisibility(View.GONE);

    }

    @Override
    protected void setValueForMembers() {
        String name = StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_COMPANY_NAME);
        String mst = StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_MST);
//        txtCompanyInfo.setText(String.format("%s - MST: %s", name, mst));

        type = getArguments().getInt(Common.KEY_COMPANY_TYPE);

        if (type == Common.TYPE_VE) {
            btnXuatBL.setText(R.string.xuat_ve);
        } else {
            btnXuatBL.setText(R.string.xuat_bien_lai);
        }

        if(khachHang != null){
            edtTenKhachHang.setText(khachHang.getNAME()!= null ? khachHang.getNAME() :"");
            edtDiaChi.setText(khachHang.getDIACHI()!=null ? khachHang.getDIACHI():"");
            edtMenhgia.setText(khachHang.getSOTIEN() != null ? String.valueOf(khachHang.getSOTIEN()*1000) : "");
        }
    }

    public void checkForCheckbox(CheckBox checkBox){
        if (checkBox.isChecked())
        {
            //do not thing
        }
        else
        {
            checkBox.setChecked(true);
            soThang++;
            updateSothang(soThang);
        }
    }
    public void checkForCheckbox2(CheckBox checkBox){
        if (checkBox.isChecked())
        {
            checkBox.setChecked(false);
            soThang--;
            updateSothang(soThang);
        }
        else
        {
            //do not thing
        }

    }

    @Override
    protected void setEventForMembers() {
        btnXuatBL.setOnClickListener(this);
        btnInThu.setOnClickListener(this);
        btnCheckTB.setOnClickListener(this);

        qui1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    checkForCheckbox(checkBox1);
                    checkForCheckbox(checkBox2);
                    checkForCheckbox(checkBox3);
                }
                else{
                    checkForCheckbox2(checkBox1);
                    checkForCheckbox2(checkBox2);
                    checkForCheckbox2(checkBox3);
                }
            }
        });

        qui2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    checkForCheckbox(checkBox4);
                    checkForCheckbox(checkBox5);
                    checkForCheckbox(checkBox6);
                }
                else{
                    checkForCheckbox2(checkBox4);
                    checkForCheckbox2(checkBox5);
                    checkForCheckbox2(checkBox6);
                }
            }
        });

        qui3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    checkForCheckbox(checkBox7);
                    checkForCheckbox(checkBox8);
                    checkForCheckbox(checkBox9);
                }
                else{
                    checkForCheckbox2(checkBox7);
                    checkForCheckbox2(checkBox8);
                    checkForCheckbox2(checkBox9);
                }
            }
        });

        qui4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    checkForCheckbox(checkBox10);
                    checkForCheckbox(checkBox11);
                    checkForCheckbox(checkBox12);
                }
                else{
                    checkForCheckbox2(checkBox10);
                    checkForCheckbox2(checkBox11);
                    checkForCheckbox2(checkBox12);
                }
            }
        });


        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    soThang++;updateSothang(soThang);
                }
                else{
                    soThang--;updateSothang(soThang);
                }
            }
        });
        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    soThang++;updateSothang(soThang);
                }
                else{
                    soThang--;updateSothang(soThang);
                }
            }
        });
        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    soThang++;updateSothang(soThang);
                }
                else{
                    soThang--;updateSothang(soThang);
                }
            }
        });
        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    soThang++;updateSothang(soThang);
                }
                else{
                    soThang--;updateSothang(soThang);
                }
            }
        });
        checkBox5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    soThang++;updateSothang(soThang);
                }
                else{
                    soThang--;updateSothang(soThang);
                }
            }
        });
        checkBox6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    soThang++;updateSothang(soThang);
                }
                else{
                    soThang--;updateSothang(soThang);
                }
            }
        });
        checkBox7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    soThang++;updateSothang(soThang);
                }
                else{
                    soThang--;updateSothang(soThang);
                }
            }
        });
        checkBox8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    soThang++;updateSothang(soThang);
                }
                else{
                    soThang--;updateSothang(soThang);
                }
            }
        });

        checkBox9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    soThang++;updateSothang(soThang);
                }
                else{
                    soThang--;updateSothang(soThang);
                }
            }
        });
        checkBox10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    soThang++;updateSothang(soThang);
                }
                else{
                    soThang--;updateSothang(soThang);
                }
            }
        });
        checkBox11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    soThang++;updateSothang(soThang);
                }
                else{
                    soThang--;updateSothang(soThang);
                }
            }
        });
        checkBox12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    soThang++;updateSothang(soThang);
                }
                else{
                    soThang--;updateSothang(soThang);
                }
            }
        });

//        spMenhGia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                loaiPhi = loaiPhiList.get(i);
////                Toast.makeText(getContext(), loaiPhi.getPATTERN(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                Toast.makeText(getContext(), "Vui lòng chọn ít nhất một loại phí", Toast.LENGTH_SHORT).show();
//            }
//        });


//        tuThang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                tuThangVal = listThang.get(i);
//                edtSoLuongThang.setText(String.valueOf(denThangVal-tuThangVal +1 ));
////                Toast.makeText(getContext(), loaiPhi.getPATTERN(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                Toast.makeText(getContext(), "Vui lòng chọn tháng", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        denThang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                denThangVal = listThang.get(i);
//                edtSoLuongThang.setText(String.valueOf(denThangVal-tuThangVal +1 ));
////                Toast.makeText(getContext(), loaiPhi.getPATTERN(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                Toast.makeText(getContext(), "Vui lòng chọn tháng", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void handleModelViewEvent(ModelEvent modelEvent) {
    }

    @Override
    public void handleErrorModelViewEvent(ModelEvent modelEvent) {

    }
    public void updateSothang(int value)
    {
        edtSoLuongThang.setText(String.valueOf(value));
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = getActivity().getIntent();
//        listThang.add(1);
//        listThang.add(2);
//        listThang.add(3);
//        listThang.add(4);
//        listThang.add(5);
//        listThang.add(6);
//        listThang.add(7);
//        listThang.add(8);
//        listThang.add(9);
//        listThang.add(10);
//        listThang.add(11);
//        listThang.add(12);
//        ArrayAdapter<Integer> adapter1 = new ArrayAdapter<Integer>(getContext(),
//                android.R.layout.simple_dropdown_item_1line,listThang );
//        tuThang.setAdapter(adapter1);
//        tuThang.setSelection(0);
//        denThang.setAdapter(adapter1);
//        denThang.setSelection(0);

    }

    private void attemptGetInv() {
        if (getInvTask != null) {
            return;
        }

        String num = edtSoLuongThang.getText().toString().trim();
        String diaChi = edtDiaChi.getText().toString().trim();
        String tenKhachHang = edtTenKhachHang.getText().toString().trim();
        String soThang = edtSoLuongThang.getText().toString().trim();
        Integer soTien = Integer.parseInt(edtMenhgia.getText().toString());


//        String tuThangVal = tuThang.getText().toString().trim();
        String tuNamVal = tuNam.getText().toString().trim();
//        String denThangVal = denThang.getText().toString().trim();
        String denNamVal = denNam.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(edtSoLuongThang.getText().toString().trim())) {
            edtSoLuongThang.setError(getString(R.string.error_empty_input));
            focusView = edtSoLuongThang;
            cancel = true;
        } else if(TextUtils.isEmpty(diaChi)){
            edtDiaChi.setError(getString(R.string.error_empty_input));
            focusView = edtDiaChi;
            cancel = true;
        }else if(TextUtils.isEmpty(tenKhachHang)){
            edtTenKhachHang.setError(getString(R.string.error_empty_input));
            focusView = edtTenKhachHang;
            cancel = true;
        }else if(TextUtils.isEmpty(soThang)){
            edtSoLuongThang.setError(getString(R.string.error_empty_input));
            focusView = edtSoLuongThang;
            cancel = true;
        }else if(TextUtils.isEmpty(tuNamVal)){
            tuNam.setError(getString(R.string.error_empty_input));
            focusView = tuNam;
            cancel = true;
        }else if(TextUtils.isEmpty(denNamVal)){
            denNam.setError(getString(R.string.error_empty_input));
            focusView = denNam;
            cancel = true;
        }else if(Integer.valueOf(edtSoLuongThang.getText().toString().trim()) < 0){
            edtSoLuongThang.setError("Vui lòng chọn từ tháng nhỏ hơn đến tháng");
            focusView = edtSoLuongThang;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            int numOfInv = Integer.parseInt(num);
            StringBuilder sb = new StringBuilder();

            //lap lai so ve
            for (int i = 1; i <= numOfInv; i++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                    long currentTime = System.currentTimeMillis();
                    String keyData = i + "_INVE" + currentTime;
                    // mau XML o day
                    String xmlChildData = "";
                    if (type == Common.TYPE_VE) {
                        xmlChildData = "<Inv><key>" + keyData + "</key><Invoice><CusCode>" + StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME) + "</CusCode><Buyer>"+ tenKhachHang +"</Buyer><CusName></CusName><CusAddress>"+ diaChi +"</CusAddress><CusPhone></CusPhone><CusTaxCode></CusTaxCode><PaymentMethod>TM, CK </PaymentMethod><KindOfService></KindOfService><Extra>"+ soThang +"</Extra><Extra1>"+ tuThangVal +"</Extra1><Extra2>"+ tuNamVal +"</Extra2><Extra3>"+ denThangVal +"</Extra3><Extra4>"+ denNamVal +"</Extra4><Products><Product><ProdName>" + loaiPhi.getNAME() + "</ProdName><ProdUnit></ProdUnit><ProdQuantity></ProdQuantity><ProdPrice>" + loaiPhi.getTOTAL() + "</ProdPrice><Total>" + loaiPhi.getTOTAL() + "</Total><Extra1></Extra1><Extra2></Extra2></Product></Products><Total>" + loaiPhi.getTOTAL() + "</Total><VATRate>"+
                                loaiPhi.getVAT_RATE() + "</VATRate><VATAmount>" + loaiPhi.getVAT_AMOUNT() + "</VATAmount><Amount>" + loaiPhi.getAMOUNT() + "</Amount><AmountInWords>"+ StringBienLai.docSo(loaiPhi.getAMOUNT()) +"</AmountInWords></Invoice></Inv>";
                    } else {
                        xmlChildData = "<Inv><key>" + keyData + "</key><Invoice><CusCode><![CDATA[" + keyData + "]]></CusCode><CusName><![CDATA[" + StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME) + "]]></CusName><CusAddress><![CDATA[]]></CusAddress><CusPhone></CusPhone><CusTaxCode></CusTaxCode><PaymentMethod><![CDATA[]]></PaymentMethod><Products><Product><Code><![CDATA[]]></Code><ProdName>"+loaiPhi.getNAME()+"</ProdName><ProdUnit>Lần</ProdUnit><ProdQuantity>1</ProdQuantity><ProdPrice>"+loaiPhi.getAMOUNT()+"</ProdPrice><Amount>1</Amount></Product></Products><KindOfService><![CDATA[]]></KindOfService><Total>"+loaiPhi.getTOTAL()+"</Total><Amount>"+loaiPhi.getAMOUNT()+"</Amount><AmountInWords>"+StringBienLai.docSo(loaiPhi.getAMOUNT())+"</AmountInWords></Invoice></Inv>";
                    }
                    /////////////////////////
                    sb.append(xmlChildData);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            String xmlData = "<Invoices>" + sb.toString() + "</Invoices>";

            getInvTask = new DetailsKhachHangActivityFragment2.GetInvTask(
                    StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME),
                    StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_PASS),
                    loaiPhi.getPATTERN(),
                    loaiPhi.getSERIAL(),
                    0,
                    xmlData);
            getInvTask.execute((Void) null);
        }
    }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnXuatBienLai: {
                if (mPOSPrinter.isBTActivated()) {
                    attemptGetInv();
                } else {
                    Toast.makeText(getContext(), "Vui lòng bật bluetooth và kết nối máy in", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.btnInThu: {
                if (mPOSPrinter.isBTActivated()) {
                    Print_Test();
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

                printInvoice(listResults);
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

    public void printInvoice(String[] list) {

        float price = loaiPhi.getAMOUNT() * (list.length);
        String firstIdx = list[0].split("_")[2];
        String lastIndx = list[list.length - 1].split("_")[2];

        int soBL = list.length;
        BienLai bienLai = new BienLai();
        bienLai.setMoTa(loaiPhi.getNAME());
        bienLai.setGiaTien(price);
        bienLai.setMau(loaiPhi.getPATTERN());
        bienLai.setSo(firstIdx + " -> " + lastIndx);
        bienLai.setKyHieu(loaiPhi.getSERIAL());
        bienLai.setPortal(StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_COMPANY_PORTAL));

        byte reset[] = {0x1b, 0x40};
        byte lineFeed[] = {0x00, 0x0A};
        // Command -- Font Size, Alignment
        byte normalSize[] = {0x1D, 0x21, 0x00};
        byte dWidthSize[] = {0x1D, 0x21, 0x10};
        byte dHeightSize[] = {0x1D, 0x21, 0x01};
        byte rightAlign[] = {0x1B, 0x61, 0x02};
        byte centerAlign[] = {0x1B, 0x61, 0x01};
        byte leftAlign[] = {0x1B, 0x61, 0x00};

        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        //Get current locale information
//        Locale currentLocale = Locale.getDefault();
//
//        //Get currency instance from locale; This will have all currency related information
//        Currency currentCurrency = Currency.getInstance(currentLocale);
//
//        //Currency Formatter specific to locale
//        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);

        String name = (type == Common.TYPE_VE) ? "VÉ ĐIỆN TỬ" :  "BIÊN LAI ĐIỆN TỬ";

//        String html = "<html>\n" +
//                "                <body>\n" +
//                "                <style>\n" +
//                "                 </style>\n" +
//                "<div style=\"text-align:center;\">"+
//                "                   <h2 style=\\\"text-align: center;font-size: 40px\\\">"+StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_COMPANY_NAME)+"</h2>\n" +
//                "</div>"+
//                "<div style=\"text-align:right;\">"+
//                "                   <h2 style=\\\"text-align: center;font-size: 40px\\\">Ký hiệu: "+bienLai.getKyHieu()+"</h2>\n" +
//                "</div>"+
//                "<div style=\"text-align:center;\">"+
//                "                       <h2 style=\\\"text-align: center;font-size: 40px\\\">MST: "+ StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_MST) +
//                "               <h1 style=\\\"text-align: center;font-size: 40px\\\">"+name+"</h1>\n" +
//                "</div>"+
//                "<div style=\"text-align:center;\">"+
//                "<h3 style='text-align:center;font-size: 25px'>("+loaiPhi.getNAME()+")</h3>" +
//                "</div>"+
//               // "<h3 style='text-align:right;font-size: 20px'>(BLĐT này không thay thế cho BL thu phí, lệ phí)</h3>" +
//
//                "                \n" +"<h2 style=\\\"text-align: center;font-size: 40px\\\">Họ tên người nộp: "+edtTenKhachHang.getText().toString().trim()+"</h2>"+
//                "               <h2 style=\\\"text-align: center;font-size: 40px\\\">Địa chỉ: "+edtDiaChi.getText().toString().trim()+"</h2>\n" +
////                "               <h2 style=\\\"text-align:right;font-size: 40px\\\">Mẫu: "+ bienLai.getMau() +"</h2>\n" +
////                "                   <h2 style=\\\"text-align:right;font-size: 40px\\\">Ký hiệu: "+bienLai.getKyHieu()+"</h2>\n" +
////                "                    <h2 style=\\\"text-align:right;font-size: 40px\\\">Số vé: "+bienLai.getSo()+"</h2>\n" +
////                "                    <h2 style=\\\"text-align: right;font-size: 40px\\\">SL: "+soBL+"</h2>\n" +
//                "<table style=\"text-align: center;width:100%;\">\n" +
//                "  <tr>\n" +
//                "    <th style=\"font-size:25px;border:1px solid black;border-right:none;border-bottom:none;\">Số tháng</th>\n" +
//                "    <th style=\"font-size:25px;border:1px solid black;border-right:none;border-bottom:none;\">Mệnh giá</th>\n" +
//                "    <th style=\"font-size:25px;border:1px solid black;border-bottom:none;\">Tổng tiền</th>\n" +
//                "  </tr>\n" +
//                "  <tr>\n" +
//                "    <td style=\"font-size:30px;border:1px solid black;border-right:none;\">"+soBL+"</th>\n" +
//                "    <td style=\"font-size:30px;border:1px solid black;border-right:none;\">"+Math.round(loaiPhi.getAMOUNT())+"</th>\n" +
//                "    <td style=\"font-size:30px;border:1px solid black;\">"+ Math.round(bienLai.getGiaTien()) +"</th>\n" +
//                "  </tr>\n" +
//                "</table>  <h1 style=\"text-align: center\">\n" +
////                "                    <h2 style=\\\"text-align: right;font-size: 40px\\\">Số tháng đóng: "+edtSoLuongThang.getText().toString().trim()+"</h2>\n" +
//                "                    <h3 style=\\\"text-align: right;font-size: 25px\\\">Từ tháng: "+tuThangVal+ "/"+tuNam.getText().toString().trim()+ "  Đến tháng: "+denThangVal+ "/"+denNam.getText().toString().trim()+ "  </h3>\n" +
////                "                    <h2>Giá: "+ Math.round(bienLai.getGiaTien()) +" VND</h2>\n" +
//                "                    <h2>("+ StringBienLai.docSo(bienLai.getGiaTien()) +")</h2>" +
//                "<div style=\"text-align:center;\">"+
//                "               <h2 style=\\\"text-align: center;font-size: 40px\\\">Ngày: "+date+"</h2>\n" +
//
//                "                    <h2 style=\\\"text-align: center;font-size: 40px\\\">Tên người thu</h2>\n" +
//                "</div>"+
//                "               <h1 style=\\\"text-align: center\\\"> ----------------------------- </h1>" +
//                "</body></html>";


        String html = "<html>\n" +
                "                <body>\n" +
                "                <style>\n" +
                "                 </style>\n" +
                "<div style=\"text-align:center;font-size:26px;\">"+
                "<b> "+StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_COMPANY_NAME)+" </b>\n" +
                "</div>"+
                "<div style=\"text-align:right;font-size: 20px\">"+
                "<b>  Ký hiệu: "+bienLai.getKyHieu()+"</b>\n" +
                "</div>"+
                "<div style=\"text-align:center;font-size: 25px\">"+
                "<b>  MST: "+ StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_MST) +"</b> \n" +
                "</div>"+
                "<div style=\"text-align:center;font-size: 28px\">"+
                "<b>  "+name+"</b> \n" +
                "</div>"+
                "<div style=\"text-align:center;font-size: 25px\">"+
                "<b>("+loaiPhi.getNAME()+")</b>" +
                "</div>"+
                "<div style=\"text-align:left;font-size: 23px\">"+
                "<b> Họ tên người nộp: "+edtTenKhachHang.getText().toString().trim()+"</b> " +
                "</div>"+
                "<div style=\"text-align:left;font-size: 23px\">"+
                "<b> Địa chỉ: "+edtDiaChi.getText().toString().trim()+" </b>" +
                "</div>"+
                "<table style=\"text-align: center;width:100%;\">\n" +
                "  <tr>\n" +
                "    <th style=\"font-size:25px;border:1px solid black;border-right:none;border-bottom:none;\">Số tháng</th>\n" +
                "    <th style=\"font-size:25px;border:1px solid black;border-right:none;border-bottom:none;\">Mệnh giá</th>\n" +
                "    <th style=\"font-size:25px;border:1px solid black;border-bottom:none;\">Tổng tiền</th>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td style=\"font-size:30px;border:1px solid black;border-right:none;\">"+soBL+"</th>\n" +
                "    <td style=\"font-size:30px;border:1px solid black;border-right:none;\">"+Math.round(loaiPhi.getAMOUNT())+"</th>\n" +
                "    <td style=\"font-size:30px;border:1px solid black;\">"+ Math.round(bienLai.getGiaTien()) +"</th>\n" +
                "  </tr>\n" +
                "</table>  \n" +
                "<div style=\"text-align:left;font-size: 20px\">"+
                "<b> Từ tháng: "+tuThangVal+ "/"+tuNam.getText().toString().trim()+ "  Đến tháng: "+denThangVal+ "/"+denNam.getText().toString().trim()+ " </b> " +
                "</div>"+
                "<div style=\"text-align:left;font-size: 20px\">"+
                "<b> ("+ StringBienLai.docSo(bienLai.getGiaTien()) +") </b> " +
                "</div>"+
                "<div style=\"text-align:center;font-size: 20px\">"+
                "<b> Ngày: "+date+"</b>\n" +
                "</div>"+
                "<div style=\"text-align:center;font-size: 20px\">"+
                "<b>    Tên người thu</b>\n" +
                "</div>"+
                "<div style=\"text-align:center;\">"+
                " <h1 style=\\\"text-align: center\\\"> ----------------------------- </h1>" +
                "</div>"+
                "</body></html>";

//        StringBuilder endContent = new StringBuilder();

//        for (String inv: list) {
//            endContent.append("<h3>").append(inv).append("</h3>");
//        }
//
//        html += endContent.toString() + "</body></html>";

        new DetailsKhachHangActivityFragment2.converHTMLTask().execute(html);

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

    private void Print_BMP(Bitmap mBitmap){
        //	byte[] buffer = PrinterCommand.POS_Set_PrtInit();
        int nMode = 0;
        int nPaperWidth = 384;

        if(mBitmap != null)
        {
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

    private void Print_Test() {
//        String msg = "<html>\n" +
//                "                <body>\n" +
//                "                <style>\n" +
//                "                 </style>\n" +
//                "<div style=\"text-align:center;font-size: 28px\">"+
//                " <b>  BAN QUẢN LÝ CHỢ HUYỆN ĐAKĐOA</b>\n" +
//                "</div>"+
//                "<div style=\"text-align:right;font-size: 20px\">"+
//                " <b> Ký hiệu: KHTK-001</b> \n" +
//                "</div>"+
//                "<div style=\"text-align:center;font-size: 25px\">"+
//                "<b>MST: "+ StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_MST) +"</b> "+
//                "</div>"+
//                "<div style=\"text-align:center;font-size: 30px\">"+
//                "  <b> BAN QUẢN LÝ CHỢ HUYỆN ĐAKĐOA</b>\n" +
//                "</div>"+
//                "<div style=\"text-align:center;font-size: 28px\">"+
//                "<b>("+loaiPhi.getNAME()+")</b>"+
//                "</div>"+
//                "<div style=\"text-align:left;font-size: 23px\">"+
//                "<b> Họ tên người nộp: "+edtTenKhachHang.getText().toString().trim()+"</b> "+
//                "</div>"+
//                "<div style=\"text-align:left;font-size: 23px\">"+
//                "<b> Địa chỉ: "+edtDiaChi.getText().toString().trim()+"</b> "+
//                "</div>"+
//                "<table style=\"text-align: center;width:100%;\">\n" +
//                "  <tr>\n" +
//                "    <th style=\"font-size:25px;border:1px solid black;border-right:none;border-bottom:none;\">Số tháng</th>\n" +
//                "    <th style=\"font-size:25px;border:1px solid black;border-right:none;border-bottom:none;\">Mệnh giá</th>\n" +
//                "    <th style=\"font-size:25px;border:1px solid black;border-bottom:none;\">Tổng tiền</th>\n" +
//                "  </tr>\n" +
//                "  <tr>\n" +
//                "    <td style=\"font-size:30px;border:1px solid black;border-right:none;\">10</th>\n" +
//                "    <td style=\"font-size:30px;border:1px solid black;border-right:none;\">"+Math.round(loaiPhi.getAMOUNT())+"</th>\n" +
//                "    <td style=\"font-size:30px;border:1px solid black;\">15000000</th>\n" +
//                "  </tr>\n" +
//                "</table> \n" +
//                "<div style=\"text-align:left;font-size: 20px\">"+
//                " <b> Từ tháng: "+tuThangVal+ "/"+tuNam.getText().toString().trim()+ "  Đến tháng: "+denThangVal+ "/"+denNam.getText().toString().trim()+ "</b>\n" +
//                "</div>"+
//                "<div style=\"text-align:left;font-size: 20px\">"+
//                " <b>  (Một triệu năm trăm ngàn) </b>\n" +
//                "</div>"+
////                "                    <h2 style=\\\"text-align: right;font-size: 40px\\\">Số tháng đóng: "+edtSoLuongThang.getText().toString().trim()+"</h2>\n" +
////                "                    <h3 style=\\\"text-align: right;font-size: 25px\\\">Từ tháng: "+tuThangVal+ "/"+tuNam.getText().toString().trim()+ "  Đến tháng: "+denThangVal+ "/"+denNam.getText().toString().trim()+ "  </h3>\n" +
////                "                    <h2>Giá: "+ Math.round(bienLai.getGiaTien()) +" VND</h2>\n" +
////                "                    <h2>("+ StringBienLai.docSo(bienLai.getGiaTien()) +")</h2>" +
//                "<div style=\"text-align:center;font-size: 20px\">"+
//                "<b>Ngày: 04-07-2020 </b>\n" +
//                "</div>"+
//                "<div style=\"text\"text-align:center;font-size: 20px\">"+
//                "------------align:center;font-size: 20px\\\">\"+\n" +
//                "                \"<b>Tên người thu</b>\"+\n" +
//                "                \"</div>\"+\n" +
//                "                \"<div style=------------------"+
//                "</div>"+
//                "</body></html>";


        String msg = "<html>\n" +
                "                <body>\n" +
                "                <style>\n" +
                "                 </style>\n" +
                "<div style=\"text-align:center;font-size:26px;\">"+
                " <b>"+StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_COMPANY_NAME)+"</b> \n" +
                "</div>"+
                "<div style=\"text-align:right;font-size: 20px\">"+
                " <b> Ký hiệu: KHTK-002</b>\n" +
                "</div>"+
                "<div style=\"text-align:center;font-size: 25px\">"+
                " <b> MST: "+ StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_MST) +"</b> \n" +
                "</div>"+
                "<div style=\"text-align:center;font-size: 28px\">"+
                " <b>"+StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_COMPANY_NAME)+"</b> \n" +
                "</div>"+
                "<div style=\"text-align:center;font-size: 26px\">"+
                "<b>(Vé mua hàng)</b>" +
                "</div>"+
                "<div style=\"text-align:left;font-size: 23px\">"+
                "<b> Họ tên người nộp: "+edtTenKhachHang.getText().toString().trim()+"</b> " +
                "</div>"+
                "<div style=\"text-align:left;font-size: 23px\">"+
                "<b> Địa chỉ: "+edtDiaChi.getText().toString().trim()+"</b> " +
                "</div>"+
                "<table style=\"text-align: center;width:100%;\">\n" +
                "  <tr>\n" +
                "    <th style=\"font-size:25px;border:1px solid black;border-right:none;border-bottom:none;\">Số tháng</th>\n" +
                "    <th style=\"font-size:25px;border:1px solid black;border-right:none;border-bottom:none;\">Mệnh giá</th>\n" +
                "    <th style=\"font-size:25px;border:1px solid black;border-bottom:none;\">Tổng tiền</th>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td style=\"font-size:30px;border:1px solid black;border-right:none;\">5</th>\n" +
                "    <td style=\"font-size:30px;border:1px solid black;border-right:none;\">"+edtMenhgia.getText().toString().trim()+"</th>\n" +
                "    <td style=\"font-size:30px;border:1px solid black;\">10000</th>\n" +
                "  </tr>\n" +
                "</table>  \n" +
                "<div style=\"text-align:left;font-size: 20px\">"+
                " <b>Từ tháng: "+tuThangVal+ "/"+tuNam.getText().toString().trim()+ "  Đến tháng: "+denThangVal+ "/"+denNam.getText().toString().trim()+ "</b>  " +
                "</div>"+
                "<div style=\"text-align:left;font-size: 20px\">"+
                " <b>(Mười ngàn đồng) </b> " +
                "</div>"+
                "<div style=\"text-align:center;font-size: 20px\">"+
                "<b> Ngày: 04-070-2022</b>\n" +
                "</div>"+
                "<div style=\"text-align:center;font-size: 20px\">"+
                " <b>    Tên người thu</b>\n" +
                "</div>"+
                "<div style=\"text-align:center;\">"+
                " <h1 style=\\\"text-align: center\\\"> ----------------------------- </h1>" +
                "</div>"+
                "</body></html>";

        new DetailsKhachHangActivityFragment2.converHTMLTask().execute(msg);
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
}
