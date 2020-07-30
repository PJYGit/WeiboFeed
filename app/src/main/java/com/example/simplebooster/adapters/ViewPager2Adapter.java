package com.example.simplebooster.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplebooster.R;
import com.example.simplebooster.activities.NineGridItemDetailActivity;
import com.example.simplebooster.data.NineGridItem;
import com.example.simplebooster.layouts.NineGridViewGroup;

import java.util.List;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ViewPager2Adapter extends RecyclerView.Adapter<ViewPager2Adapter.ViewHolder>
        implements PhotoViewAttacher.OnPhotoTapListener {

    private Context context;
    private List<NineGridItem> nineGridItemList;
    private View item_media_view;
    public final static String TAG = "ViewPager2Adapter";

    public ViewPager2Adapter() {
    }

    public ViewPager2Adapter(Context context, List<NineGridItem> nineGridItemList) {
        this.context = context;
        this.nineGridItemList = nineGridItemList;
    }

    @NonNull
    @Override
    public ViewPager2Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        item_media_view = LayoutInflater.from(context).inflate(R.layout.item_ninegrid, parent, false);
        return new ViewHolder(item_media_view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final PhotoView photoView;
        private final ConstraintLayout parentView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentView = itemView.findViewById(R.id.item_player_parent);
            // 图片
            photoView = itemView.findViewById(R.id.pv);
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    ((NineGridItemDetailActivity) context).finishActivityAnim();
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPager2Adapter.ViewHolder holder, int position) {
        // 存在视频地址
        holder.photoView.setVisibility(View.VISIBLE);

        showExcessPic(nineGridItemList.get(position), holder.photoView);
        NineGridViewGroup.getImageLoader().onDisplayImage(
                context,
                holder.photoView,
                nineGridItemList.get(position).bigImageUrl);
    }

    @Override
    public int getItemCount() {
        return nineGridItemList.size();
    }

    /**
     * 单击退场
     */
    @Override
    public void onPhotoTap(View view, float x, float y) {
        ((NineGridItemDetailActivity) context).finishActivityAnim();
    }

    private boolean existVideoUrl(List<NineGridItem> nineGridItemList, int position) {
        return !TextUtils.isEmpty(nineGridItemList.get(position).getVideoUrl());
    }

    /**
     * 展示过度图片
     */
    private void showExcessPic(NineGridItem nineGridItem, PhotoView imageView) {
        //先获取大图的缓存图片
        Bitmap cacheImage = NineGridViewGroup.getImageLoader().getCacheImage(nineGridItem.bigImageUrl);
        //如果大图的缓存不存在,在获取小图的缓存
        if (cacheImage == null)
            cacheImage = NineGridViewGroup.getImageLoader().getCacheImage(nineGridItem.thumbnailUrl);
        //如果没有任何缓存,使用默认图片,否者使用缓存
        if (cacheImage == null) {
            imageView.setImageResource(R.drawable.ic_default_color);
        } else {
            imageView.setImageBitmap(cacheImage);
        }
    }

    public View getPrimaryItem() {
        return item_media_view;
    }

    public PhotoView getPrimaryPhotoView() {
        return (PhotoView) item_media_view.findViewById(R.id.pv);
    }
}
