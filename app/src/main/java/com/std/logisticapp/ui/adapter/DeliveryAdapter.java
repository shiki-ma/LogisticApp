package com.std.logisticapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiki.recyclerview.FGORecyclerViewAdapter;
import com.shiki.utils.StringUtils;
import com.std.logisticapp.R;
import com.std.logisticapp.bean.OrderBean;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Eric on 2016/5/19.
 */
public class DeliveryAdapter extends FGORecyclerViewAdapter<DeliveryAdapter.DeliveryViewHolder> {
    public interface OnItemClickListener {
        void onDeliveryItemClicked(OrderBean order);
    }

    public interface OnCallClickListener {
        void onDeliveryCallClicked(OrderBean order);
    }

    public interface OnComplaintClickListener {
        void onDeliveryComplaintClicked(OrderBean order);
    }

    private List<OrderBean> mOrderList;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;
    private OnCallClickListener mOnCallClickListener;
    private OnComplaintClickListener mOnComplaintClickListener;
    private boolean mIsComplaint = false;

    private static final int NORMAL_ITEM = 0;
    private static final int GROUP_ITEM = 1;

    @Inject
    public DeliveryAdapter(Context context) {
        this.mLayoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mOrderList = Collections.emptyList();
        this.mContext = context;
    }

    public DeliveryAdapter(Context context,boolean isComplaint) {
        this.mLayoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mOrderList = Collections.emptyList();
        this.mContext = context;
        mIsComplaint = isComplaint;
    }

