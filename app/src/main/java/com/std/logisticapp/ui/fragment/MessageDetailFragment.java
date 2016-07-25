package com.std.logisticapp.ui.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.std.logisticapp.R;
import com.std.logisticapp.bean.MessageBean;
import com.std.logisticapp.core.BaseFragment;
import com.std.logisticapp.di.components.MessageComponent;
import com.std.logisticapp.presenter.MessageDetailPresenter;
import com.std.logisticapp.presenter.iview.MessageDetailView;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by Maik on 2016/5/12.
 */
public class MessageDetailFragment extends BaseFragment implements MessageDetailView {
    @Inject
    MessageDetailPresenter messageDetailPresenter;
    @Bind(R.id.tv_msgTitle) TextView tvMsgTitle;
    @Bind(R.id.tv_msgSubTitle) TextView tvMsgSubTitle;
    @Bind(R.id.wv_msgContent) WebView wvMsgContent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(MessageComponent.class).inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.messageDetailPresenter.attachView(this);
        if (savedInstanceState == null)
            this.messageDetailPresenter.loadDetailData();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message_detail;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void renderMessageDetail(MessageBean message) {
        if (message != null) {
            setTitleWithFormat(message.getMessageTitle());
            setSubTitleWithFormat(message.getMessageDate() + "    " + message.getMessageName());
            setContentWithFormat(message.getMessageContent());
        }
    }

    @Override
    public void setTitleWithFormat(String title) {
        tvMsgTitle.setText(title);
        tvMsgTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvMsgTitle.getPaint().setFakeBoldText(true);//加粗

    }

    @Override
    public void setSubTitleWithFormat(String subtitle) {
        tvMsgSubTitle.setText(subtitle);
    }

    @Override
    public void setContentWithFormat(String content) {
        wvMsgContent.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
        wvMsgContent.setVerticalScrollBarEnabled(false);
    }

    @Override
    public void onFailure(String msg) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.messageDetailPresenter.detachView();
    }
}
