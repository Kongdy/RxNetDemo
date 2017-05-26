package com.kongdy.rxnetdemo.base;

import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.kongdy.rxnetdemo.model.SectionEntity;

import java.util.List;

/**
 * @author kongdy
 * @date 2017-04-26 17:01
 * @TIME 17:01
 **/

public abstract class BaseSectionAdapter<T1 extends SectionEntity, T2 extends ViewDataBinding> extends BaseRecyclerAdapter<T1,T2> {

    protected static final int SECTION_HEAD_VIEW = 0x00000444;
    protected int mSectionLayoutId;

    public BaseSectionAdapter(int layoutId, List<T1> mDatas, int variableId,int mSectionLayoutId) {
        super(layoutId, mDatas, variableId);
        this.mSectionLayoutId = mSectionLayoutId;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == SECTION_HEAD_VIEW)
            return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(mSectionLayoutId,parent,false));
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).isHeader?SECTION_HEAD_VIEW:super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T1 model = mDatas.get(holder.getLayoutPosition());
        switch (holder.getItemViewType()) {
            case SECTION_HEAD_VIEW:
                convertSection((T2) holder.getHolderBinding(),model);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }

    }

    protected abstract void convertSection(T2 binding, T1 item);
}
