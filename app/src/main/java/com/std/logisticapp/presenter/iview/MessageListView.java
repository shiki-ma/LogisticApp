package com.std.logisticapp.presenter.iview;

import com.std.logisticapp.bean.MessageBean;
import com.std.logisticapp.core.mvp.MvpView;

import java.util.List;

/**
 * Created by Maik on 2016/5/9.
 */
public interface MessageListView extends MvpView {
    void renderMessageList(List<MessageBean> messageData);
    void viewMessage(MessageBean message);
    void setRefreshState(boolean isShow);
}
