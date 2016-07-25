package com.std.logisticapp.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.shiki.utils.ActivityManager;
import com.std.logisticapp.R;
import com.std.logisticapp.core.BaseActivity;
import com.std.logisticapp.di.HasComponent;
import com.std.logisticapp.di.components.DaggerMainComponent;
import com.std.logisticapp.di.components.MainComponent;
import com.std.logisticapp.ui.adapter.MainViewPagerAdapter;
import com.std.logisticapp.ui.fragment.DeliveryFragment;
import com.std.logisticapp.ui.fragment.HomeFragment;
import com.std.logisticapp.ui.fragment.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Maik on 2016/5/5.
 */
public class MainActivity extends BaseActivity implements HasComponent<MainComponent> {
    @Bind(R.id.vp_main) ViewPager vpMain;
    @Bind(R.id.rg_tabbar) RadioGroup rgTab;
    private List<Fragment> fgContent;
    private MainComponent mainComponent;
    private DeliveryFragment deliveryFragment;
    private long timeMillis;

    private void initializeInjector() {
        this.mainComponent = DaggerMainComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initializeInjector();
    }

    @Override
    protected void initListeners() {
        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rgTab.check(R.id.rb_home);
                        //mMenuItem.setVisible(false);
                        break;
                    case 1:
                        rgTab.check(R.id.rb_delivery);
                        //mMenuItem.setVisible(true);
                        break;
                    case 2:
                        rgTab.check(R.id.rb_profile);
                        //mMenuItem.setVisible(false);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        setTitle("业务");
                        vpMain.setCurrentItem(0, false);
                        break;
                    case R.id.rb_delivery:
                        setTitle("配送单管理");
                        vpMain.setCurrentItem(1, false);
                        break;
                    case R.id.rb_profile:
                        setTitle("我的");
                        vpMain.setCurrentItem(2, false);
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {
        fgContent = new ArrayList<>();
        HomeFragment homeFragment = new HomeFragment();
        deliveryFragment = new DeliveryFragment();
        ProfileFragment profileFragment = new ProfileFragment();
        fgContent.add(homeFragment);
        fgContent.add(deliveryFragment);
        fgContent.add(profileFragment);
        vpMain.setAdapter(new MainViewPagerAdapter(
                getSupportFragmentManager(), fgContent));
        vpMain.setCurrentItem(0);
    }

    public void switchDelivery() {
        rgTab.check(R.id.rb_delivery);
    }

    public void switchDelivery(int position) {
        this.switchDelivery();
        this.deliveryFragment.setViewItem(position);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        mMenuItem = menu.findItem(R.id.action_search);
        mMenuItem.collapseActionView();
        final SearchView searchview=(SearchView) mMenuItem.getActionView();
        *//*SearchManager mSearchManager=(SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo info=mSearchManager.getSearchableInfo(getComponentName());
        searchview.setSearchableInfo(info); //需要在Xml文件加下建立searchable.xml,搜索框配置文件*//*
        searchview.setQueryHint("搜索");
        *//*try {
            Class<?> argClass=searchview.getClass();
            //修改为展开时的搜索图标
            Field mSearchButton = argClass.getDeclaredField("mSearchButton");
            mSearchButton.setAccessible(true);
            ImageView search = (ImageView) mSearchButton.get(searchview);
            search.setImageResource(R.drawable.ic_search);

            //修改关闭图标
            Field mCloseButton = argClass.getDeclaredField("mCloseButton");
            mCloseButton.setAccessible(true);
            ImageView backView = (ImageView) mCloseButton.get(searchview);
            backView.setImageResource(R.drawable.ic_gf_clear);

            //修改关闭图标
            Field mSearchHintIcon = argClass.getDeclaredField("mSearchHintIcon");
            mSearchHintIcon.setAccessible(true);
            Drawable drawable = (Drawable) mSearchHintIcon.get(searchview);
            drawable.mutate();
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
            drawable.setColorFilter(cf);
        } catch (Exception e) {
            e.printStackTrace();
        }*//*
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 0) {
                    Logger.d("onQueryTextSubmit");
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Logger.d("onQueryTextChange");
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(this,"搜索Main",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }*/

    @Override
    public MainComponent getComponent() {
        return mainComponent;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - timeMillis) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                timeMillis = System.currentTimeMillis();
            } else {
                ActivityManager.getActivityManager().finishAllActivity();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
