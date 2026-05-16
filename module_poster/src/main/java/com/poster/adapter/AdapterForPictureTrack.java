package com.poster.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.bumptech.glide.Glide;
import com.poster.R;
import com.poster.http.UrlConfig;
import com.poster.model.PreviewModel;
import com.poster.utils.DensityUtils;

public class AdapterForPictureTrack {
    private ItemClickListener mListener;
    private Context mContext;

    public AdapterForPictureTrack(Context context) {
        this.mContext = context;
    }

    public void loadView(PreviewModel mPreview, LinearLayout mListView) {
        if (mPreview == null || mPreview.getTemplate() == null || mPreview.getTemplate().getPages() == null) {
            return;
        }

        for (int index = 0; index < mPreview.getTemplate().getPages().size(); index++) {
            ConstraintLayout childView = (ConstraintLayout) View.inflate(mContext, R.layout.adapter_item_picture_track, null);
            ImageView itemView = childView.findViewById(R.id.item_img_view);
            itemView.setTag(index);

            if (index != 0) {
                childView.setBackground(null);
            }
            PreviewModel.TemplateBean.PagesBean pages = mPreview.getTemplate().getPages().get(index);
            Glide.with(mContext).load(UrlConfig.BASE_URL + pages.getIcon())
                    .override(DensityUtils.dp2px(mContext, 40), DensityUtils.dp2px(mContext, 40))
                    .into(itemView);

            int buff = index;
            itemView.setOnClickListener(v -> mListener.onClick(buff, pages, itemView));
            itemView.setOnLongClickListener(v -> {
                mListener.onLongClick(buff, pages, itemView);
                return false;
            });
            mListView.addView(childView, mListView.getChildCount() - 1);
        }
    }

    public void setOnClickListener(ItemClickListener listener) {
        this.mListener = listener;
    }

    public interface ItemClickListener {
        void onClick(int position, PreviewModel.TemplateBean.PagesBean bean, View view);

        void onLongClick(int position, PreviewModel.TemplateBean.PagesBean bean, View view);
    }
}