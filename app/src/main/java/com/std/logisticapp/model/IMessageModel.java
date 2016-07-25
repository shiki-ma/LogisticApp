package com.std.logisticapp.model;

import com.std.logisticapp.bean.MessageBean;
import com.std.logisticapp.bean.ResultBean;

import java.util.List;

import rx.Observable;

/**
 * Created by Maik on 2016/5/10.
 */
public interface IMessageModel {
    Observable<ResultBean<List<MessageBean>>> getMessageList(String expressKey);
    Observable<ResultBean<MessageBean>> getMessageDetail(String expressId, String messageId);
}
