/**
 * Copyright (C) 2015  Haiyang Yu Android Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bright.common.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bright.common.R;
import com.bright.common.widget.pulltorefresh.IPullToRefresh;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础的Adapter
 */
public abstract class ListAdapter<DATA, HOLDER extends ListAdapter.Holder> extends BaseAdapter {
    private static final String TAG = "ListAdapter";

    /**
     * 类型 是头部
     */
    public static final int TYPE_HEADER = 0;
    /**
     * 类型是body
     */
    public static final int TYPE_BODY = 1;
    /**
     * 类型是最后一个
     */
    public static final int TYPE_FOOTER = 2;

    /**
     * 默认一页显示的数量
     */
    public static final int DEFAULT_PAGER_SIZE = 10;
    /**
     * 当前页数
     */
    protected int mPagerNumber = 1;
    /**
     * 第一页
     */
    protected int mFirstPager;
    /**
     * 可能会使用到的：例如 请求Url的参数
     */
    private Object mParamKey;
    /**
     * 可能会参数
     */
    private Object mParam;
    /**
     * 请求地址
     */
    private String mUrl;

    private boolean isLoadingMoreState = false;

    protected List<DATA> mData = new ArrayList<>();
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    public ListAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        initPagerNumber();
    }

    /**
     * 添加数据
     */
    public void setData(List<DATA> data) {
        setData(data, true);
    }

    /**
     * @param data  设定的值
     * @param force 是否强制添加值
     */
    public void setData(List<DATA> data, boolean force) {
        if (data != null) {
            mData = data;
            notifyDataSetChanged();
        } else if (force) {
            mData.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 只添加一组数据
     */
    public void setOnlyOneData(DATA data) {
        if (data != null) {
            mData.clear();
            mData.add(data);
            notifyDataSetChanged();
        } else {
            Log.i(TAG, "setOnlyOneData data is null");
        }
    }

    /**
     * 增加数据
     */
    public void plusData(List<DATA> data) {
        if (data != null) {
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 获取数据
     */
    public List<DATA> getData() {
        return mData;
    }

    /**
     * 清空数据
     */
    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size() + getHeaderCount() + getFooterCount();
    }

    /**
     * 获取移除Header的Count
     */
    public int getRealCount() {
        return mData.size();
    }

    @Override
    public DATA getItem(int position) {
        return mData.get(position);
    }

    /**
     * 获取移除 Header的Item
     */
    public DATA getRealItem(int position) {
        return mData.get(position - getHeaderCount());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 获取View的type
    public int getItemViewType(int position) {
        return TYPE_BODY;
    }

    /**
     * 初始化
     */
    protected void initPagerNumber() {
        mFirstPager = mContext.getResources().getInteger(R.integer.default_pager_number);
        mPagerNumber = mFirstPager;
    }

    /**
     * 重新设置PagerNumber
     */
    public void setPagerNumber(int pagerNumber) {
        mPagerNumber = pagerNumber;
    }

    /**
     * 重置分页
     */
    public void resetPagerNumber() {
        mPagerNumber = mFirstPager;
    }

    /**
     * 分页数加1
     */
    public void plusPagerNumber() {
        mPagerNumber++;
    }

    /**
     * 获取当前分页
     *
     * @return 获取当前分页
     */
    public int getPagerNumber() {
        return mPagerNumber;
    }

    /**
     * 获取分页大小
     */
    public int getPagerSize() {
        return DEFAULT_PAGER_SIZE;
    }

    /**
     * 获取分页大小
     */
    public int getPagerSize(int wantPagerSize) {
        return wantPagerSize > getPagerSize() ? wantPagerSize : getPagerSize();
    }

    /**
     * 设置请求参数的关键值
     */
    public void setParamKey(Object key) {
        mParamKey = key;
    }

    /**
     * 获取请求参数的关键值
     */
    public Object getParamKey() {
        return mParamKey;
    }

    /**
     * 设定请求参数
     */
    public void setParam(Object param) {
        mParam = param;
    }

    /**
     * 获取请求参数
     */
    public Object getParam() {
        // TODO
        return mParam;
    }

    /**
     * 请求的地址
     */
    public void setUrl(String url) {
        mUrl = url;
    }

    /**
     * 获取请求的地址
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * 获取头部数量
     */
    public int getHeaderCount() {
        return 0;
    }

    /**
     * 获取Footer数量
     */
    public int getFooterCount() {
        return 0;
    }

    /**
     * 设置是否是加载更多模式
     */
    public void setLoadingMoreState(boolean state) {
        isLoadingMoreState = state;
    }

    /**
     * 判断是否是加载更多模式
     */
    public boolean isLoadingMoreState() {
        return isLoadingMoreState;
    }

    public void resetPullTorefresh(IPullToRefresh pullToRefresh) {
        pullToRefresh.setPullRefreshEnable(true);
        pullToRefresh.setPullLoadEnable(true);
        pullToRefresh.setEmptyState();
        if (isLoadingMoreState()) {
            pullToRefresh.stopLoadMore();
        } else {
            pullToRefresh.setRefreshFail();
        }
    }

    /**
     * 消化数据
     */
    public void resolveData(IPullToRefresh pullToRefresh, List<DATA> datas, int totalcount) {
        if (isLoadingMoreState()) {
            pullToRefresh.stopLoadMore();
            plusData(datas);
        } else {
            setData(datas);
            pullToRefresh.setRefreshSuccess();
        }
        // 修复pagernumber
        repairPagerNumber(datas);

        pullToRefresh.setPullRefreshEnable(true);
        pullToRefresh.setEmptyState();
        int realCount = getRealCount();
        if (realCount == 0) {
            pullToRefresh.setPullLoadEnable(false);
        } else if (realCount >= totalcount) {
            pullToRefresh.setPullLoadEnable(true);
            pullToRefresh.setLoadEnd();
        } else {
            pullToRefresh.setPullLoadEnable(true);
        }

    }

    /**
     * 修复pagerSize
     */
    private void repairPagerNumber(List<DATA> datas) {
        if (datas == null) {
            return;
        }
        int defaultSize = getPagerSize();
        int size = datas.size();
        // 如果是第一页 && 返回的数据必当前分页多
        if (getPagerNumber() == mFirstPager && size > defaultSize) {
            setPagerNumber(size / defaultSize + mFirstPager);
        } else {
            plusPagerNumber();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        final int type = getItemViewType(position);
        if (convertView == null) {
            holder = onCreateViewHolder(position, type);
            convertView = holder.getItemView();
        } else {
            holder = (Holder) convertView.getTag(R.id.tag_view_holder);
            // 如果当前的View Type 和 已经缓存的不同就重新加载
            int _type = holder.getType();
            if (type != _type) {
                holder = onCreateViewHolder(position, type);
                convertView = holder.getItemView();
            }

        }
        onBindViewHolder((HOLDER) holder, position, type);
        return convertView;
    }

    /**
     * 创建View容器
     *
     * @param position 当前的 position
     * @param type     当前view的type
     * @return 当前view的容器
     */
    public abstract HOLDER onCreateViewHolder(int position, int type);

    /**
     * 通过容器来 绑定view
     *
     * @param holder   当前view的容器
     * @param position 当前的 position
     * @param type     当前view的type
     */
    public abstract void onBindViewHolder(HOLDER holder, int position, int type);


    /**
     * ViewHolder复用View的
     */
    public static abstract class Holder {
        private View item;
        private int mType;

        public Holder(View item, int type) {
            mType = type;
            if (item == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.item = item;
            item.setTag(R.id.tag_view_holder, this);
        }

        public int getType() {
            return mType;
        }

        public View getItemView() {
            return item;
        }
    }

}
