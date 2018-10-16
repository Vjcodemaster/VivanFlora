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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app_utility.OnFragmentInteractionListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link app_utility.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewOrderFragment extends Fragment {
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

    Button btnPlaceOrder, btnSaveOrder;
    TextView tvDate;
    Button btnDelete;
    ArrayList<TextView> alSlNoTextView = new ArrayList<>();
    FloatingActionButton fabCreateOrder;
    ScrollView scrollView;
    private final Calendar myCalendar = Calendar.getInstance();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_order, container, false);
        init(view, inflater);
        handleRow();

        fabCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alSlNoTextView.size() != 30) {
                    handleRow();
                } else
                    Toast.makeText(getActivity(), "Maximum 30 products allowed, please create new quotation", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    void init(View view, LayoutInflater inflater) {
        //rows = new TableRow[10];
        //baButtonDelete = new Button[10];
        tvDate = view.findViewById(R.id.tv_date);
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

        TextView tvSlNo = row.findViewById(R.id.tv_sl_no_value);
        alSlNoTextView.add(tvSlNo);
        tvSlNo.setText(String.valueOf(alSlNoTextView.size()));

        tlProducts.addView(row);
        if (alSlNoTextView.size() > 10)
            scrollView.fullScroll(View.FOCUS_DOWN);

        btnDelete = row.findViewById(R.id.btn_table_row_delete);
        btnDelete.setTag(alSlNoTextView.size());
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
        //new updateSerialNoAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
