package com.jinlin.fourcloverview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J!nl!n on 15/10/19.
 * Copyright © 1990-2015 J!nl!n™ Inc. All rights reserved.
 * <p>
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽保佑,代码无bug
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 */
public abstract class BaseRVAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected final Context mContext;
    protected final List<T> mDatas;
    protected final int[] mItemLayoutId;

    public BaseRVAdapter(Context context, int... itemLayoutId) {
        this(context, null, itemLayoutId);
    }

    public BaseRVAdapter(Context context, List<T> datas, int... layoutResId) {
        this.mDatas = datas == null ? new ArrayList<T>() : new ArrayList<>(datas);
        this.mContext = context;
        this.mItemLayoutId = layoutResId;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * 创建ViewHolder时的回调
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mItemLayoutId[viewType], parent, false);
        return new ViewHolder(mContext, view);
    }

    @Override
    public void onViewRecycled(final ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        convert(holder, position, getItemViewType(position),getItem(position));
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(null, v, position, holder.getItemId());
//                    onItemClickListener.onItemClick(null, v, holder.getPosition(), holder.getItemId());
                }
            });
        }
    }

    public T getItem(int position) {
        if (position >= mDatas.size()) return null;
        return mDatas.get(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    protected abstract void convert(ViewHolder holder, int position, int type, T item);

    private AdapterView.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void add(T elem) {
        mDatas.add(elem);
        notifyDataSetChanged();
    }

    public void addAll(List<T> elem) {
        mDatas.addAll(elem);
        notifyDataSetChanged();
    }

    public void set(T oldElem, T newElem) {
        set(mDatas.indexOf(oldElem), newElem);
    }

    public void set(int index, T elem) {
        mDatas.set(index, elem);
        notifyDataSetChanged();
    }

    public void remove(T elem) {
        mDatas.remove(elem);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        mDatas.remove(index);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> elem) {
        mDatas.clear();
        mDatas.addAll(elem);
        notifyDataSetChanged();
    }

    public boolean contains(T elem) {
        return mDatas.contains(elem);
    }

    /**
     * Clear mDatas list
     */
    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

}