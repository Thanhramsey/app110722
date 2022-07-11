package com.vnpt.webservice;

import android.content.Context;
import android.util.Log;

import com.vnpt.common.Common;
import com.vnpt.utils.StoreSharePreferences;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class SOAPWebserviceExtPortal {

    public static String URL_SERVER_SOAP = "";
    private static final String TAG = "SOAPWebserviceExtPortal";
    private static final String NAMESPACE = "http://tempuri.org/";

    private static String URL_PORTALSERVICE = URL_SERVER_SOAP + "/extportalservice.asmx?wsdl";


    private static final String SOAP_ACTION_LISTINVBYINDATE = "http://tempuri.org/listInvByInDate";
    private static final String METHOD_LISTINVBYINDATE = "listInvByInDate";


    private String WS_USER = "";
    private String WS_PASS = "";

    Context context;
    private static SOAPWebserviceExtPortal instance;

    public SOAPWebserviceExtPortal(String server, String user, String pass) {
        this.URL_SERVER_SOAP = server;
        this.URL_PORTALSERVICE = URL_SERVER_SOAP + "/extportalservice.asmx?WSDL";
        this.WS_USER = user;
        this.WS_PASS = pass;
    }

    public static synchronized SOAPWebserviceExtPortal getInstance(Context context) {
        if (instance == null) {
            String wsUser = StoreSharePreferences.getInstance(context).loadStringSavedPreferences(Common.KEY_COMPANY_USER);
            String wsPass = StoreSharePreferences.getInstance(context).loadStringSavedPreferences(Common.KEY_COMPANY_PASS);
            String strServer = StoreSharePreferences.getInstance(context).loadStringSavedPreferences(Common.KEY_COMPANY_URL);
            instance = new SOAPWebserviceExtPortal(strServer, wsUser, wsPass);
        }
        return instance;
    }

    public String getWS_listInvByInDate(String dateFrom, String dateEnd) {
        String ret = null;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_LISTINVBYINDATE);
            // Set the property info for the to currency
            //Tham số default
            PropertyInfo userCName = new PropertyInfo();
            userCName.setName("userName");
            userCName.setValue(WS_USER);
            userCName.setType(String.class);
            request.addProperty(userCName);

            PropertyInfo userCPass = new PropertyInfo();
            userCPass.setName("userPass");
            userCPass.setValue(WS_PASS);
            userCPass.setType(String.class);
            request.addProperty(userCPass);

            PropertyInfo fromDate = new PropertyInfo();
            fromDate.setName("fromDate");
//            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            fromDate.setValue(dateFrom);
            fromDate.setType(String.class);
            request.addProperty(fromDate);

            PropertyInfo toDate = new PropertyInfo();
            toDate.setName("toDate");
            toDate.setValue(dateEnd);
            toDate.setType(String.class);
            request.addProperty(toDate);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL_PORTALSERVICE, 30000);
            try {
                androidHttpTransport.call(SOAP_ACTION_LISTINVBYINDATE, envelope);
                //SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
//            SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                System.out.println("Response::" + resultsRequestSOAP.toString());
                ret = resultsRequestSOAP.toString();
            } catch (Exception e) {
                System.out.println("Error:" + e);
            }
        } catch (Exception ex) {
//            Log.e(TAG, "getWS_listInvByCus");
            ex.printStackTrace();
        }
        return ret;
    }
}
