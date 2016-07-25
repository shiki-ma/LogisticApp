package com.std.logisticapp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.std.logisticapp.R;
import com.std.logisticapp.core.BaseFragment;
import com.std.logisticapp.presenter.iview.DeliveryView;
import com.std.logisticapp.ui.adapter.DeliveryViewPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import java.util.List;

import javax.xml.transform.Source;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Eric on 2016/5/23.
 */
public class DeliveryFragment extends BaseFragment implements DeliveryView {
    @Bind(R.id.ind_delivery)
    TabPageIndicator mIndDelivery;
    @Bind(R.id.vp_delivery)
    ViewPager mVpDelivery;
    @Bind(R.id.et_receipt)
    EditText mEtReceipt;
    @Bind(R.id.iv_receipt)
    ImageView mIvReceipt;

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_delivery;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        mVpDelivery.setAdapter(new DeliveryViewPagerAdapter(
                getActivity().getSupportFragmentManager()));
        mIndDelivery.setViewPager(mVpDelivery);

    }

    @Override
    protected void initListeners() {
        mIndDelivery.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mEtReceipt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) mEtReceipt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    getActivity()
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    searchData();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public String getSearch() {
        return mEtReceipt.getText().toString();
    }

    @Override
    public void onFailure(String msg) {

    }

    @OnClick(R.id.iv_receipt)
    public void onSearchClick() {
        searchData();
    }


    private void searchData(){
        if(mVpDelivery.getAdapter() ==null){
            mVpDelivery.setAdapter(new DeliveryViewPagerAdapter(
                    getActivity().getSupportFragmentManager()));
        }
        ((DeliveryViewPagerAdapter)mVpDelivery.getAdapter()).setSearch(getSearch());
        mVpDelivery.getAdapter().notifyDataSetChanged();
        lazyData();
    }

    public void setViewItem(int position) {
        this.mVpDelivery.setCurrentItem(position);
    }

    @Override
    protected void lazyData() {
        DeliveryListFragment firstDelivery = (DeliveryListFragment) ((DeliveryViewPagerAdapter)mVpDelivery.getAdapter()).currentFragment;
        if (firstDelivery != null) {
            firstDelivery.vpLoad();
        }
    }
}
