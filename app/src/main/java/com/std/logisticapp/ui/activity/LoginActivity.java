package com.std.logisticapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;
import com.std.logisticapp.R;
import com.std.logisticapp.core.BaseActivity;
import com.std.logisticapp.presenter.LoginPresenter;
import com.std.logisticapp.presenter.iview.LoginView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Maik on 2016/4/27.
 */
public class LoginActivity extends BaseActivity implements LoginView {

    @Bind(R.id.et_account) EditText etUserCode;
    @Bind(R.id.et_passwd) EditText etPasswd;
    @Bind(R.id.sb_remember) SwitchButton sbRemember;
    @Bind(R.id.rl_login) RelativeLayout rlProgress;
    LoginPresenter loginPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        getSupportActionBar().hide();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {
        this.loginPresenter = new LoginPresenter();
        this.loginPresenter.attachView(this);
        this.loginPresenter.showUserAndPasswd();
    }

    @OnClick(R.id.btn_login)
    void login() {
        this.loginPresenter.login();
    }

    @Override
    public String getUserCode() {
        return etUserCode.getText().toString();
    }

    @Override
    public void setUserCode(String userCode) {
        this.etUserCode.setText(userCode);
    }

    @Override
    public String getPasswd() {
        return etPasswd.getText().toString();
    }

    @Override
    public void setPasswd(String passwd) {
        this.etPasswd.setText(passwd);
    }

    @Override
    public Boolean getRemember() {
        return sbRemember.isChecked();
    }

    @Override
    public void setRemeber(Boolean isRemeber) {
        this.sbRemember.setChecked(isRemeber);
    }

    @Override
    public void showLoginProgress() {
        this.rlProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoginProgress() {
        this.rlProgress.setVisibility(View.GONE);
    }

    @Override
    public void enterMain() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onFailure(String msg) {
        if (msg.indexOf("ConnectException") > 0) {
            Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        } else if (msg.indexOf("SocketTimeoutException") > 0) {
            Toast.makeText(this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
