package com.cheng315.nfc.viewholder;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by Administrator on 2017/8/17.
 */

public class ViewHolder {


    /**
     * viewholder
     *
     */
    public static <T extends View> T getView(View view, int viewId) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();

        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            view.setTag(viewHolder);
        }

        View childView = viewHolder.get(viewId);


        if (childView == null) {
            childView = view.findViewById(viewId);
            viewHolder.put(viewId, childView);
        }
        return (T) childView;
    }

}
