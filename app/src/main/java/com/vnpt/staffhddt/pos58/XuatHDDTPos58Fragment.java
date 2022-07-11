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
import com.izettle.html2bitmap.Html2Bitmap;
import com.izettle.html2bitmap.content.WebViewContent;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vnpt.common.Common;
import com.vnpt.common.ModelEvent;
import com.vnpt.dto.BienLai;
import com.vnpt.dto.HDDTGLI;
import com.vnpt.dto.HoaDon;
import com.vnpt.listener.OnEventControlListener;
import com.vnpt.printproject.pos58bus.command.sdk.Command;
import com.vnpt.printproject.pos58bus.command.sdk.PrintPicture;
import com.vnpt.printproject.pos58bus.command.sdk.PrinterCommand;
import com.vnpt.room.LoaiPhi;
import com.vnpt.staffhddt.MainPos58Activity;
import com.vnpt.staffhddt.R;
import com.vnpt.staffhddt.fragment.BaseFragment;
import com.vnpt.utils.DialogUtils;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.utils.StringBienLai;
import com.vnpt.webservice.AppServices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.vnpt.staffhddt.MainPos58Activity.mPOSPrinter;
import static com.vnpt.utils.Helper.hideSoftKeyboard;

public class XuatHDDTPos58Fragment extends BaseFragment implements View.OnClickListener, OnEventControlListener {

    private static final int REQUEST_BLUE_ADMIN = 888;
    Spinner spMenhGia;
    Button btnXuatBL, btnInThu, btnCheckTB;
    EditText edtTen, edtCoQuan, edtDiaChi, edtMst, edtK;
    TextView txtCompanyInfo;
    private AwesomeProgressDialog dg;
    StoreSharePreferences preferences = null;

    List<LoaiPhi> loaiPhiList;
    LoaiPhi loaiPhi = null;

    private GetInvTask getInvTask = null;

    public static String TAG = XuatVeMenhGiaPos58Fragment.class.getName();


    private static final String[] PRICES = new String[]{
            "10.000"
    };

    private int type;

