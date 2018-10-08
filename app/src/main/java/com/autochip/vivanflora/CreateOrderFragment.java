package com.autochip.vivanflora;

import android.content.Context;
import android.net.Uri;
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

import java.util.zip.Inflater;

import app_utility.OnFragmentInteractionListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link app_utility.OnFragmentInteractionListener} interface
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
    Button[] baButtonDelete;
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

        init(view, inflater);
        int count = tlProducts.getChildCount();
        row = (TableRow) getLayoutInflater().inflate(R.layout.table_row, null);
        //rows[count] = row;
        tlProducts.addView(trHeading);
        tlProducts.addView(row);


        fabCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = tlProducts.getChildCount();
                row = (TableRow) getLayoutInflater().inflate(R.layout.table_row, null);
                TextView tvSlNo = row.getChildAt(0).findViewById(R.id.tv_sl_no);
                tvSlNo.setText(String.valueOf(count));
                tlProducts.addView(row);
                //rows[count] = row;
            }
        });

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

    void init(View view, LayoutInflater inflater){
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
