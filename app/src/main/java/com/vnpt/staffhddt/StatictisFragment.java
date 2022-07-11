package com.vnpt.staffhddt;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vnpt.common.Common;
import com.vnpt.common.ConstantsApp;
import com.vnpt.common.ModelEvent;
import com.vnpt.room.LoaiPhi;
import com.vnpt.staffhddt.fragment.BaseFragment;
import com.vnpt.utils.DateTimeUtil;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.utils.ToastMessageUtil;
import com.vnpt.webservice.AppServices;

import java.util.Calendar;
import java.util.List;

public class StatictisFragment extends BaseFragment {
    public static String TAG = StatictisFragment.class.getName();

    TextView txtCount;
    TextView txtAmount;
    EditText edtUser, edtDateFrom, edtDateTo;
    Spinner spMenhGia;
    Button btnXem;

    AppServices appServices;

    List<LoaiPhi> loaiPhiList;
    LoaiPhi loaiPhi = null;
    int printer;

    private static final String[] PRICES = new String[]{
            "10.000"
    };


    public StatictisFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appServices = new AppServices(getContext());
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_statictis, container, false);
        init(layout);
        printer = StoreSharePreferences.getInstance(getContext()).loadIntegerSavedPreferences(Common.KEY_PRINTER);
        if (printer == Common.PRINT_ER_58) {
            ((MainEr58AiActivity) getActivity()).showProccessbar(false);
        } else {
            ((MainPos58Activity) getActivity()).showProccessbar(false);
        }

        setValueForMembers();
        setEventForMembers();
        return layout;
    }

    @Override
    protected void init(View layout) {
        txtCount = layout.findViewById(R.id.txtCount);
        txtAmount = layout.findViewById(R.id.txtAmount);
        edtUser = layout.findViewById(R.id.edtUser);
        edtDateFrom = layout.findViewById(R.id.edtDateFrom);
        edtDateTo = layout.findViewById(R.id.edtDateTo);
        spMenhGia = layout.findViewById(R.id.spMenhGia);
        btnXem = layout.findViewById(R.id.btnXem);
    }

    @Override
    protected void setValueForMembers() {
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

        btnXem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edtUser.getText().toString().trim();
                String from = edtDateFrom.getText().toString().trim();
                String to = edtDateTo.getText().toString().trim();
                if ((DateTimeUtil.compareDate(from, to) == ConstantsApp.DATE_BEFOR) || (DateTimeUtil.compareDate(from, to) == ConstantsApp.DATE_EQUAL)) {
                    if (!TextUtils.isEmpty(user)) {
//                    Toast.makeText(getContext(), loaiPhi.getAMOUNT().toString(), Toast.LENGTH_SHORT).show();
                        if (printer == Common.PRINT_ER_58) {
                            ((MainEr58AiActivity) getActivity()).showProccessbar(true);
                        } else {
                            ((MainPos58Activity) getActivity()).showProccessbar(true);
                        }
                        new GetListInvTask(user, Math.round(loaiPhi.getAMOUNT()) + "", from, to).execute();
                   } else {
                        Toast.makeText(getContext(), "Vui lòng nhập user !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ToastMessageUtil.showToastShort(getContext(), "Vui lòng chọn ngày bắt đầu nhỏ hơn hoặc bằng ngày kết thúc");
                }
            }
        });

        final Calendar calendar = Calendar.getInstance();

        edtDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(edtDateFrom, calendar);
            }
        });

        edtDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(edtDateTo, calendar);
            }
        });
    }

    private void showDatePickerDialog(EditText edtDate, Calendar calendar) {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                edtDate.setText(String.format("%02d/%02d/%d", date, month + 1, year));
            }
        };

        new DatePickerDialog(getContext(), onDateSetListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void setEventForMembers() {
        // TODO Auto-generated method stub
    }

    @Override
    public void handleModelViewEvent(ModelEvent modelEvent) {

    }

    @Override
    public void handleErrorModelViewEvent(ModelEvent modelEvent) {

//        ((MainPos58Activity) getActivity()).showProccessbar(false);

    }

    class GetListInvTask extends AsyncTask<Void, Void, String> {

        private String userName;
        private String amount;
        private String dateFrom;
        private String dateEnd;

        public GetListInvTask(String userName, String amount, String dateFrom, String dateEnd) {
            this.userName = userName;
            this.amount = amount;
            this.dateFrom = dateFrom;
            this.dateEnd = dateEnd;
        }

        @Override
        protected String doInBackground(Void... voids) {
            int type = StoreSharePreferences.getInstance(getContext()).loadIntegerSavedPreferences(Common.KEY_COMPANY_TYPE);

                return appServices.getTotalInvCurrentDate(userName, amount, dateFrom, dateEnd);

        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (printer == Common.PRINT_ER_58) {
                ((MainEr58AiActivity) getActivity()).showProccessbar(false);
            } else {
                ((MainPos58Activity) getActivity()).showProccessbar(false);
            }
            txtCount.setText(integer.split("-")[0] + "");
            txtAmount.setText(integer.split("-")[1] + " (" + integer.split("-")[2]+ ") ");
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (printer == Common.PRINT_ER_58) {
                ((MainEr58AiActivity) getActivity()).showProccessbar(false);
            } else {
                ((MainPos58Activity) getActivity()).showProccessbar(false);
            }
        }
    }
}
