package com.autochip.vivanflora;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import app_utility.CircularProgressBar;
import app_utility.DataBaseHelper;
import app_utility.DatabaseHandler;
import app_utility.NetworkState;
import app_utility.OnAsyncTaskInterface;
import app_utility.OnFragmentInteractionListener;
import app_utility.VivanFloraAsyncTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * {@link OnAsyncTaskInterface}
 * to handle interaction events.
 * Use the {@link CreateOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateOrderFragment extends Fragment implements OnAsyncTaskInterface {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    boolean isProductAlreadyPresent = false;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private OnAsyncTaskInterface onAsyncTaskInterface;

    ArrayList<String> alProducts = new ArrayList<>();
    ArrayList<Integer> alImagePosition = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Set<String> hsSelectedProducts = new HashSet<>();

    TableLayout tlProducts;
    //TableRow[] rows;
    TableRow row;
    TableRow trHeading;

    Button btnPlaceOrder, btnSaveOrder;
    TextView tvDate;
    Button btnDelete;
    ArrayList<TextView> alSlNoTextView = new ArrayList<>();
    FloatingActionButton fabCreateOrder;
    ScrollView scrollView;
    Spinner spinner;
    VivanFloraAsyncTask vivanFloraAsyncTask;
    String sCompare;
    TextView tvUnitPrice, tvSubTotal;
    EditText etQuantity;
    float nQuantity;

    DatabaseHandler dbh;

    LinkedHashSet<ArrayList<String>> lhsData = new LinkedHashSet<>();
    LinkedHashMap<String, ArrayList<String>> lhmData = new LinkedHashMap<>();
    LinkedHashMap<String, ArrayList<String>> lhmProductsData = new LinkedHashMap<>();

    private String sDate;

    String sStatus;

    private TextView tvTotalAmount;

    private final Calendar myCalendar = Calendar.getInstance();

    NetworkState networkState;

    public CreateOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateOrderFragment.
     */

    public static CreateOrderFragment newInstance(String param1, String param2) {
        CreateOrderFragment fragment = new CreateOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAsyncTaskInterface = this;
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        dbh = new DatabaseHandler(getActivity());
        networkState = new NetworkState();

        alProducts.add("Select Product");
        alProducts.add("Product A");
        alProducts.add("Product B");
        alProducts.add("Product C");
        alProducts.add("Product D");
        /*vivanFloraAsyncTask = new VivanFloraAsyncTask(getActivity(), onAsyncTaskInterface);
        vivanFloraAsyncTask.execute(String.valueOf(4), "");
        alProducts.add("Select Product");*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_order, container, false);

        init(view, inflater);
        handleRow();
        fabCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner.getSelectedItemPosition() != 0 && !sCompare.equals("Select Product")) {
                    addDataToList(sCompare);
                    if (alSlNoTextView.size() != 30) {
                        handleRow();
                    } else
                        Toast.makeText(getActivity(), "Maximum 30 products allowed, please create new quotation", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getActivity(), "Please select product first", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    void init(View view, LayoutInflater inflater) {
        //rows = new TableRow[10];
        //baButtonDelete = new Button[10];
        tvDate = view.findViewById(R.id.tv_date);
        Date date = new Date();
        String modifiedDate = new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(date);
        tvDate.setText(modifiedDate);
        tvTotalAmount = view.findViewById(R.id.tv_total_amount);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd/MM/yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        sDate = sdf.format(myCalendar.getTime());
                        tvDate.setText(sDate);
                    }

                };
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        btnSaveOrder = view.findViewById(R.id.btn_save_order);
        btnPlaceOrder = view.findViewById(R.id.btn_place_order);

        tlProducts = view.findViewById(R.id.tl_products);
        trHeading = (TableRow) inflater.inflate(R.layout.table_row_heading, null);

        fabCreateOrder = view.findViewById(R.id.fab_create_order);
        scrollView = view.findViewById(R.id.sv_create_order);
        scrollView.setSmoothScrollingEnabled(true);

        btnSaveOrder.setOnClickListener(onClickListener);
        btnPlaceOrder.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        //HashMap<String, Object> hmDataList = new HashMap<>();

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save_order:
                    sStatus = "SAVE";
                    //Toast.makeText(getActivity(), "SAVE", Toast.LENGTH_SHORT).show();
                    //ArrayList<String> alData = new ArrayList<>(lhmData.get())
                    if (spinner.getSelectedItemPosition() != 0 && !sCompare.equals("Select Product"))
                        addDataToList(sCompare);

                    //dbh.addDataToTempTable(new DataBaseHelper());
                    new updateSerialNoAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "2");
                    /*vivanFloraAsyncTask = new VivanFloraAsyncTask(getActivity(), onAsyncTaskInterface, lhmData);
                    vivanFloraAsyncTask.execute(String.valueOf(2), "");*/
                    break;
                case R.id.btn_place_order:
                    sStatus = "PLACE";
                    //Toast.makeText(getActivity(), "PLACE", Toast.LENGTH_SHORT).show();
                    //hmDataList = new HashMap<>();
                    //hmDataList.put("state", ORDER_STATE[2]);
                    if (networkState.isOnline() && networkState.isNetworkAvailable(getActivity())) {
                        vivanFloraAsyncTask = new VivanFloraAsyncTask(getActivity(), onAsyncTaskInterface, lhmData);
                        vivanFloraAsyncTask.execute(String.valueOf(2), "");
                    } else {
                        new updateSerialNoAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "2");
                    }
                    break;
                case R.id.btn_table_row_delete:
                    if (hsSelectedProducts.size() >= 1) {
                        row = (TableRow) v.getParent();
                        TextView tv = row.findViewById(R.id.tv_sl_no_value);
                        Spinner spinner = row.findViewById(R.id.spinner_product);
                        TextView tvSubTotal = row.findViewById(R.id.tv_sub_total_value);
                        sCompare = spinner.getSelectedItem().toString();
                        lhmData.remove(sCompare);
                        hsSelectedProducts.remove(sCompare);
                        alSlNoTextView.remove(tv);
                        tlProducts.removeView(row);
                        new updateSerialNoAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
                        if (sCompare.equals("Select Product")) {
                            sCompare = "";
                            spinner.setSelection(5);
                        }
                        String sSubTotal = tvSubTotal.getText().toString().trim();
                        double dTotal = Double.valueOf(tvTotalAmount.getText().toString().trim());
                        double dFinalAmount = dTotal - Double.valueOf(sSubTotal);
                        tvTotalAmount.setText(String.valueOf(dFinalAmount));
                    }
                    break;
            }
        }
    };


    void addDataToList(String sSpinnerData) {
        String sTotal = tvTotalAmount.getText().toString().trim();
        String sSubTotal = tvSubTotal.getText().toString().trim();
        ArrayList<String> alData = new ArrayList<>();
        //alData.add(sSpinnerData);
        ArrayList<String> alID = new ArrayList<>(lhmProductsData.get(sSpinnerData));
        alData.add(alID.get(0));

        /*String sQuantity;
        if(etQuantity.getText().toString().trim().equals(""))
            sQuantity = "1.0";
        else*/
        String sQuantity = etQuantity.getText().toString().trim();
        alData.add(sQuantity);
        alData.add(tvUnitPrice.getText().toString().trim());
        alData.add(sSubTotal);
        lhmData.put(sSpinnerData, alData);
        //lhsData.add(alData);
        double dTotal = Double.valueOf(sTotal);
        double dFinalAmount = dTotal + Double.valueOf(sSubTotal);
        tvTotalAmount.setText(String.valueOf(dFinalAmount));

        spinner.setClickable(false);
        etQuantity.setEnabled(false);
        //Spinner spinner = row.findViewById(R.id.spinner_product);
        /*String s = spinner.getSelectedItem().toString();
        isProductAlreadyPresent = lhmData.containsKey(s);*/
    }

    void handleRow() {
        row = (TableRow) getLayoutInflater().inflate(R.layout.table_row, null);
        btnDelete = row.findViewById(R.id.btn_table_row_delete);
        if (tlProducts.getChildCount() == 0)
            tlProducts.addView(trHeading, 0);

        etQuantity = row.findViewById(R.id.et_quantity);
        tvUnitPrice = row.findViewById(R.id.tv_unit_price_value);
        tvSubTotal = row.findViewById(R.id.tv_sub_total_value);
        TextView tvSlNo = row.findViewById(R.id.tv_sl_no_value);
        alSlNoTextView.add(tvSlNo);
        tvSlNo.setText(String.valueOf(alSlNoTextView.size()));

        etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etQuantity.getText().toString().trim().equals("")) {
                    nQuantity = Float.valueOf(etQuantity.getText().toString().trim());
                    float dTmp = nQuantity * Float.valueOf(tvUnitPrice.getText().toString().trim());
                    tvSubTotal.setText(String.valueOf(dTmp));
                    //etQuantity.setText(String.valueOf(nQuantity));
                }
            }
        });

        tlProducts.addView(row);
        if (alSlNoTextView.size() > 10)
            scrollView.fullScroll(View.FOCUS_DOWN);


        btnDelete = row.findViewById(R.id.btn_table_row_delete);
        btnDelete.setTag(alSlNoTextView.size());
        btnDelete.setOnClickListener(onClickListener);
        /*btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row = (TableRow) view.getParent();
                TextView tv = row.findViewById(R.id.tv_sl_no_value);
                Spinner spinner = row.findViewById(R.id.spinner_product);
                hsSelectedProducts.remove(spinner.getSelectedItem().toString());
                alSlNoTextView.remove(tv);
                tlProducts.removeView(row);
                new updateSerialNoAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        ArrayList<String> alMake = new ArrayList<>();
        alMake.add("Gerbera");
        alMake.add("Red Rose");
        alMake.add("Sun flower");
        alMake.add("Casa Blanca");*/
        spinner = row.findViewById(R.id.spinner_product);
        adapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_row, R.id.tv_products, alProducts) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = view.findViewById(R.id.tv_products);
                //ImageView iv = view.findViewById(R.id.iv_product_image);
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                    /*if (alImagePosition.contains(position + 1)) {
                        ArrayList<String> alImageData = new ArrayList<>(lhmProductsData.get(tv.getText().toString()));
                        Bitmap bitmap = convertToBitmap(alImageData.get(2).substring(3));
                        iv.setImageBitmap(bitmap);
                    }*/

                    //if (bitmap != null) {
                }
                return view;
            }
        };

        //adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row, R.id.tv_products, alProducts); //android.R.layout.simple_spinner_item
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(R.layout.spinner_row);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = view.findViewById(R.id.tv_products);

                sCompare = spinner.getSelectedItem().toString();
                if (spinner.getSelectedItemPosition() != 0) {
                    tv.setTextColor(getResources().getColor(R.color.dark_grey));
                    ArrayList<String> alData = new ArrayList<>(lhmProductsData.get(sCompare));
                    tvUnitPrice.setText(alData.get(1));
                    tvSubTotal.setText(alData.get(1));
                    hsSelectedProducts.add(sCompare);
                    etQuantity.setText("1.0");
                } else {
                    if (!sCompare.equals("Select Product"))
                        tv.setTextColor(getResources().getColor(R.color.light_grey));
                }

                /*{
                    tv.setTextColor(getResources().getColor(R.color.dark_grey));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.light_grey));
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //new updateSerialNoAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAsyncTaskComplete(String sCase, int nFlag, LinkedHashMap<String, ArrayList<String>> lhmData,
                                    ArrayList<Integer> alImagePosition) {
        switch (nFlag) {
            case 4:
                alProducts.addAll(lhmData.keySet());
                spinner.setAdapter(adapter);
                this.lhmProductsData = lhmData;
                this.alImagePosition = alImagePosition;
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class updateSerialNoAsync extends AsyncTask<String, String, String> {
        int type;
        private CircularProgressBar circularProgressBar;

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
                    //loginTask();
                    break;
                case 2:
                    //setProgressBar();
                    saveDataToTempDB();
                    //createOrder();
                    //updateTask();
                    break;
            }
            return null;
        }

        private void saveDataToTempDB() {
            ArrayList<String> alKeySet = new ArrayList<>(lhmData.keySet());
            ArrayList<String> alData;
            ArrayList<String> alProductID = new ArrayList<>();
            ArrayList<String> alProductName = new ArrayList<>();
            ArrayList<String> alProductQuantity = new ArrayList<>();
            ArrayList<String> alUnitPrice = new ArrayList<>();
            ArrayList<String> alSubTotal = new ArrayList<>();
            for (int i = 0; i < lhmData.size(); i++) {
                alData = new ArrayList<>(lhmData.get(alKeySet.get(i)));
                alProductID.add(alData.get(0));
                alProductName.add(alKeySet.get(i));
                alProductQuantity.add(alData.get(1));
                alUnitPrice.add(alData.get(2));
                alSubTotal.add(alData.get(3));
                //String productID = Integer.valueOf(alData.get(0));
            }
            String sFinalProductID = android.text.TextUtils.join(",", alProductID);
            String sFinalProductName = android.text.TextUtils.join(",", alProductName);
            String sFinalProductQuantity = android.text.TextUtils.join(",", alProductQuantity);
            String sFinalUnitPrice = android.text.TextUtils.join(",", alUnitPrice);
            String sFinalSubTotal = android.text.TextUtils.join(",", alSubTotal);
            String sDeliveryDate = tvDate.getText().toString();

            dbh.addDataToTempTable(new DataBaseHelper(sFinalProductID, sFinalProductName, sFinalProductQuantity, sFinalUnitPrice,
                    sFinalSubTotal, sDeliveryDate, sStatus));
        }

        @Override
        protected void onPostExecute(String result) {
            TextView tv;
            String sText;

            switch (type) {
                case 1:
                    for (int i = 0; i < alSlNoTextView.size(); i++) {
                        tv = alSlNoTextView.get(i);
                        sText = String.valueOf(i + 1);
                        tv.setText(sText);
                    }
                    break;
                case 2:

                    break;
            }
            if (circularProgressBar != null && circularProgressBar.isShowing()) {
                circularProgressBar.dismiss();
            }
            /*for (int i = 0; i < alSlNoTextView.size(); i++) {
                tv = alSlNoTextView.get(i);
                sText = String.valueOf(i + 1);
                tv.setText(sText);
            }*/
        }

        private void setProgressBar() {
            circularProgressBar = new CircularProgressBar(getActivity());
            circularProgressBar.setCanceledOnTouchOutside(false);
            circularProgressBar.setCancelable(false);
            circularProgressBar.show();
        }
    }

    /*public static Bitmap convertToBitmap(String base64Str) throws IllegalArgumentException {
        byte[] decodedString = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        *//*byte[] decodedBytes = Base64.decode(base64Str.substring(base64Str.indexOf(",")  + 1), Base64.DEFAULT
        );
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);*//*
    }*/

}