    static class DeliveryViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_deliveryCode)
        TextView mTvDeliveryCode;
        @Bind(R.id.tv_deliveryAddr)
        TextView mTvDeliveryAddr;
        @Bind(R.id.tv_recipient)
        TextView mTvRecipient;
        @Bind(R.id.tv_exprItem)
        TextView mTvExprItem;
        @Bind(R.id.tv_appointment)
        TextView mTvAppointment;
        @Bind(R.id.iv_remind)
        ImageView mIvRemind;
        @Bind(R.id.iv_late)
        ImageView mIvLate;
        @Bind(R.id.iv_complaint)
        ImageView mIvComplaint;
        @Bind(R.id.iv_call)
        ImageView mIvCall;
        @Bind(R.id.cv_item)
        CardView mCvItem;

        public DeliveryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 带订单号标题
     */
    public class OrderViewHolder extends DeliveryViewHolder {
        @Bind(R.id.tv_ordCode)
        TextView mTvOrdCode;

        public OrderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public DeliveryViewHolder getViewHolder(View view) {
        return new DeliveryViewHolder(view);
    }

    @Override
    public DeliveryViewHolder onCreateViewHolder(ViewGroup parent) {
        final View view = this.mLayoutInflater.inflate(R.layout.item_delivery, parent, false);
        return new DeliveryViewHolder(view);
    }

    @Override
    public DeliveryViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        if (viewType == GROUP_ITEM) {
            final View view = this.mLayoutInflater.inflate(R.layout.item_delivery_order, parent, false);
            return new OrderViewHolder(view);
        } else {
            final View view = this.mLayoutInflater.inflate(R.layout.item_delivery, parent, false);
            return new DeliveryViewHolder(view);
        }

    }

    @Override
    public int getAdapterItemCount() {
        return mOrderList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= mOrderList.size() : position < mOrderList.size()) && (customHeaderView != null ? position > 0 : true)) {
            final int index = customHeaderView != null ? position - 1 : position;
            //第一个要显示订单号
            if (index == 0)
                return GROUP_ITEM;

            String orderCode = mOrderList.get(index).getOrdCode();
            int prevIndex = index - 1;
            boolean isDifferent = !mOrderList.get(prevIndex).getOrdCode().equals(orderCode);
            return isDifferent ? GROUP_ITEM : NORMAL_ITEM;
        }
        return NORMAL_ITEM;

    }

    @Override
    public void onBindViewHolder(DeliveryViewHolder holder, final int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= mOrderList.size() : position < mOrderList.size()) && (customHeaderView != null ? position > 0 : true)) {
            final int index = customHeaderView != null ? position - 1 : position;
            final OrderBean order = mOrderList.get(index);
            if (holder instanceof OrderViewHolder) {
                ((OrderViewHolder) holder).mTvOrdCode.setText(mContext.getText(R.string.dl_order_code)+":"+StringUtils.nullStrToEmpty(order.getOrdCode()));
            }
            holder.mTvDeliveryCode.setText(mContext.getText(R.string.dl_delivery_code)+":"+ StringUtils.nullStrToEmpty(order.getDeliveryCode()));
            holder.mTvDeliveryAddr.setText(mContext.getText(R.string.dl_delivery_addr)+":"+StringUtils.nullStrToEmpty(order.getOrdCode()));
            holder.mTvRecipient.setText(mContext.getText(R.string.dl_recipient)+":"+StringUtils.nullStrToEmpty(order.getRecipient()));
            holder.mTvExprItem.setText(mContext.getText(R.string.dl_expr_item)+":"+StringUtils.nullStrToEmpty(order.getExprItem()));
            holder.mTvAppointment.setText(mContext.getText(R.string.dl_appointment)+":"+StringUtils.nullStrToEmpty(order.getAppointment()));
            if(mIsComplaint){
                holder.mIvRemind.setVisibility(View.GONE);
                if(order.getComplaintNum()>0){
                    holder.mIvComplaint.setVisibility(View.VISIBLE);
                    holder.mIvComplaint.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (DeliveryAdapter.this.mOnComplaintClickListener != null) {
                                order.setIndex(index);
                                order.setPosition(position);
                                DeliveryAdapter.this.mOnComplaintClickListener.onDeliveryComplaintClicked(order);
                            }
                        }
                    });
                }else{
                    holder.mIvComplaint.setVisibility(View.GONE);
                }
                holder.mIvLate.setVisibility(View.GONE);
                holder.mIvCall.setVisibility(View.GONE);
            }else{
                if(order.getRemindNum()>0){
                    holder.mIvRemind.setVisibility(View.VISIBLE);
                }else{
                    holder.mIvRemind.setVisibility(View.GONE);
                }
                if(order.getComplaintNum()>0){
                    holder.mIvComplaint.setVisibility(View.VISIBLE);
                    holder.mIvComplaint.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (DeliveryAdapter.this.mOnComplaintClickListener != null) {
                                order.setIndex(index);
                                order.setPosition(position);
                                DeliveryAdapter.this.mOnComplaintClickListener.onDeliveryComplaintClicked(order);
                            }
                        }
                    });
                }else{
                    holder.mIvComplaint.setVisibility(View.GONE);
                }
                if(order.getLateNum()>0){
                    holder.mIvLate.setVisibility(View.VISIBLE);
                }else{
                    holder.mIvLate.setVisibility(View.GONE);
                }
                if(StringUtils.isEmpty(order.getOrdTel())){
                    holder.mIvCall.setVisibility(View.GONE);
                }else{
                    holder.mIvCall.setVisibility(View.VISIBLE);
                    holder.mIvCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (DeliveryAdapter.this.mOnCallClickListener != null) {
                                DeliveryAdapter.this.mOnCallClickListener.onDeliveryCallClicked(order);
                            }
                        }
                    });
                }
                holder.mCvItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (DeliveryAdapter.this.mOnItemClickListener != null) {
                            order.setIndex(index);
                            order.setPosition(position);
                            DeliveryAdapter.this.mOnItemClickListener.onDeliveryItemClicked(order);
                        }
                    }
                });
            }
        }
    }



    public void setOrderList(List<OrderBean> orderList) {
        this.mOrderList = orderList;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnCallClickListener(OnCallClickListener onCallClickListener) {
        this.mOnCallClickListener = onCallClickListener;
    }

    public void setOnComplaintClickListener(OnComplaintClickListener onComplaintClickListener) {
        this.mOnComplaintClickListener = onComplaintClickListener;
    }

    public void removeOrder(OrderBean order) {
        OrderBean orderBean = this.mOrderList.get(order.getIndex());
        this.mOrderList.remove(orderBean);
        this.notifyDataSetChanged();
        //this.notifyItemRemoved(order.getPosition());
        /*MessageBean newMessage = this.messageData.get(message.getIndex());
        newMessage.setMessageFlag(message.getMessageFlag());
        this.notifyItemChanged(message.getPosition());*/
    }

    public void updateOrder(OrderBean order) {
        OrderBean orderBean = this.mOrderList.get(order.getIndex());
        orderBean.setAppointment(order.getAppointment());
        //orderBean.setComplaintBack(order.getComplaintBack());
        this.notifyItemChanged(order.getPosition());
    }

    public void updateOrderComplaint(OrderBean order) {
        OrderBean orderBean = this.mOrderList.get(order.getIndex());
        orderBean.setComplaintNum(0);
        this.notifyItemChanged(order.getPosition());
    }

}
