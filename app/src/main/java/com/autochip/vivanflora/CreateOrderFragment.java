package com.autochip.vivanflora;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app_utility.OnFragmentInteractionListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateOrderFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TableLayout tlProducts;
    TableRow[] rows;
    TableRow row;
    TableRow trHeading;

    Button btnConfirm, btnCancel;
    Button btnDelete;
    Button[] baButtonDelete;
    ArrayList<TextView> alSlNoTextView = new ArrayList<>();
    FloatingActionButton fabCreateOrder;
    int rowLength = 0;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_order, container, false);

        rows = new TableRow[11];
        baButtonDelete = new Button[11];
        init(view, inflater);
        final int count = tlProducts.getChildCount();
        row = (TableRow) getLayoutInflater().inflate(R.layout.table_row, null);
        btnDelete = row.findViewById(R.id.btn_table_row_delete);
        tlProducts.addView(trHeading);
        tlProducts.addView(row);
        baButtonDelete[count] = btnDelete;
        rows[count] = row;
        TextView tvSlNo = row.findViewById(R.id.tv_sl_no);
        alSlNoTextView.add(tvSlNo);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rows[count].removeAllViews();
                tlProducts.removeView(row);
            }
        });


        fabCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int count = tlProducts.getChildCount();
                if (count != rows.length) {
                    row = (TableRow) getLayoutInflater().inflate(R.layout.table_row, null);
                    TextView tvSlNo = row.getChildAt(0).findViewById(R.id.tv_sl_no);
                    tvSlNo.setText(String.valueOf(count));
                    btnDelete = row.findViewById(R.id.btn_table_row_delete);
                    tlProducts.addView(row);
                    baButtonDelete[count] = btnDelete;
                    rows[count] = row;
                    alSlNoTextView.add(tvSlNo);
                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            rows[count].removeAllViews();
                            //tlProducts.removeView(row);
                            tlProducts.removeViewAt(count);
                            alSlNoTextView.remove(count-1);
                            final TextView[] tv = new TextView[1];
                            final String[] sText = new String[1];
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i=0 ; i<alSlNoTextView.size(); i++){
                                        tv[0] = alSlNoTextView.get(i);
                                        sText[0] = String.valueOf(i+1);
                                        tv[0].setText(sText[0]);
                                    }
                                }
                            });
                        }
                    });

                } else
                    Toast.makeText(getActivity(), "Maximum 10 products allowed", Toast.LENGTH_LONG).show();
            }
        });

        /*for (int i=0; i<baButtonDelete.length-1; i++){
            final int finalI = i;
            baButtonDelete[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    row = rows[finalI];
                    rows[finalI].removeAllViews();
                    tlProducts.removeView(row);
                }
            });
        }*/
        /*for(int i=0; i<rows.length; i++){
            baButtonDelete[i] = row.findViewById(R.id.btn_table_row_delete);
            final int finalI = i;
            baButtonDelete[finalI].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    row = rows[finalI];
                    //rows[finalI].removeAllViews();
                    tlProducts.removeView(row);
                }
            });
        }*/


        return view;
    }

    void init(View view, LayoutInflater inflater) {
        //rows = new TableRow[10];
        //baButtonDelete = new Button[10];

        btnConfirm = view.findViewById(R.id.btn_confirm);
        btnCancel = view.findViewById(R.id.btn_cancel);

        tlProducts = view.findViewById(R.id.tl_products);
        trHeading = (TableRow) inflater.inflate(R.layout.table_row_heading, null);

        fabCreateOrder = view.findViewById(R.id.fab_create_order);
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
