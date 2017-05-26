package com.kongdy.rxnetdemo.base;

import android.animation.Animator;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.kongdy.rxnetdemo.anim.AlphaAndSlideRightAnimation;
import com.kongdy.rxnetdemo.anim.BaseAnimation;

import java.util.List;

/**
 * @author kongdy
 * @date 2017-04-26 16:30
 * @TIME 16:30
 *
 * binding与recycler adapter组合的基类
 *
 **/
public abstract class BaseRecyclerAdapter<T1, T2 extends ViewDataBinding> extends RecyclerView.Adapter<BaseRecyclerAdapter.BaseViewHolder> {

    protected int layoutId;
    protected List<T1> mDatas;
    protected Context mContext;

    protected int variableId = 0;

    private boolean openAnimation = false;
    private boolean onlyFirstPlayAnimation = false;

    protected OnItemClickListener OnItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;

    private BaseAnimation mBaseAnimation = new AlphaAndSlideRightAnimation();

    private long animDuration = 300L;

    public BaseRecyclerAdapter(int layoutId, List<T1> mDatas, int variableId) {
        this.layoutId = layoutId;
        this.mDatas = mDatas;
        this.variableId = variableId;
    }

    public BaseRecyclerAdapter(int layoutId, List<T1> mDatas) {
        this(layoutId,mDatas,0);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T1 model = mDatas.get(holder.getLayoutPosition());
        if (variableId != 0)
            holder.setBindingData(variableId, model);
        if(null != OnItemClickListener)
            holder.itemView.setOnClickListener(getClickListener(holder.getLayoutPosition(),holder.itemView));
        if(null != onItemLongClickListener)
            holder.itemView.setOnLongClickListener(getLongClickListener(holder.getLayoutPosition(),holder.itemView));
        convert((T2) holder.getHolderBinding(), model);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    private View.OnClickListener getClickListener(final int pos, final View view) {
        return (v -> OnItemClickListener.onItemClick(this, view, pos));
    }

    private View.OnLongClickListener getLongClickListener(final int pos, final View view) {
        return (v -> {
            onItemLongClickListener.onItemLongClick(this, view, pos);
            return true;
        });
    }

    public BaseRecyclerAdapter.OnItemClickListener getOnItemClickListener() {
        return OnItemClickListener;
    }

    public void setOnItemClickListener(BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
        OnItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    protected abstract void convert(T2 binding, T1 item);

    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding holderBinding;

        public BaseViewHolder(View itemView) {
            super(itemView);
            holderBinding = DataBindingUtil.bind(itemView);
        }

        public BaseViewHolder setBindingData(int variableId, Object object) {
            holderBinding.setVariable(variableId, object);
            holderBinding.executePendingBindings();
            return this;
        }

        public ViewDataBinding getHolderBinding() {
            return holderBinding;
        }
    }

    public boolean isOpenAnimation() {
        return openAnimation;
    }

    public void setOpenAnimation(boolean openAnimation) {
        this.openAnimation = openAnimation;
    }

    public boolean isOnlyFirstPlayAnimation() {
        return onlyFirstPlayAnimation;
    }

    public void setOnlyFirstPlayAnimation(boolean onlyFirstPlayAnimation) {
        this.onlyFirstPlayAnimation = onlyFirstPlayAnimation;
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        addAnimation(holder);
    }

    private void addAnimation(RecyclerView.ViewHolder holder) {
        if(openAnimation && !onlyFirstPlayAnimation) {
            if(null != mBaseAnimation) {
                for(Animator animator : mBaseAnimation.getAnimators(holder.itemView)) {
                    startAnim(animator,holder.getLayoutPosition());
                }
            }
        }
    }

    private void startAnim(Animator anim, int index) {
        anim.setDuration(animDuration).start();
    }

    public interface OnItemClickListener {
        void onItemClick(BaseRecyclerAdapter adapter, View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(BaseRecyclerAdapter adapter, View view, int position);
    }
}
