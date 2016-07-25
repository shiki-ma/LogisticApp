package com.std.logisticapp.model.impl;

import com.std.logisticapp.bean.MessageBean;
import com.std.logisticapp.bean.ResultBean;
import com.std.logisticapp.logistic.LogisticMain;
import com.std.logisticapp.model.IMessageModel;

import java.util.List;

import rx.Observable;

/**
 * Created by Maik on 2016/5/10.
 */
public class MessageModel implements IMessageModel {
    private static final MessageModel instance = new MessageModel();

    public static MessageModel getInstance() {
        return instance;
    }

    private MessageModel() {
    }


    @Override
    public Observable<ResultBean<List<MessageBean>>> getMessageList(String expressKey) {
        return LogisticMain.getInstance().getLogisticService().getMessageList(expressKey);
    }

    @Override
    public Observable<ResultBean<MessageBean>> getMessageDetail(String expressId, String messageId) {
        return LogisticMain.getInstance().getLogisticService().getMessageDetail(expressId, messageId);
    }
}
