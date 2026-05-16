package com.poster.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.poster.ActivityPoster;
import com.poster.adapter.AdapterForSelectPage;
import com.poster.model.PreviewModel;
import com.poster.R;

import java.util.List;

public class FragmentForSelect extends Fragment implements AdapterForSelectPage.ItemListener {
    private List<PreviewModel.TemplateBean.PagesBean.ObjectsBean> mData;
    private AdapterForSelectPage mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select, container, false);
        bindView(view.findViewById(R.id.select_list_view));
        return view;
    }

    private void bindView(ListView listView) {
        mAdapter = new AdapterForSelectPage(getContext());
        mAdapter.setItemListener(this);
        mAdapter.setData(mData);
        listView.setAdapter(mAdapter);
    }

    public void setData(List<PreviewModel.TemplateBean.PagesBean.ObjectsBean> pages) {
        this.mData = pages;
    }

    public void notifyDataChange() {
        mAdapter.setData(mData);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view) {
        FrameLayout mMainView = ((ActivityPoster) getActivity()).getRightEditVIew();
        ViewGroup mGroupView = (ViewGroup) mMainView.getChildAt(mMainView.getChildCount() - 1);

        for (int index = 0; index < mGroupView.getChildCount(); index++) {
            View child = mGroupView.getChildAt(index);
            if (child.getTag(R.id.tag_widget_view).toString().equals(view.getTag(R.id.tag_widget_view).toString())) {
                ActivityPoster context = (ActivityPoster) getActivity();
                context.setBorder(child);
                context.defaultViewBg(R.color.colorBlack, true);
                break;
            }
        }
    }
}
