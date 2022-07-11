package com.vnpt.staffhddt.er58;

import static com.vnpt.utils.Helper.hideSoftKeyboard;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vnpt.common.Common;
import com.vnpt.common.ModelEvent;
import com.vnpt.dto.BienLai;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.room.LoaiPhi;
import com.vnpt.staffhddt.MainEr58AiActivity;
import com.vnpt.staffhddt.R;
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

public class XuatVeGopDiaChiFragment extends BaseFragment implements View.OnClickListener, OnEventControlListener {

    private static final int REQUEST_BLUE_ADMIN = 888;
    Spinner spMenhGia,tuThang,denThang;
    MaterialEditText edtSoLuong, edtDiaChi, edtTenKhachHang, edtSoLuongThang;
    EditText tuNam,denNam;
    Button btnXuatBL, btnInThu, btnCheckTB;
    TextView txtCompanyInfo;
    private AwesomeProgressDialog dg;
    StoreSharePreferences preferences = null;

    List<LoaiPhi> loaiPhiList;
    LoaiPhi loaiPhi = null;List<Integer> listThang=new ArrayList<>();

    int tuThangVal,denThangVal ;

    private GetInvTask getInvTask = null;

    public static String TAG = XuatVeGopDiaChiFragment.class.getName();


    private static final String[] PRICES = new String[]{
            "10.000"
    };

    private int type;

