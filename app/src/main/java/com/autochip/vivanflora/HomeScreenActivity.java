package com.autochip.vivanflora;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import app_utility.OnFragmentInteractionListener;

public class HomeScreenActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    int nUserDisplayHeight;
    int[] nOffSetLocation;
    int nDisplayDDXOffSet; //display drop down x off set
    int nDisplayOffSetD3;

    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        tvTitle = findViewById(R.id.tv_title);
    }

    public void onClickOrderButton(View view) {
        Fragment newFragment;
        FragmentTransaction transaction;
        View viewAnimate;

        //String sBackStackParent;

        switch (view.getId()) {
            case R.id.btn_my_orders:

                /*Bundle bundle = new Bundle();
                bundle.putInt("index", 0);*/
                newFragment = new MyOrdersFragment();
                //newFragment.setArguments(bundle);

                //sBackStackParent = newFragment.getClass().getName();
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
                transaction.replace(R.id.fl_container, newFragment, null);
                transaction.addToBackStack(null);
                transaction.commit();

                viewAnimate = findViewById(R.id.btn_my_orders);
                show(viewAnimate, findViewById(R.id.fl_container));
                tvTitle.setText(R.string.my_orders);
                break;
            case R.id.btn_catalogue:
                newFragment = new CatalogueFragment();
                //newFragment.setArguments(bundle);
                //sBackStackParent = newFragment.getClass().getName();
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
                transaction.replace(R.id.fl_container, newFragment, null);
                transaction.addToBackStack(null);
                transaction.commit();

                viewAnimate = findViewById(R.id.btn_catalogue);
                show(viewAnimate, findViewById(R.id.fl_container));
                tvTitle.setText(R.string.catalogue);
                break;
            case R.id.btn_dashboard:
                newFragment = new DashboardFragment();
                //newFragment.setArguments(bundle);
                //sBackStackParent = newFragment.getClass().getName();
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
                transaction.replace(R.id.fl_container, newFragment, null);
                transaction.addToBackStack(null);
                transaction.commit();

                viewAnimate = findViewById(R.id.btn_dashboard);
                show(viewAnimate, findViewById(R.id.fl_container));
                tvTitle.setText(R.string.dashboard);
                break;
            case R.id.btn_settings:
                newFragment = new SettingsFragment();
                //newFragment.setArguments(bundle);
                //sBackStackParent = newFragment.getClass().getName();
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
                transaction.replace(R.id.fl_container, newFragment, null);
                transaction.addToBackStack(null);
                transaction.commit();

                viewAnimate = findViewById(R.id.btn_settings);
                show(viewAnimate, findViewById(R.id.fl_container));
                tvTitle.setText(R.string.settings);
                break;
        }
    }

    public void changeTitleTo(int nCase, String sCase) {
        switch (nCase) {
            case 1:
                tvTitle.setText(R.string.my_orders);
                break;
            case 2:
                tvTitle.setText(R.string.catalogue);
                break;
            case 3:
                tvTitle.setText(R.string.dashboard);
                break;
            case 4:
                tvTitle.setText(R.string.settings);
                break;
            default:
                tvTitle.setText(R.string.app_name);
                break;
        }

    }

    /*
     To reveal a previously invisible view using this effect:
     below method show is used to produce circular animation effect on home screen buttons.
     */
    private void show(final View view, final View mParentView) {
        //mParentView.setVisibility(View.INVISIBLE);
        // get the center for the clipping circle
        //int cx = (mAnimView.getLeft() + mAnimView.getRight()) / 2;
        //int cy = (mAnimView.getTop() + mParentView.getBottom()) / 2;

        nUserDisplayHeight = getResources().getDisplayMetrics().heightPixels; //holds height of screen in pixels

        nOffSetLocation = new int[2];
        view.getLocationInWindow(nOffSetLocation);
        nDisplayOffSetD3 = (nUserDisplayHeight + nOffSetLocation[1]) / 10;

        nDisplayDDXOffSet = (nOffSetLocation[0] / 2) + nDisplayOffSetD3;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // get the final radius for the clipping circle
            int finalRadius = Math.max(mParentView.getWidth(), mParentView.getHeight());

            //create the animator for this view (the start radius is zero)
            Animator anim;
            anim = ViewAnimationUtils.createCircularReveal(mParentView, nDisplayDDXOffSet, nOffSetLocation[1],
                    0, finalRadius);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(350);
            mParentView.setVisibility(View.VISIBLE);
            anim.start();
        }
    }

    @Override
    public void onFragmentMessage(String sCase, int nFlag) {

    }

    /*switch (view.getId()) {
        case R.id.btn_create_order:

                *//*Bundle bundle = new Bundle();
                bundle.putInt("index", 0);*//*
            newFragment = new CreateOrderFragment();
            //newFragment.setArguments(bundle);

            //sBackStackParent = newFragment.getClass().getName();
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
            transaction.replace(R.id.fl_container, newFragment, null);
            transaction.addToBackStack(null);
            transaction.commit();

            viewAnimate = findViewById(R.id.btn_create_order);
            show(viewAnimate, findViewById(R.id.fl_container));
            break;
        case R.id.btn_view_order:
            newFragment = new ViewOrderFragment();
            //newFragment.setArguments(bundle);
            //sBackStackParent = newFragment.getClass().getName();
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
            transaction.replace(R.id.fl_container, newFragment, null);
            transaction.addToBackStack(null);
            transaction.commit();

            viewAnimate = findViewById(R.id.btn_view_order);
            show(viewAnimate, findViewById(R.id.fl_container));
            break;
    }*/
}
