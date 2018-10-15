package com.autochip.vivanflora;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app_utility.OnFragmentInteractionListener;

/*
 * Created by Vj on 15-OCT-18.
 */

class MyOrdersRVAdapter extends RecyclerView.Adapter<MyOrdersRVAdapter.ProductsHolder> {

    private OnFragmentInteractionListener mListener;
    private Context context;
    private FragmentManager supportFragmentManager;


    MyOrdersRVAdapter(Context context, FragmentManager supportFragmentManager, OnFragmentInteractionListener mListener) {
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
        this.mListener = mListener;
    }

    @Override
    public ProductsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_my_orders, parent, false);

        return new MyOrdersRVAdapter.ProductsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersRVAdapter.ProductsHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ProductsHolder extends RecyclerView.ViewHolder{
        /*ImageView mImageView;
        TextView tvProductsReview;*/

        ProductsHolder(View itemView) {
            super(itemView);
            /*tvProductsReview = (TextView) itemView.findViewById(R.id.tv_products_review);
            mImageView = (ImageView) itemView.findViewById(R.id.products_rv_image_view);*/
        }
    }
}
