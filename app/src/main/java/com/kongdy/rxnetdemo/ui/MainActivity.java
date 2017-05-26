package com.kongdy.rxnetdemo.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kongdy.rxnetdemo.R;
import com.kongdy.rxnetdemo.databinding.ActivityMainBinding;
import com.kongdy.rxnetdemo.model.Recommend;
import com.kongdy.rxnetdemo.model.Root;
import com.kongdy.rxnetdemo.model.WareInfo;
import com.kongdy.rxnetdemo.net.NetCallBack;
import com.kongdy.rxnetdemo.net.RequestScheduler;
import com.kongdy.rxnetdemo.net.RetrofitHelper;
import com.kongdy.rxnetdemo.ui.adapter.JDShopRecommendAdapter;
import com.kongdy.rxnetdemo.widgets.DialogHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private List<WareInfo> wareInfos = new ArrayList<>();

    private JDShopRecommendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 绑定dataBinding
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        adapter = new JDShopRecommendAdapter(wareInfos);
        binding.rvJdRecommend.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Uri uri = Uri.parse(wareInfos.get(position).getClickUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        binding.rvJdRecommend.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.rvJdRecommend.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
               // super.getItemOffsets(outRect, view, parent, state);

                outRect.set(0,0,0,10);
            }
        });

        /*
            请求京东首页推荐列表
                     */
        RetrofitHelper.getRetrofit()
                .getJDShopRecommend(1) // 请求接口
                .compose(RequestScheduler.defaultSchedulers()) // 加载默认配置参数 : 主线程/独立线程  自动重试机制
                .subscribe(new NetCallBack<Root<WareInfo>>(this) { // 不手动指定的情况下，默认显示加载框
                    @Override
                    public void onResponse(Root<WareInfo> wareInfoRecommend) {
                        wareInfos.clear();
                        wareInfos.addAll(wareInfoRecommend.getWareInfoList());

                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        DialogHelper.showDialog(MainActivity.this,getString(R.string.tip),"加载失败"
                        , DialogInterface::dismiss,DialogInterface::dismiss);
                    }
                });
    }
}
