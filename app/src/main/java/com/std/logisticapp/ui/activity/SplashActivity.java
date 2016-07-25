package com.std.logisticapp.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shiki.okttp.OkHttpUtils;
import com.shiki.okttp.callback.FileCallback;
import com.shiki.utils.ApkUtils;
import com.shiki.utils.DeviceUtils;
import com.shiki.utils.StringUtils;
import com.std.logisticapp.R;
import com.std.logisticapp.core.BaseActivity;
import com.std.logisticapp.logistic.LogisticApi;
import com.std.logisticapp.presenter.AppPresenter;
import com.std.logisticapp.presenter.iview.AppView;

import java.io.File;

import butterknife.Bind;
import okhttp3.Call;

/**
 * Created by Maik on 2016/5/3.
 */
public class SplashActivity extends BaseActivity implements AppView {
    @Bind(R.id.rl_root) RelativeLayout rlRoot;
    @Bind(R.id.tv_version) TextView tvVersion;
    AppPresenter appPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        getSupportActionBar().hide();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        AlphaAnimation anim = new AlphaAnimation(0.2f, 1);
        anim.setDuration(2000);
        rlRoot.startAnimation(anim);
    }

    @Override
    protected void initData() {
        this.appPresenter = new AppPresenter();
        this.appPresenter.attachView(this);
        appPresenter.getMvpView().setAppVersion("版本号：" + ApkUtils.getVersionName(this));
        appPresenter.checkUpdate();
    }

    @Override
    public void setAppVersion(String versionId) {
        tvVersion.setText(versionId);
    }

    @Override
    public Integer getAppVersion() {
        return ApkUtils.getVersionCode(SplashActivity.this);
    }

    @Override
    public void showUpdateDialog(String titile, String desc, final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titile);
        builder.setMessage(desc);
        builder.setPositiveButton("立即更新",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadAPK(url);
                    }
                });
        builder.setNegativeButton("以后再说",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enterHome();
                    }
                });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });
        builder.show();
    }

    @Override
    public void enterHome() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onFailure(String msg) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();
    }

    private void downloadAPK(String upateUrl) {
        final ProgressDialog dialog = new ProgressDialog(SplashActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle("正在下载...");
        dialog.setMessage("请稍等...");
        dialog.setProgress(0);
        dialog.setMax(100);
        dialog.show();

        if (DeviceUtils.existSDCard() && !StringUtils.isEmpty(upateUrl)) {
            OkHttpUtils.get().url(upateUrl).build().execute(new FileCallback(Environment.getExternalStorageDirectory().getAbsolutePath(), LogisticApi.APK_NAME) {

                @Override
                public void inProgress(float progress) {
                    dialog.setProgress(Math.round(progress * 100));
                }

                @Override
                public void onError(Call call, Exception e) {
                    dialog.dismiss();
                    Toast.makeText(SplashActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                    enterHome();
                }

                @Override
                public void onResponse(File response) {
                    dialog.dismiss();
                    ApkUtils.install(SplashActivity.this, response);
                }
            });
        }
    }
}
