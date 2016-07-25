package com.std.logisticapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shiki.recyclerview.FGORecyclerViewAdapter;
import com.shiki.utils.StringUtils;
import com.std.logisticapp.R;
import com.std.logisticapp.bean.OrderBean;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Maik on 2016/5/16.
 */
public class OrderReceiptAdapter extends FGORecyclerViewAdapter<OrderReceiptAdapter.ReceiptViewHolder> {
    public interface OnItemCheckListener {
        void onOrderItemChecked(CheckBox cbItem);
    }

    private List<OrderBean> orderData;
    private final LayoutInflater layoutInflater;
    private OnItemCheckListener onItemCheckListener;

    @Inject
    public OrderReceiptAdapter(Context context) {
        this.layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.orderData = Collections.emptyList();
    }

    public void setOrderData(List<OrderBean> orderData) {
        this.orderData = orderData;
        this.notifyDataSetChanged();
    }

    public void setOrderDataChecked(Boolean isChecked) {
        for (OrderBean order : orderData) {
            order.setIsSelected(isChecked);
        }
        this.notifyDataSetChanged();
    }

    public boolean isAllSelected() {
        boolean result = true;
        for (OrderBean order : orderData) {
            if (order.getIsSelected() == null || !order.getIsSelected()) {
                result = false;
                break;
            }
        }
        return result;
    }

    public void updateAfterSubmit() {
        Iterator<OrderBean> orderIterator = orderData.iterator();
        while (orderIterator.hasNext()) {
            OrderBean order = orderIterator.next();
            if (order.getIsSelected() != null && order.getIsSelected()) {
                orderIterator.remove();
            }
        }
        this.notifyDataSetChanged();
    }

    public String getDeliverys() {
        StringBuilder sb = new StringBuilder();
        for (OrderBean order : orderData) {
            if (order.getIsSelected() != null && order.getIsSelected()) {
                sb.append(order.getOrdId() + ',');
            }
        }
        if (sb.length() > 0)
            sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }

    @Override
    public ReceiptViewHolder getViewHolder(View view) {
        return new ReceiptViewHolder(view);
    }

    @Override
    public ReceiptViewHolder onCreateViewHolder(ViewGroup parent) {
        final View view = this.layoutInflater.inflate(R.layout.item_order_receipt, parent, false);
        return new ReceiptViewHolder(view);
    }

    @Override
    public int getAdapterItemCount() {
        return orderData.size();
    }

    @Override
    public void onBindViewHolder(ReceiptViewHolder holder, int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= orderData.size() : position < orderData.size()) && (customHeaderView != null ? position > 0 : true)) {
            final int index = customHeaderView != null ? position - 1 : position;
            final OrderBean order = orderData.get(index);
            holder.tvLogisticCode.setText("物流单号：" + StringUtils.nullStrToEmpty(order.getLogisticCode()));
            holder.tvOrdCode.setText("订单编号：" + StringUtils.nullStrToEmpty(order.getOrdCode()));
            holder.tvDeliveryCode.setText("配送单号：" + StringUtils.nullStrToEmpty(order.getDeliveryCode()));
            holder.tvDeliveryAddr.setText("配送地址：" + StringUtils.nullStrToEmpty(order.getDeliveryAddr()));
            holder.tvRecipient.setText("收件人：" + StringUtils.nullStrToEmpty(order.getRecipient()));
            holder.tvOrdTel.setText("联系电话：" + StringUtils.nullStrToEmpty(order.getOrdTel()));
            holder.tvExprItem.setText("快递物品：" + StringUtils.nullStrToEmpty(order.getExprItem()));
            holder.chkSelected.setChecked(order.getIsSelected() == null ? false : order.getIsSelected());
            holder.chkSelected.setTag(order);
            holder.chkSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderReceiptAdapter.this.onItemCheckListener.onOrderItemChecked((CheckBox) v);
                }
            });
        }
    }

    static class ReceiptViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_logisticCode)
        TextView tvLogisticCode;
        @Bind(R.id.tv_ordCode)
        TextView tvOrdCode;
        @Bind(R.id.tv_deliveryCode)
        TextView tvDeliveryCode;
        @Bind(R.id.tv_deliveryAddr)
        TextView tvDeliveryAddr;
        @Bind(R.id.tv_recipient)
        TextView tvRecipient;
        @Bind(R.id.tv_ordTel)
        TextView tvOrdTel;
        @Bind(R.id.tv_exprItem)
        TextView tvExprItem;
        @Bind(R.id.chk_selected)
        CheckBox chkSelected;

        public ReceiptViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
