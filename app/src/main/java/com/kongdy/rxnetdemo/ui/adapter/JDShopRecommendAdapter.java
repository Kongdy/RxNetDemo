package com.kongdy.rxnetdemo.ui.adapter;

import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kongdy.rxnetdemo.BR;
import com.kongdy.rxnetdemo.R;
import com.kongdy.rxnetdemo.base.BaseRecyclerAdapter;
import com.kongdy.rxnetdemo.databinding.ViewJdShopItemBinding;
import com.kongdy.rxnetdemo.model.WareInfo;

import java.util.List;

/**
 * @author kongdy
 * @date 2017-05-26 15:02
 * @TIME 15:02
 **/

public class JDShopRecommendAdapter extends BaseRecyclerAdapter<WareInfo,ViewJdShopItemBinding> {

    public JDShopRecommendAdapter(List<WareInfo> mDatas) {
        super(R.layout.view_jd_shop_item, mDatas, BR.ware);
    }

    @Override
    protected void convert(ViewJdShopItemBinding binding, WareInfo item) {
        Glide.with(mContext)
                .load(item.getImageurl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .into(binding.acivImage);
    }
}