    public XuatHDDTPos58Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_xuat_hddt, container, false);

        setupUI(layout.findViewById(R.id.layout_frament_xuathddt));
        init(layout);

        setEventForMembers();
        setValueForMembers();
        ((MainPos58Activity) getActivity()).showProccessbar(false);

        preferences = StoreSharePreferences.getInstance(getContext());

        return layout;
    }

    @Override
    protected void init(View layout) {
        spMenhGia = layout.findViewById(R.id.spMenhGia);
        btnXuatBL = layout.findViewById(R.id.btnXuatBienLai);
        btnInThu = layout.findViewById(R.id.btnInThu);
        btnCheckTB = layout.findViewById(R.id.btnCheck);
        txtCompanyInfo = layout.findViewById(R.id.txtCompanyInfo);

        edtTen = layout.findViewById(R.id.edtTen);
        edtCoQuan = layout.findViewById(R.id.edtCoQuan);
        edtDiaChi = layout.findViewById(R.id.edtDiaChi);
        edtMst = layout.findViewById(R.id.edtMst);
        edtK = layout.findViewById(R.id.edtK);
    }

    @Override
    protected void setValueForMembers() {
        String name = StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_COMPANY_NAME);
        String mst = StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_MST);
        txtCompanyInfo.setText(String.format("%s - MST: %s", name, mst));

        type = getArguments().getInt(Common.KEY_COMPANY_TYPE);

        if (type == Common.TYPE_VE) {
            btnXuatBL.setText(R.string.xuat_ve);
        } else if (type == Common.TYPE_BIEN_LAI) {
            btnXuatBL.setText(R.string.xuat_bien_lai);
        } else if (type == Common.TYPE_HDDT) {
            btnXuatBL.setText(R.string.xuat_hd);
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
        }
    }

    private void attemptGetInv() {
        if (getInvTask != null) {
            return;
        }

        String ten = edtTen.getText().toString().trim();
        String coQuan = edtCoQuan.getText().toString().trim();
        String diaChi = edtDiaChi.getText().toString().trim();
        String mst = edtMst.getText().toString().trim();
        String k = edtK.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(ten)) {
            edtTen.setError(getString(R.string.error_empty_input));
            focusView = edtTen;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);

            long currentTime = System.currentTimeMillis();
            String keyData = "INVE" + currentTime;
            // mau XML o day

            String xmlChildData = "<Inv><key>" + keyData + "</key><Invoice><CusCode>" + StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME) + "</CusCode><Buyer>" + ten + "</Buyer><CusName>" + coQuan + "</CusName><CusAddress>" + diaChi + "</CusAddress><CusPhone></CusPhone><CusTaxCode></CusTaxCode><PaymentMethod>TM, CK </PaymentMethod><KindOfService></KindOfService><Products><Product><ProdName>" + loaiPhi.getNAME() + "</ProdName><ProdUnit></ProdUnit><ProdQuantity></ProdQuantity><ProdPrice>" + loaiPhi.getTOTAL() + "</ProdPrice><Amount>" + loaiPhi.getTOTAL() + "</Amount><Extra1>" + k + "</Extra1><Extra2></Extra2></Product></Products><Total>" + loaiPhi.getTOTAL() + "</Total><VATRate>" +
                    loaiPhi.getVAT_RATE() + "</VATRate><VATAmount>" + loaiPhi.getVAT_AMOUNT() + "</VATAmount><Amount>" + loaiPhi.getAMOUNT() + "</Amount><AmountInWords>" + StringBienLai.docSo(loaiPhi.getAMOUNT()) + "</AmountInWords><Extra></Extra></Invoice></Inv>";

            /////////////////////////

            String xmlData = "<Invoices>" + xmlChildData + "</Invoices>";

            HDDTGLI hddtgli = new HDDTGLI();
            hddtgli.setTen(ten);
            hddtgli.setCoQuan(coQuan);
            hddtgli.setDiaChi(diaChi);
            hddtgli.setMst(mst);
            hddtgli.setExtra(k);

            getInvTask = new GetInvTask(
                    StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_NAME),
                    StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_USER_PASS),
                    loaiPhi.getPATTERN(),
                    loaiPhi.getSERIAL(),
                    0,
                    xmlData,
                    hddtgli);
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
        private HDDTGLI hddtgli;

        public GetInvTask(String userName, String password, String strPattern, String strSerial, int convert, String strXmlInvData, HDDTGLI hddtgli) {
            this.userName = userName;
            this.password = password;
            this.strPattern = strPattern;
            this.strSerial = strSerial;
            this.convert = convert;
            this.strXmlInvData = strXmlInvData;
            this.hddtgli = hddtgli;
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

                String inv = s.substring(s.lastIndexOf("-") + 1);

                printInvoice(inv, hddtgli);
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

    public void printInvoice(String inv, HDDTGLI hoaDon) {

        String[] soBL = inv.split("_");
        BienLai bienLai = new BienLai();
        bienLai.setMoTa(loaiPhi.getNAME());
        bienLai.setGiaTien(loaiPhi.getAMOUNT());
        bienLai.setMau(loaiPhi.getPATTERN());
        bienLai.setSo(soBL[1].trim());
        bienLai.setKyHieu(loaiPhi.getSERIAL());
        bienLai.setPortal(StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_COMPANY_PORTAL));

        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        //Get current locale information
//        Locale currentLocale = Locale.getDefault();
//
//        //Get currency instance from locale; This will have all currency related information
//        Currency currentCurrency = Currency.getInstance(currentLocale);
//
//        //Currency Formatter specific to locale
//        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);

        String name = "HOÁ ĐƠN ĐIỆN TỬ";
        String search_des = "Để tra cứu hoá đơn gốc gốc kính mời truy cập vào link : ";

        String html = "<html>\n" +
                "                <body>\n" +
                "                <style>\n" +
                "                 </style>\n" +
                "<div style=\"text-align:center;\">" +
                "                   <h2 style=\\\"text-align: center;font-size: 40px\\\">" + StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_COMPANY_NAME) + "</h2>\n" +
                "                       <h2 style=\\\"text-align: center;font-size: 40px\\\">MST: " + StoreSharePreferences.getInstance(getContext()).loadStringSavedPreferences(Common.KEY_MST) +
                "               <h1 style=\\\"text-align: center;font-size: 40px\\\">" + name + "</h1>\n" +
                "<h3 style='text-align:center;font-size: 30px'>(" + loaiPhi.getNAME() + ")</h3>" +
                // "<h3 style='text-align:right;font-size: 20px'>(BLĐT này không thay thế cho BL thu phí, lệ phí)</h3>" +
                "</div>" +
                "                \n" + "<h2 style=\\\"text-align: center;font-size: 40px\\\">Ngày: " + date + "</h2>" +
                "               <h3 style=\\\"text-align:right;font-size: 30px\\\">Mẫu: " + bienLai.getMau() + "</h3>\n" +
                "                   <h3 style=\\\"text-align:right;font-size: 30px\\\">Ký hiệu: " + bienLai.getKyHieu() + "</h3>\n" +
                "                    <h2 style=\\\"text-align:right;font-size: 30px\\\">Số: " + bienLai.getSo() + "</h2>\n" +
                "                    <h2 style=\\\"text-align: right;font-size: 30px\\\">Tên: " + hoaDon.getTen() + "</h2>\n" +
                "                    <h2 style=\\\"text-align: right;font-size: 30px\\\">Cơ quan: " + hoaDon.getCoQuan() + "</h2>\n" +
                "                    <h2 style=\\\"text-align: right;font-size: 30px\\\">MST: " + hoaDon.getMst() + "</h2>\n" +
                "                 <h2>Giá: " + Math.round(bienLai.getGiaTien()) + " VND</h2>\n" +
                "<h2>(" + StringBienLai.docSo(bienLai.getGiaTien()) + ")</h2>" +
                "               <h1 style=\\\"text-align: center\\\">-------------------------- </h1>" +
                "<h3 style='text-align:right;font-size: 20px'>" + search_des + "</h3>" +
                "<h3>" + bienLai.getPortal() + "</h3><h3>Mã tra cứu: </h3><h3>" + inv + "</h3>" +
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

}