    public XuatVeGopDiaChiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_xuat_ve_gop_diachi, container, false);

        setupUI(layout.findViewById(R.id.layout_frament_xuatvegopdiachi));
        init(layout);

        setEventForMembers();
        setValueForMembers();
        ((MainEr58AiActivity) getActivity()).showProccessbar(false);

        preferences = StoreSharePreferences.getInstance(getContext());

        return layout;
    }

    @Override
    protected void init(View layout) {
        spMenhGia = layout.findViewById(R.id.spMenhGia);
        tuThang = layout.findViewById(R.id.tuThang);
        denThang= layout.findViewById(R.id.denThang);
//        edtSoLuong = layout.findViewById(R.id.edtSoLuong);
        edtSoLuongThang = layout.findViewById(R.id.edtSoLuongThang);

        edtTenKhachHang = layout.findViewById(R.id.edtTenKhachHang);
        edtDiaChi = layout.findViewById(R.id.edtDiaChi);


//        tuThang = layout.findViewById(R.id.tuThang);
        tuNam = layout.findViewById(R.id.tuNam);
//        denThang = layout.findViewById(R.id.denThang);
        denNam = layout.findViewById(R.id.denNam);

        btnXuatBL = layout.findViewById(R.id.btnXuatBienLai);
        btnInThu = layout.findViewById(R.id.btnInThu);
        btnCheckTB = layout.findViewById(R.id.btnCheck);
        txtCompanyInfo = layout.findViewById(R.id.txtCompanyInfo);

        tuNam.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        denNam.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
//        tuNam.setEnabled(false);
//        denNam.setEnabled(false);
        tuNam.setVisibility(View.GONE);
        denNam.setVisibility(View.GONE);spMenhGia = layout.findViewById(R.id.spMenhGia);
        tuThang = layout.findViewById(R.id.tuThang);
        denThang= layout.findViewById(R.id.denThang);
//        edtSoLuong = layout.findViewById(R.id.edtSoLuong);
        edtSoLuongThang = layout.findViewById(R.id.edtSoLuongThang);

        edtTenKhachHang = layout.findViewById(R.id.edtTenKhachHang);
        edtDiaChi = layout.findViewById(R.id.edtDiaChi);


//        tuThang = layout.findViewById(R.id.tuThang);
        tuNam = layout.findViewById(R.id.tuNam);
//        denThang = layout.findViewById(R.id.denThang);
        denNam = layout.findViewById(R.id.denNam);

        btnXuatBL = layout.findViewById(R.id.btnXuatBienLai);
        btnInThu = layout.findViewById(R.id.btnInThu);
        btnCheckTB = layout.findViewById(R.id.btnCheck);
        txtCompanyInfo = layout.findViewById(R.id.txtCompanyInfo);

        tuNam.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        denNam.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
//        tuNam.setEnabled(false);
//        denNam.setEnabled(false);
        tuNam.setVisibility(View.GONE);
        denNam.setVisibility(View.GONE);
    }

    @Override
    protected void setValueForMembers() {
        String name = StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_COMPANY_NAME);
        String mst = StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_MST);
        txtCompanyInfo.setText(String.format("%s - MST: %s", name, mst));

        type = getArguments().getInt(Common.KEY_COMPANY_TYPE);

        if (type == Common.TYPE_VE) {
            btnXuatBL.setText(R.string.xuat_ve);
        } else {
            btnXuatBL.setText(R.string.xuat_bien_lai);
        }
    }

    @Override
    protected void setEventForMembers() {
        btnXuatBL.setOnClickListener(this);
        btnInThu.setOnClickListener(this);
        btnCheckTB.setOnClickListener(this);

        spMenhGia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loaiPhi = loaiPhiList.get(i);
//                Toast.makeText(getContext(), loaiPhi.getPATTERN(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getContext(), "Vui lòng chọn ít nhất một loại phí", Toast.LENGTH_SHORT).show();
            }
        });
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
            if (loaiPhiList != null && loaiPhiList.size() > 0) {
                ArrayAdapter<LoaiPhi> adapter = new ArrayAdapter<LoaiPhi>(getContext(),
                        android.R.layout.simple_dropdown_item_1line, loaiPhiList);
                spMenhGia.setAdapter(adapter);
            } else {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_dropdown_item_1line, PRICES);
                spMenhGia.setAdapter(adapter);
            }
            spMenhGia.setSelection(0);
            listThang.add(1);
            listThang.add(2);
            listThang.add(3);
            listThang.add(4);
            listThang.add(5);
            listThang.add(6);
            listThang.add(7);
            listThang.add(8);
            listThang.add(9);
            listThang.add(10);
            listThang.add(11);
            listThang.add(12);
            ArrayAdapter<Integer> adapter1 = new ArrayAdapter<Integer>(getContext(),
                    android.R.layout.simple_dropdown_item_1line,listThang );
            tuThang.setAdapter(adapter1);
            tuThang.setSelection(0);
            denThang.setAdapter(adapter1);
            denThang.setSelection(0);
        }
    }

    private void attemptGetInv() {
        if (getInvTask != null) {
            return;
        }

        String num = edtSoLuongThang.getText().toString().trim();
        String diaChi = edtDiaChi.getText().toString().trim();
        String tenKhachHang = edtTenKhachHang.getText().toString().trim();
        String soThang = edtSoLuongThang.getText().toString().trim();


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
                        xmlChildData = "<Inv>" +
                                "<key>" + keyData + "</key>" +
                                "<Invoice>" +
                                "<Buyer>" + tenKhachHang + "</Buyer>" +
                                "<CusCode>" + StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME) + "</CusCode>" +
                                "<CusName></CusName>" +
                                "<CusAddress>"+ diaChi +"</CusAddress>"+
                                "<CusPhone></CusPhone>" +
                                "<CusTaxCode></CusTaxCode>" +
                                "<PaymentMethod>TM, CK</PaymentMethod>" +
                                "<Extra1></Extra1>" +
                                "<CurrencyUnit>VND</CurrencyUnit>" +
                                "<KindOfService></KindOfService>" +
                                "<Extra>"+ soThang +"</Extra>"+
                                "<Extra1>"+tuThangVal+"</Extra1>"+
                                "<Extra2>"+tuNamVal+"</Extra2>"+
                                "<Extra3>"+denThangVal+"</Extra3>"+
                                "<Extra4>"+denNamVal+"</Extra4>"+
                                "<Products>" +
                                "<Product>" +
                                "<ProdName>" + loaiPhi.getNAME() + "</ProdName>" +
                                "<ProdUnit></ProdUnit>" +
                                "<ProdQuantity></ProdQuantity>" +
                                "<ProdPrice>" + loaiPhi.getTOTAL() + "</ProdPrice>" +
                                "<VATAmount>0</VATAmount>" +
                                "<Total>"+ loaiPhi.getTOTAL() +"</Total>" +
                                "<Amount>" + loaiPhi.getTOTAL() + "</Amount>" +
                                "<IsSum>0</IsSum>" +
                                "</Product>" +
                                "</Products>" +
                                "<GrossValue>0</GrossValue>" +
                                "<GrossValue0>0</GrossValue0>" +
                                "<VatAmount0>0</VatAmount0>" +
                                "<GrossValue5>0</GrossValue5>" +
                                "<VatAmount5>0</VatAmount5>" +
                                "<GrossValue10>0</GrossValue10>" +
                                "<VatAmount10>0</VatAmount10>" +
                                "<VATRate>"+loaiPhi.getVAT_RATE() + "</VATRate>" +
                                "<VATAmount>" + loaiPhi.getVAT_AMOUNT() + "</VATAmount>" +
                                "<Total>" + loaiPhi.getTOTAL() + "</Total>" +
                                "<Amount>" + loaiPhi.getAMOUNT() + "</Amount>" +
                                "<AmountInWords>"+ StringBienLai.docSo(loaiPhi.getAMOUNT()) +"</AmountInWords>" +
                                "</Invoice>" +
                                "</Inv>";
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

            getInvTask = new GetInvTask(
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
                if (MainEr58AiActivity.mPOSPrinter.isBTActivated()) {
                    attemptGetInv();
                } else {
                    Toast.makeText(getContext(), "Vui lòng bật bluetooth và kết nối máy in", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.btnInThu: {
                if (MainEr58AiActivity.mPOSPrinter.isBTActivated()) {
                    printInvoiceTest();
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
        ((MainEr58AiActivity) getActivity()).showProccessbar(false);
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
        String lastIndx = list[0].split("_")[2];
        String firstIdx = list[list.length - 1].split("_")[2];

        int soBL = list.length;
        BienLai bienLai = new BienLai();
        bienLai.setMoTa(loaiPhi.getNAME());
        bienLai.setGiaTien(price);
        bienLai.setMau(loaiPhi.getPATTERN());
        bienLai.setSo(firstIdx + " -> " + lastIndx);
        bienLai.setKyHieu(loaiPhi.getSERIAL());
        bienLai.setPortal(StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_COMPANY_PORTAL));

        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        byte reset[] = {0x1b, 0x40};
        byte lineFeed[] = {0x00, 0x0A};
        // Command -- Font Size, Alignment
        byte normalSize[] = {0x1D, 0x21, 0x00};
        byte dWidthSize[] = {0x1D, 0x21, 0x10};
        byte dHeightSize[] = {0x1D, 0x21, 0x01};
        byte rightAlign[] = {0x1B, 0x61, 0x02};
        byte centerAlign[] = {0x1B, 0x61, 0x01};
        byte leftAlign[] = {0x1B, 0x61, 0x00};

		/*//check if bluetooth is connected or not before any operation
		if (mPOSPrinter.getState() != mPOSPrinter.STATE_CONNECTED) {
			Toast.makeText(getApplicationContext(), "bluetooth is not connected yet", Toast.LENGTH_LONG).show();
			return;
		}*/

        MainEr58AiActivity.mPOSPrinter.setEncoding("utf-16LE");
        // send reset command first in front of every receipt
        MainEr58AiActivity.mPOSPrinter.sendByte(reset);

        //receipt content getString(R.string.type_app_receipt) + StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_COMPANY_NAME)
        MainEr58AiActivity.mPOSPrinter.printText(StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_COMPANY_NAME) + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_CENTER, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) (MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH | MainEr58AiActivity.mPOSPrinter.TXT_1HEIGHT));
        MainEr58AiActivity.mPOSPrinter.printText("MST: "+StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_MST) + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_CENTER, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) (MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH | MainEr58AiActivity.mPOSPrinter.TXT_1HEIGHT));

        if (type == Common.TYPE_VE) {
            MainEr58AiActivity.mPOSPrinter.printText("VÉ ĐIỆN TỬ\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_CENTER, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) (MainEr58AiActivity.mPOSPrinter.TXT_2WIDTH | MainEr58AiActivity.mPOSPrinter.TXT_2HEIGHT));
        } else {
            MainEr58AiActivity.mPOSPrinter.printText("BIÊN LAI ĐIỆN TỬ\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_CENTER, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) (MainEr58AiActivity.mPOSPrinter.TXT_2WIDTH | MainEr58AiActivity.mPOSPrinter.TXT_2HEIGHT));
        }
        MainEr58AiActivity.mPOSPrinter.printText("Phí: " + bienLai.getMoTa() + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_CENTER, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) 8);
//        MainEr58AiActivity.mPOSPrinter.printText("Tên khách hàng: " + edtTenKhachHang.getText().toString().trim() + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_LEFT, MainEr58AiActivity.mPOSPrinter.FNT_FONTB, (byte) (MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH | MainEr58AiActivity.mPOSPrinter.TXT_1HEIGHT));
//        MainEr58AiActivity.mPOSPrinter.printText("Địa chỉ: " + edtDiaChi.getText().toString().trim() + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_LEFT, MainEr58AiActivity.mPOSPrinter.FNT_FONTB, (byte) (MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH | MainEr58AiActivity.mPOSPrinter.TXT_1HEIGHT));
        MainEr58AiActivity.mPOSPrinter.printText("Tên khách hàng: " + edtTenKhachHang.getText().toString().trim() + "\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_LEFT, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        MainEr58AiActivity.mPOSPrinter.printText("Địa chỉ: " + edtDiaChi.getText().toString().trim() + "\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_LEFT, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        MainEr58AiActivity.mPOSPrinter.printText("STT:" + soBL + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_LEFT, MainEr58AiActivity.mPOSPrinter.FNT_FONTB, (byte) (MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH | MainEr58AiActivity.mPOSPrinter.TXT_1HEIGHT));
        MainEr58AiActivity.mPOSPrinter.printText("Số vé: " + bienLai.getSo() + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_LEFT, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        MainEr58AiActivity.mPOSPrinter.printText("Số tháng: " +  edtSoLuongThang.getText().toString().trim()  + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_LEFT, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        MainEr58AiActivity.mPOSPrinter.printText("Từ ngày: " +  tuThangVal+ " năm:" +  tuNam.getText().toString().trim()  + " Đến tháng:"+denThangVal+ " năm: "+denNam.getText().toString().trim() +"   \r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_LEFT, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        MainEr58AiActivity.mPOSPrinter.printText("Ngày: " + date + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_LEFT, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        MainEr58AiActivity.mPOSPrinter.printText("Mẫu: " + bienLai.getMau() + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_LEFT, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        MainEr58AiActivity.mPOSPrinter.printText("Ký hiệu: " + bienLai.getKyHieu() + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_LEFT, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        MainEr58AiActivity.mPOSPrinter.printText("Giá: " + Math.round(bienLai.getGiaTien()) + " VND\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_LEFT, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        MainEr58AiActivity.mPOSPrinter.printText("(" + StringBienLai.docSo(bienLai.getGiaTien()) + ")\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_LEFT, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        MainEr58AiActivity.mPOSPrinter.sendByte(centerAlign);
        MainEr58AiActivity.mPOSPrinter.printString("-------------------------------\r\n");
        if (type == Common.TYPE_VE) {
            MainEr58AiActivity.mPOSPrinter.printText("Để tra cứu vé gốc kính mời truy cập vào link : " + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_LEFT, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        } else {
            MainEr58AiActivity.mPOSPrinter.printText("Để tra cứu biên lai gốc kính mời truy cập vào link : " + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_LEFT, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        }
        MainEr58AiActivity.mPOSPrinter.printText(bienLai.getPortal() + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_LEFT, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        MainEr58AiActivity.mPOSPrinter.printText("Mã tra cứu: " + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_CENTER, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        for (String inv : list) {
            MainEr58AiActivity.mPOSPrinter.printText(inv + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_CENTER, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        }

        //feed paper to make sure receipt is exposed enough to tear off
        MainEr58AiActivity.mPOSPrinter.lineFeed(2);

    }


    public void printInvoiceTest() {
        //OK:01VEDB0/001;AM/20E-1_INVE16139758289057_12
        BienLai bienLai = new BienLai();
        bienLai.setMoTa("Vé in thử");
        bienLai.setGiaTien(10000f);
        bienLai.setMau(preferences.loadStringSavedPreferences(
                Common.KEY_DEFAULT_PATTERN_INVOICES));
        bienLai.setSo("0");
        bienLai.setKyHieu(preferences.loadStringSavedPreferences(
                Common.KEY_DEFAULT_SERIAL_INVOICES));

        byte reset[] = {0x1b, 0x40};
        byte lineFeed[] = {0x00, 0x0A};
        // Command -- Font Size, Alignment
        byte normalSize[] = {0x1D, 0x21, 0x00};
        byte dWidthSize[] = {0x1D, 0x21, 0x10};
        byte dHeightSize[] = {0x1D, 0x21, 0x01};
        byte rightAlign[] = {0x1B, 0x61, 0x02};
        byte centerAlign[] = {0x1B, 0x61, 0x01};
        byte leftAlign[] = {0x1B, 0x61, 0x00};

		/*//check if bluetooth is connected or not before any operation
		if (mPOSPrinter.getState() != mPOSPrinter.STATE_CONNECTED) {
			Toast.makeText(getApplicationContext(), "bluetooth is not connected yet", Toast.LENGTH_LONG).show();
			return;
		}*/

        MainEr58AiActivity.mPOSPrinter.setEncoding("utf-16LE");
        // send reset command first in front of every receipt
        MainEr58AiActivity.mPOSPrinter.sendByte(reset);

        //receipt content getString(R.string.type_app_receipt) +
        MainEr58AiActivity.mPOSPrinter.printText("VÉ ĐIỆN TỬ\r\n\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_CENTER, MainEr58AiActivity.mPOSPrinter.FNT_DEFAULT, (byte) (MainEr58AiActivity.mPOSPrinter.TXT_2WIDTH | MainEr58AiActivity.mPOSPrinter.TXT_2HEIGHT));
        //mPOSPrinter.printText("TEL (123)-456-7890\r\n",(byte)mPOSPrinter.ALIGNMENT_RIGHT,mPOSPrinter.FNT_FONTB, (byte)mPOSPrinter.TXT_1WIDTH);
        MainEr58AiActivity.mPOSPrinter.printText("Phí: " + bienLai.getMoTa() + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_CENTER, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        MainEr58AiActivity.mPOSPrinter.printText("Giá: " + bienLai.getGiaTien() + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_CENTER, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        MainEr58AiActivity.mPOSPrinter.printText("Số BL: " + bienLai.getSo() + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_CENTER, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        MainEr58AiActivity.mPOSPrinter.printText("Mẫu: " + bienLai.getMau() + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_CENTER, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        MainEr58AiActivity.mPOSPrinter.printText("Ký hiệu: " + bienLai.getKyHieu() + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_CENTER, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        MainEr58AiActivity.mPOSPrinter.printText("Tên khách hàng: " + edtTenKhachHang.getText().toString().trim() + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_CENTER, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        MainEr58AiActivity.mPOSPrinter.printText("Địa chỉ: " + edtDiaChi.getText().toString().trim() + "\r\n", (byte) MainEr58AiActivity.mPOSPrinter.ALIGNMENT_CENTER, MainEr58AiActivity.mPOSPrinter.FNT_BOLD, (byte) MainEr58AiActivity.mPOSPrinter.TXT_1WIDTH);
        MainEr58AiActivity.mPOSPrinter.sendByte(centerAlign);
        MainEr58AiActivity.mPOSPrinter.printString("-------------------------------\r\n");

        //feed paper to make sure receipt is exposed enough to tear off
        MainEr58AiActivity.mPOSPrinter.lineFeed(3);

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

    public void checkPrinter() {
        //check printer status before any operation.
        int result;
        result = MainEr58AiActivity.mPOSPrinter.checkPrinter();
        if (result == MainEr58AiActivity.mPOSPrinter.MP_SUCCESS) {
            int status;
            String errMsg = "";
            status = MainEr58AiActivity.mPOSPrinter.getStatus();
            if ((status & MainEr58AiActivity.mPOSPrinter.STS_PAPER_MASK) == MainEr58AiActivity.mPOSPrinter.STS_PAPER_EMPTY)
                errMsg += "\n\t * Giấy in trống";
            else
                errMsg += "\n\t * Giấy in sẵn sàng";

			/*
			if((status&mPOSPrinter.STS_COVER_MASK)==mPOSPrinter.STS_COVER_OPEN)
				errMsg += "\n\t * Cover is open";
			else
				errMsg += "\n\t * Cover is closed";

			if((status&mPOSPrinter.STS_BATTERY_MASK)==mPOSPrinter.STS_BATTERY_LOW)
				errMsg += "\n\t * Battery level is low";
			else if((status&mPOSPrinter.STS_BATTERY_MASK)==mPOSPrinter.STS_BATTERY_MEDIUM)
				errMsg += "\n\t * Battery level is medium";
			else if((status&mPOSPrinter.STS_BATTERY_MASK)==mPOSPrinter.STS_BATTERY_HIGH)
				errMsg += "\n\t * Battery level is high";
			*/
            if (errMsg.length() > 0) {
                //please consider the battery level status affection.
                Toast.makeText(getActivity(), errMsg, Toast.LENGTH_LONG).show();
                return;
            }
            //else
            //{
            //	Toast.makeText(BluetoothDemo.this, "\n\t * Printer is normal", Toast.LENGTH_LONG).show();
            //	return;
            //}
        } else if (result == MainEr58AiActivity.mPOSPrinter.MP_NO_CONNECTION) {
            Toast.makeText(getContext(), "Bluetooth không được kết nối", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Kiểm tra thất bại", Toast.LENGTH_LONG).show();
            return;
        }
    }
}