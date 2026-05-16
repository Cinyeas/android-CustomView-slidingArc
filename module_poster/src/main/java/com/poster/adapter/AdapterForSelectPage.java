package com.poster.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.poster.R;
import com.poster.http.UrlConfig;
import com.poster.model.PreviewModel;
import com.poster.utils.DensityUtils;

import java.util.List;

public class AdapterForSelectPage extends BaseAdapter {
    private List<PreviewModel.TemplateBean.PagesBean.ObjectsBean> mDataBean;
    private Context mContext;
    private ItemListener mIml;

    public AdapterForSelectPage(Context context) {
        this.mContext = context;
    }

    public void setData(List<PreviewModel.TemplateBean.PagesBean.ObjectsBean> mData) {
        this.mDataBean = mData;
    }

    @Override
    public int getCount() {
        if (mDataBean == null) {
            return 0;
        }
        return mDataBean.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.adapter_item_select_page, null);
            holder = new ViewHolder();
            holder.mImage = convertView.findViewById(R.id.select_item_img);
            holder.mTitle = convertView.findViewById(R.id.select_item_tv);
            holder.mValue = convertView.findViewById(R.id.select_item_value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mDataBean.get(position).getType() == 0) {
            holder.mTitle.setText("文本");
            holder.mValue.setVisibility(View.VISIBLE);
            holder.mImage.setVisibility(View.GONE);
            holder.mValue.setText(mDataBean.get(position).getText());
        } else if (mDataBean.get(position).getType() == 1) {
            holder.mTitle.setText("图片");
            loadView(holder.mValue, holder.mImage, UrlConfig.BASE_URL + mDataBean.get(position).getImg());
        } else if (mDataBean.get(position).getType() == 2) {
            holder.mTitle.setText("动画");
            loadView(holder.mValue, holder.mImage, UrlConfig.BASE_URL + mDataBean.get(position).getImg());
        } else if (mDataBean.get(position).getType() == 3) {
            holder.mTitle.setText("视频");
            loadView(holder.mValue, holder.mImage, UrlConfig.BASE_URL + mDataBean.get(position).getImg());
        }

        convertView.setTag(R.id.tag_widget_view, mDataBean.get(position).getId());
        convertView.setOnClickListener(v -> mIml.onItemClick(v));
        return convertView;
    }

    private void loadView(TextView mValue, ImageView mImage, String url) {
        mImage.setVisibility(View.VISIBLE);
        mValue.setVisibility(View.GONE);
        Glide.with(mContext).load(url)
                .override(DensityUtils.dp2px(mContext, 40), DensityUtils.dp2px(mContext, 40))
                .into(mImage);
    }

    class ViewHolder {
        TextView mTitle;
        TextView mValue;
        ImageView mImage;
    }

    public void setItemListener(ItemListener iml) {
        this.mIml = iml;
    }

    public interface ItemListener {
        void onItemClick(View view);
    }
}
