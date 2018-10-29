package com.autochip.vivanflora;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
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
import java.util.List;
import java.util.Locale;
import java.util.Set;

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

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private OnAsyncTaskInterface onAsyncTaskInterface;

    ArrayList<String> alProducts = new ArrayList<>();
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
    LinkedHashMap<String, ArrayList<String>> lhmProductsData = new LinkedHashMap<>();

    private final Calendar myCalendar = Calendar.getInstance();

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
        vivanFloraAsyncTask = new VivanFloraAsyncTask(getActivity(), onAsyncTaskInterface);
        vivanFloraAsyncTask.execute(String.valueOf(4), "");
        alProducts.add("Select Product");
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
                if (spinner.getSelectedItemPosition() != 0 && !sCompare.equals("Select Product"))
                    if (alSlNoTextView.size() != 30) {
                        handleRow();
                    } else
                        Toast.makeText(getActivity(), "Maximum 30 products allowed, please create new quotation", Toast.LENGTH_LONG).show();
                else
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

        btnSaveOrder.setOnClickListener(onClickListener);
        btnPlaceOrder.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save_order:
                    Toast.makeText(getActivity(), "SAVE", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_place_order:
                    Toast.makeText(getActivity(), "PLACE", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_table_row_delete:
                    if(hsSelectedProducts.size() >=1) {
                        row = (TableRow) v.getParent();
                        TextView tv = row.findViewById(R.id.tv_sl_no_value);
                        Spinner spinner = row.findViewById(R.id.spinner_product);
                        sCompare = spinner.getSelectedItem().toString();
                        hsSelectedProducts.remove(sCompare);
                        alSlNoTextView.remove(tv);
                        tlProducts.removeView(row);
                        new updateSerialNoAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        if (sCompare.equals("Select Product")) {
                            sCompare = "";
                            spinner.setSelection(5);
                        }
                    }
                    break;
            }
        }
    };

    void handleRow() {
        row = (TableRow) getLayoutInflater().inflate(R.layout.table_row, null);
        btnDelete = row.findViewById(R.id.btn_table_row_delete);
        if (tlProducts.getChildCount() == 0)
            tlProducts.addView(trHeading, 0);

        tvUnitPrice = row.findViewById(R.id.tv_unit_price_value);
        tvSubTotal = row.findViewById(R.id.tv_sub_total_value);
        TextView tvSlNo = row.findViewById(R.id.tv_sl_no_value);
        alSlNoTextView.add(tvSlNo);
        tvSlNo.setText(String.valueOf(alSlNoTextView.size()));

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
                TextView tv = (TextView) view.findViewById(R.id.tv_products);
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
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
                if (spinner.getSelectedItemPosition() != 0){
                    tv.setTextColor(getResources().getColor(R.color.dark_grey));
                    ArrayList<String> alData = new ArrayList<>(lhmProductsData.get(sCompare));
                    tvUnitPrice.setText(alData.get(1));
                    hsSelectedProducts.add(sCompare);
                } else {
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
    public void onAsyncTaskComplete(String sCase, int nFlag, LinkedHashMap<String, ArrayList<String>> lhmData) {
        switch (nFlag) {
            case 4:
                alProducts.addAll(lhmData.keySet());
                spinner.setAdapter(adapter);
                this.lhmProductsData = lhmData;
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

}
