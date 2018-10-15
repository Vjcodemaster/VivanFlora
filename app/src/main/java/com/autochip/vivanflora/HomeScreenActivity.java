package com.autochip.vivanflora;

import android.animation.Animator;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import app_utility.OnFragmentInteractionListener;

public class HomeScreenActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    int nUserDisplayHeight;
    int[] nOffSetLocation;
    int nDisplayDDXOffSet; //display drop down x off set
    int nDisplayOffSetD3;

    TextView tvTitle;
    ImageButton ibBackButton;
    public static OnFragmentInteractionListener onFragmentInteractionListener;
    Fragment newFragment;
    FragmentTransaction transaction;
    String sBackStackParent;

    View viewAnimate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        onFragmentInteractionListener = this;
        tvTitle = findViewById(R.id.tv_title);
        ibBackButton = findViewById(R.id.ib_back_button);

        ibBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void onClickOrderButton(View view) {

        switch (view.getId()) {
            case R.id.btn_my_orders:

                /*Bundle bundle = new Bundle();
                bundle.putInt("index", 0);*/
                newFragment = new MyOrdersFragment();
                //newFragment.setArguments(bundle);
                sBackStackParent = newFragment.getClass().getName();
                viewAnimate = findViewById(R.id.btn_my_orders);
                tvTitle.setText(R.string.my_orders);
                break;
            case R.id.btn_catalogue:
                newFragment = new CatalogueFragment();
                //newFragment.setArguments(bundle);
                sBackStackParent = newFragment.getClass().getName();
                /*transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
                transaction.replace(R.id.fl_container, newFragment, sBackStackParent);
                transaction.addToBackStack(null);
                transaction.commit();*/

                viewAnimate = findViewById(R.id.btn_catalogue);
                //show(viewAnimate, findViewById(R.id.fl_container));
                tvTitle.setText(R.string.catalogue);
                break;
            case R.id.btn_dashboard:
                newFragment = new DashboardFragment();
                //newFragment.setArguments(bundle);
                sBackStackParent = newFragment.getClass().getName();
                /*transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
                transaction.replace(R.id.fl_container, newFragment, sBackStackParent);
                transaction.addToBackStack(null);
                transaction.commit();*/

                viewAnimate = findViewById(R.id.btn_dashboard);
                //show(viewAnimate, findViewById(R.id.fl_container));
                tvTitle.setText(R.string.dashboard);
                break;
            case R.id.btn_settings:
                newFragment = new SettingsFragment();
                //newFragment.setArguments(bundle);
                sBackStackParent = newFragment.getClass().getName();
                /*transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
                transaction.replace(R.id.fl_container, newFragment, sBackStackParent);
                transaction.addToBackStack(null);
                transaction.commit();*/

                viewAnimate = findViewById(R.id.btn_settings);
                //show(viewAnimate, findViewById(R.id.fl_container));
                tvTitle.setText(R.string.settings);
                break;
        }
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
        transaction.replace(R.id.fl_container, newFragment, sBackStackParent);
        transaction.addToBackStack(null);
        transaction.commit();
        show(viewAnimate, findViewById(R.id.fl_container));
        ibBackButton.setVisibility(View.VISIBLE);
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
            case 5:
                tvTitle.setText(R.string.create_order);
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
        switch (sCase) {
            case "MY_ORDER_FAB_CLICK":
                newFragment = new CreateOrderFragment();
                //newFragment.setArguments(bundle);
                sBackStackParent = newFragment.getClass().getName();
                transaction = getSupportFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.b2t, R.anim.t2b);
                transaction.replace(R.id.fl_container, newFragment, sBackStackParent);
                transaction.addToBackStack(null);
                transaction.commit();

                viewAnimate = findViewById(R.id.fab_create_order);
                show(viewAnimate, findViewById(R.id.fl_container));
                changeTitleTo(5, "");
                break;
        }
    }

    private void backPressed(){
        String[] saTag;
        int size = getSupportFragmentManager().getBackStackEntryCount();
        if (size >= 1) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
            saTag = currentFragment.getTag().replace(".", ",").split(",");
            switch (saTag[3]) {
                case "MyOrdersFragment":
                    changeTitleTo(1, "");
                    break;
                case "CatalogueFragment":
                    changeTitleTo(2, "");
                    break;
                case "DashboardFragment":
                    changeTitleTo(3, "");
                    break;
                case "SettingFragment":
                    changeTitleTo(4, "");
                    break;
                case "CreateOrderFragment":
                    changeTitleTo(5, "");
                    break;
                default:
                    changeTitleTo(0, "");
                    break;
            }
            //Toast.makeText(HomeScreenActivity.this,saTag[0],  Toast.LENGTH_SHORT).show();
        } else if(size==0){
            tvTitle.setText(R.string.app_name);
            ibBackButton.setVisibility(View.GONE);
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPressed();
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
