package com.poster.model;

import java.util.Comparator;

public class BeanComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        int id1 = ((PreviewModel.TemplateBean.PagesBean.ObjectsBean) o1).getId();
        int id2 = ((PreviewModel.TemplateBean.PagesBean.ObjectsBean) o2).getId();
        if (id1 < id2) {
            return 1;
        } else if (id1 > id2) {
            return -1;
        } else {
            return 0;
        }
    }
}
