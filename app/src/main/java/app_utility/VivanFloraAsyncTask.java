package app_utility;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static app_utility.StaticReferenceClass.DB_NAME;
import static app_utility.StaticReferenceClass.NETWORK_ERROR_CODE;
import static app_utility.StaticReferenceClass.PASSWORD;
import static app_utility.StaticReferenceClass.PORT_NO;
import static app_utility.StaticReferenceClass.SERVER_URL;
import static app_utility.StaticReferenceClass.USER_ID;

public class VivanFloraAsyncTask extends AsyncTask<String, Void, String> {

    private LinkedHashMap<String, ArrayList<String>> lhmProductsWithID = new LinkedHashMap<>();
    private CircularProgressBar circularProgressBar;
    private Activity aActivity;
    private Context context;
    private OnAsyncTaskInterface onAsyncTaskInterface;
    private HashMap<String, Object> object = new HashMap<>();

    public VivanFloraAsyncTask(Activity aActivity, OnAsyncTaskInterface onAsyncTaskInterface) {
        this.aActivity = aActivity;
        this.onAsyncTaskInterface = onAsyncTaskInterface;
    }

    public VivanFloraAsyncTask(Context context) {
        this.context = context;
    }

    private Boolean isConnected = false;
    private int ERROR_CODE = 0;
    private String sMsgResult;
    private int type;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        setProgressBar();
    }

    @Override
    protected String doInBackground(String... params) {
        type = Integer.parseInt(params[0]);
        switch (type) {
            case 1:
                loginTask();
                break;
            case 2:
                //updateTask();
                break;
            case 3:
                readTask();
                break;
            case 4:
                //snapRoadTask(params[1]);
                readProducts();
                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (ERROR_CODE != 0) {
            switch (ERROR_CODE) {
                case NETWORK_ERROR_CODE:
                    unableToConnectServer(ERROR_CODE);
                    break;
            }
            ERROR_CODE = 0;
            return;
        }
        switch (type) {
            case 4:
                onAsyncTaskInterface.onAsyncTaskComplete("READ_PRODUCTS", type, lhmProductsWithID);
                break;
        }
        if (circularProgressBar != null && circularProgressBar.isShowing()) {
            circularProgressBar.dismiss();
        }
    }

    private void loginTask() {
        //if (isConnected) {
        try {
            isConnected = OdooConnect.testConnection(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
            if (isConnected) {
                isConnected = true;
                //return true;
            } else {
                isConnected = false;
                sMsgResult = "Connection error";
            }
        } catch (Exception ex) {
            ERROR_CODE = NETWORK_ERROR_CODE;
            // Any other exception
            sMsgResult = "Error: " + ex;
        }
        // }
        //return isConnected;
    }

    private void readTask() {
        OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
        List<HashMap<String, Object>> data = oc.search_read("sale.order", new Object[]{
                new Object[]{new Object[]{"id", "=", "265"}}}, "id", "name", "partner_id");

        for (int i = 0; i < data.size(); ++i) {

        }
    }

    private void readProducts() {
        OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
        List<HashMap<String, Object>> data = oc.search_read("product.template", new Object[]{
                new Object[]{new Object[]{"type", "=", "product"}}}, "id", "name", "list_price");

        for (int i = 0; i < data.size(); ++i) {
            //int id = Integer.valueOf(data.get(i).get("id").toString());
            String sName = String.valueOf(data.get(i).get("name").toString());
            //String sUnitPrice = String.valueOf(data.get(i).get("list_price").toString());
            ArrayList<String> alData = new ArrayList<>();
            alData.add(data.get(i).get("id").toString());
            //alData.add(data.get(i).get("name").toString());
            alData.add(data.get(i).get("list_price").toString());
            lhmProductsWithID.put(sName, alData);
        }
    }

    private void unableToConnectServer(int errorCode) {
        //MainActivity.asyncInterface.onAsyncTaskCompleteGeneral("SERVER_ERROR", 2001, errorCode, "", null);
    }

    private void setProgressBar() {
        if (aActivity == null)
            circularProgressBar = new CircularProgressBar(context);
        else
            circularProgressBar = new CircularProgressBar(aActivity);
        circularProgressBar.setCanceledOnTouchOutside(false);
        circularProgressBar.setCancelable(false);
        circularProgressBar.show();
    }

}
