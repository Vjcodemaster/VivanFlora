package com.autochip.vivanflora;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

import app_utility.DataBaseHelper;
import app_utility.DatabaseHandler;
import app_utility.OnAsyncTaskInterface;
import app_utility.OnFragmentInteractionListener;
import app_utility.VivanFloraAsyncTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link app_utility.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewOrderFragment extends Fragment implements OnAsyncTaskInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TableLayout tlProducts;
    //TableRow[] rows;
    TableRow row;
    TableRow trHeading;

    TextView tvUnitPrice, tvSubTotal;
    EditText etQuantity;

    private OnAsyncTaskInterface onAsyncTaskInterface;

    Button btnPlaceOrder, btnSaveOrder;
    TextView tvDate;
    Button btnDelete;
    ArrayList<TextView> alSlNoTextView = new ArrayList<>();
    FloatingActionButton fabCreateOrder;
    ScrollView scrollView;
    Spinner spinner;
    DatabaseHandler dbh;
    ArrayList<DataBaseHelper> alDBData;

    private final Calendar myCalendar = Calendar.getInstance();
    int nDBID;
    ArrayList<String> alProductID;
    ArrayList<String> alProductName;
    ArrayList<String> alProductQuantity;
    ArrayList<String> alUnitPrice;
    ArrayList<String> alSubTotal;
    String sDate;

    ArrayAdapter<String> adapter;
    ArrayList<String> alProducts = new ArrayList<>();

    LinkedHashMap<String, ArrayList<String>> lhmProductsData = new LinkedHashMap<>();
    LinkedHashMap<String, ArrayList<String>> lhmData = new LinkedHashMap<>();
    private TextView tvTotalAmount;
    String sCompare;

    public ViewOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewOrderFragment newInstance(String param1, String param2) {
        ViewOrderFragment fragment = new ViewOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        onAsyncTaskInterface = this;
        dbh = new DatabaseHandler(getActivity());
        alDBData = new ArrayList<>(dbh.getSingleProductByID(Integer.valueOf(mParam1)));

        nDBID = alDBData.get(0).get_id();
        alProductID = new ArrayList<>(Arrays.asList(alDBData.get(0).get_product_id_string().split(",")));
        alProductName = new ArrayList<>(Arrays.asList(alDBData.get(0).get_product_name().split(",")));
        alProductQuantity = new ArrayList<>(Arrays.asList(alDBData.get(0).get_product_quantity_string().split(",")));
        alUnitPrice = new ArrayList<>(Arrays.asList(alDBData.get(0).get_unit_price_string().split(",")));
        alSubTotal = new ArrayList<>(Arrays.asList(alDBData.get(0).get_sub_total_string().split(",")));
        sDate = alDBData.get(0).get_delivery_date();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_order, container, false);
        init(view, inflater);

        VivanFloraAsyncTask vivanFloraAsyncTask = new VivanFloraAsyncTask(getActivity(), onAsyncTaskInterface);
        vivanFloraAsyncTask.execute(String.valueOf(4), "");
        alProducts.add("Select Product");

        for (int i=0; i<alProductID.size();i++){
            addDataToRowFromList(i);
        }
        tvDate.setText(sDate);
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
    void addDataToRowFromList(int i){
        row = (TableRow) getLayoutInflater().inflate(R.layout.table_row, null);
        btnDelete = row.findViewById(R.id.btn_table_row_delete);
        if (tlProducts.getChildCount() == 0)
            tlProducts.addView(trHeading, 0);

        TextView tvSlNo = row.findViewById(R.id.tv_sl_no_value);
        //alSlNoTextView.add(tvSlNo);
        tvSlNo.setText(String.valueOf(i+1));

        spinner = row.findViewById(R.id.spinner_product);

        adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row, R.id.tv_products, alProductName); //android.R.layout.simple_spinner_item
        spinner.setAdapter(adapter);

        spinner.setSelection(i);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = view.findViewById(R.id.tv_products);

                sCompare = spinner.getSelectedItem().toString();
                if (spinner.getSelectedItemPosition() != 0) {
                    tv.setTextColor(getResources().getColor(R.color.dark_grey));
                    ArrayList<String> alData = new ArrayList<>(lhmProductsData.get(sCompare));
                    tvUnitPrice.setText(alData.get(1));
                    tvSubTotal.setText(alData.get(1));
                    //hsSelectedProducts.add(sCompare);
                    etQuantity.setText("1.0");
                } else {
                    if (!sCompare.equals("Select Product"))
                        tv.setTextColor(getResources().getColor(R.color.light_grey));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        EditText etQuantity = row.findViewById(R.id.et_quantity);
        etQuantity.setText(alProductQuantity.get(i));

        TextView tvUnitPrice = row.findViewById(R.id.tv_unit_price_value);
        tvUnitPrice.setText(alUnitPrice.get(i));

        TextView tvSubTotal = row.findViewById(R.id.tv_sub_total_value);
        tvSubTotal.setText(alSubTotal.get(i));

        tlProducts.addView(row);


        /*if (alSlNoTextView.size() > 10)
            scrollView.fullScroll(View.FOCUS_DOWN);

        btnDelete = row.findViewById(R.id.btn_table_row_delete);
        btnDelete.setTag(String.valueOf(i+1));
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row = (TableRow) view.getParent();
                TextView tv = row.findViewById(R.id.tv_sl_no_value);
                alSlNoTextView.remove(tv);
                tlProducts.removeView(row);
                new updateSerialNoAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });*/


    }

    void init(View view, LayoutInflater inflater) {
        //rows = new TableRow[10];
        //baButtonDelete = new Button[10];
        tvDate = view.findViewById(R.id.tv_date);
        tvTotalAmount = view.findViewById(R.id.tv_total_amount);
        Date date = new Date();
        String modifiedDate= new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(date);
        tvDate.setText(modifiedDate);

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
                        tvDate.setText(sdf.format(myCalendar.getTime()));
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

        tlProducts.addView(row);
        if (alSlNoTextView.size() > 10)
            scrollView.fullScroll(View.FOCUS_DOWN);

        btnDelete = row.findViewById(R.id.btn_table_row_delete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row = (TableRow) view.getParent();
                TextView tv = row.findViewById(R.id.tv_sl_no_value);
                alSlNoTextView.remove(tv);
                tlProducts.removeView(row);
                new updateSerialNoAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        /*ArrayList<String> alMake = new ArrayList<>();
        alMake.add("Gerbera");
        alMake.add("Red Rose");
        alMake.add("Sun flower");
        alMake.add("Casa Blanca");*/
        spinner = row.findViewById(R.id.spinner_product);
        adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row, R.id.tv_products, alProducts); //android.R.layout.simple_spinner_item
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = view.findViewById(R.id.tv_products);

                sCompare = spinner.getSelectedItem().toString();
                if (spinner.getSelectedItemPosition() != 0) {
                    tv.setTextColor(getResources().getColor(R.color.dark_grey));
                    ArrayList<String> alData = new ArrayList<>(lhmProductsData.get(sCompare));
                    tvUnitPrice.setText(alData.get(1));
                    tvSubTotal.setText(alData.get(1));
                    //hsSelectedProducts.add(sCompare);
                    etQuantity.setText("1.0");
                } else {
                    if (!sCompare.equals("Select Product"))
                        tv.setTextColor(getResources().getColor(R.color.light_grey));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //new updateSerialNoAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onAsyncTaskComplete(String sCase, int nFlag, LinkedHashMap<String, ArrayList<String>> lhmData, ArrayList<Integer> alImagePosition) {
        switch (nFlag) {
            case 4:
                alProducts.addAll(lhmData.keySet());
                spinner.setAdapter(adapter);
                this.lhmProductsData = lhmData;
                //this.alImagePosition = alImagePosition;
                break;
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class updateSerialNoAsync extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView tv;
            String sText;

            for (int i = 0; i < alSlNoTextView.size(); i++) {
                tv = alSlNoTextView.get(i);
                sText = String.valueOf(i + 1);
                tv.setText(sText);
            }
        }
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
}
