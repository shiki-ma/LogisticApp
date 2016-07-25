package com.std.logisticapp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.shiki.utils.StringUtils;
import com.std.logisticapp.R;
import com.std.logisticapp.bean.MenuBean;
import com.std.logisticapp.core.BaseFragment;
import com.std.logisticapp.ui.activity.MainActivity;
import com.std.logisticapp.ui.adapter.MenuAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnItemClick;

/**
 * Created by Eric on 2016/5/12.
 */
public class HomeFragment extends BaseFragment {
    @Bind(R.id.gv_main)
    GridView gvMain;

    private List<MenuBean> menus;

    @Override
    protected void initData(Bundle savedInstanceState) {
        initMenu();
        gvMain.setAdapter(new MenuAdapter(getActivity(), menus));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {

    }

    private void initMenu() {
        menus = new ArrayList<>();
        MenuBean menu = new MenuBean();
        menu.setImgResource(R.drawable.ic_receipt);
        menu.setRootClass("com.std.logisticapp.ui.activity.OrderReceiptActivity");
        menu.setTitle("配送单收单");
        menus.add(menu);
        menu = new MenuBean();
        menu.setImgResource(R.drawable.ic_manage);
        menu.setRootClass("");
        menu.setTitle("配送单管理");
        menus.add(menu);
        menu = new MenuBean();
        menu.setImgResource(R.drawable.ic_tousu);
        menu.setRootClass("com.std.logisticapp.ui.activity.DeliveryComplaintListActivity");
        menu.setTitle("投诉处理");
        menus.add(menu);
        menu = new MenuBean();
        menu.setImgResource(R.drawable.ic_bulletin);
        menu.setRootClass("com.std.logisticapp.ui.activity.MessageListActivity");
        menu.setTitle("公告信息");
        menus.add(menu);
    }

    @Override
    protected void initListeners() {

    }

    @OnItemClick(R.id.gv_main)
    public void itemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            String rootClass = menus.get(position).getRootClass();
            if (!StringUtils.isEmpty(rootClass)) {
                Intent intent = new Intent(getActivity(), Class.forName(rootClass));
                startActivity(intent);
            } else {
                ((MainActivity)getActivity()).switchDelivery();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
