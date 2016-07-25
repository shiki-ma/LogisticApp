package com.std.logisticapp.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.std.logisticapp.di.HasComponent;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Maik on 2016/5/6.
 */
public abstract class BaseFragment extends Fragment {
    protected View self;
    private boolean isVisible;
    private boolean isPrepared;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (this.self == null) {
            this.self = inflater.inflate(this.getLayoutId(), container, false);
        }
        if (this.self.getParent() != null) {
            ViewGroup parent = (ViewGroup) this.self.getParent();
            parent.removeView(this.self);
        }
        ButterKnife.bind(this, this.self);
        this.initViews(this.self, savedInstanceState);
        this.initData(savedInstanceState);
        this.initListeners();
        return this.self;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
        }
    }

    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        lazyData();
    }

    protected void lazyData() {
    }

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract int getLayoutId();

    protected abstract void initViews(View self, Bundle savedInstanceState);

    protected abstract void initListeners();

    @SuppressWarnings("unchecked")
    protected <V extends View> V findView(int id) {
        return (V) this.self.findViewById(id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }
}
