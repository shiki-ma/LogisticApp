package com.std.logisticapp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shiki.recyclerview.FGORecyclerView;
import com.shiki.utils.StringUtils;
import com.std.logisticapp.R;
import com.std.logisticapp.bean.MessageBean;
import com.std.logisticapp.core.BaseFragment;
import com.std.logisticapp.di.components.MessageComponent;
import com.std.logisticapp.logistic.LogisticApi;
import com.std.logisticapp.presenter.MessageListPresenter;
import com.std.logisticapp.presenter.iview.MessageListView;
import com.std.logisticapp.ui.activity.MessageDetailActivity;
import com.std.logisticapp.ui.adapter.MessageAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by Maik on 2016/5/9.
 */
public class MessageListFragment extends BaseFragment implements MessageListView {
    @Bind(R.id.fgo_recycler_view)
    FGORecyclerView rvAnnouncement;
    @Bind(R.id.et_message)
    EditText etMessage;
    @Inject
    MessageAdapter messageAdapter;
    @Inject
    MessageListPresenter messagePresenter;

    @OnClick(R.id.iv_message)
    public void msgSearch() {
        this.messagePresenter.loadMessageData(etMessage.getText().toString());
    }

    @OnEditorAction(R.id.et_message)
    public boolean hideKeyboard(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                msgSearch();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        this.rvAnnouncement.setAdapter(this.messageAdapter);
        this.rvAnnouncement.setEmptyView(R.layout.empty_view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(MessageComponent.class).inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.messagePresenter.attachView(this);
        if (savedInstanceState == null)
            this.messagePresenter.loadMessageData(etMessage.getText().toString());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        rvAnnouncement.setHasFixedSize(true);
        rvAnnouncement.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void initListeners() {
        rvAnnouncement.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MessageListFragment.this.messagePresenter.loadMessageData(etMessage.getText().toString());
            }
        });
        this.messageAdapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onMessageItemClicked(MessageBean message) {
                if (MessageListFragment.this.messagePresenter != null && message != null) {
                    MessageListFragment.this.messagePresenter.onMessageClicked(message);
                }
            }
        });
        this.etMessage.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    msgSearch();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onFailure(String msg) {
        if (StringUtils.isNumeric(msg))
            Toast.makeText(getActivity(), getString(Integer.parseInt(msg)), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void renderMessageList(List<MessageBean> messageData) {
        if (messageData != null) {
            this.messageAdapter.setMessageData(messageData);
        }
    }

    @Override
    public void viewMessage(MessageBean message) {
        Intent intentToLaunch = MessageDetailActivity.getCallingIntent(getActivity(), message);
        startActivityForResult(intentToLaunch, LogisticApi.INTENT_REQUEST_MESSAGE_CODE);
    }

    @Override
    public void setRefreshState(boolean isShow) {
        this.rvAnnouncement.setRefreshing(isShow);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MessageBean message = data.getParcelableExtra(LogisticApi.INTENT_EXTRA_PARAM_MESSAGE);
        this.messageAdapter.updateMessage(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.messagePresenter.detachView();
    }
}
