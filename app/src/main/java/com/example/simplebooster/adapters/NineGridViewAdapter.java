package com.example.simplebooster.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.simplebooster.R;
import com.example.simplebooster.activities.EmptyActivity;
import com.example.simplebooster.activities.NineGridItemDetailActivity;
import com.example.simplebooster.data.NineGridItem;
import com.example.simplebooster.layouts.NineGridViewGroup;
import com.example.simplebooster.views.NineGridItemWrapperView;

import java.io.Serializable;
import java.util.List;

/**
 * 这个类是此模块的入口
 */
public class NineGridViewAdapter {

    private List<NineGridItem> nineGridItemList;

    public NineGridViewAdapter(List<NineGridItem> nineGridItemList) {
       this.nineGridItemList = nineGridItemList;
    }
    public List<NineGridItem> getNineGridItemList() {
        return nineGridItemList;
    }

    public void onNineGridItemClick(Context context, NineGridViewGroup nineGridViewGroup,
                                    int index, List<NineGridItem> nineGridItemList) {
        // 遍历 nineGridItemList，计算每张图片的宽高和图片起始点
        for (int i = 0; i < nineGridItemList.size(); i++) {
            NineGridItem nineGridItem = nineGridItemList.get(i);
            View nineGridViewItem;
            if (i < nineGridViewGroup.getMaxSize()) {
                nineGridViewItem = nineGridViewGroup.getChildAt(i);
            } else {
                nineGridViewItem = nineGridViewGroup.getChildAt(nineGridViewGroup.getMaxSize() - 1);
            }
            nineGridItem.nineGridViewItemWidth = nineGridViewItem.getWidth();
            nineGridItem.nineGridViewItemHeight = nineGridViewItem.getHeight();
            int[] points = new int[2];
            nineGridViewItem.getLocationInWindow(points);
            nineGridItem.nineGridViewItemX = points[0];
            nineGridItem.nineGridViewItemY = points[1];
        }

//        Intent intent = new Intent(context, EmptyActivity.class);

        Intent intent = new Intent(context, NineGridItemDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(NineGridItemDetailActivity.MEDIA_INFO, (Serializable) nineGridItemList);
        bundle.putInt(NineGridItemDetailActivity.CURRENT_ITEM, index);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    public ImageView generateImageView(Context context) {
        NineGridItemWrapperView imageView = new NineGridItemWrapperView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.ic_default_color);
        return imageView;
    }
}
