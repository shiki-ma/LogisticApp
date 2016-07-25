package com.std.logisticapp.presenter.iview;

import com.std.logisticapp.bean.MessageBean;
import com.std.logisticapp.core.mvp.MvpView;

/**
 * Created by Maik on 2016/5/12.
 */
public interface MessageDetailView extends MvpView {
    void renderMessageDetail(MessageBean message);
    void setTitleWithFormat(String title);
    void setSubTitleWithFormat(String subtitle);
    void setContentWithFormat(String content);
}
