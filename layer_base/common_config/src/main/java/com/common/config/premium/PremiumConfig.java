package com.common.config.premium;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by niepan on 2/24/26.
 */
public class PremiumConfig {


    public interface PremiumLayoutManagerCreate {


        RecyclerView.LayoutManager getLayoutManager(Context context);

    }



    public static PremiumLayoutManagerCreate sPremiumLayoutManagerCreator;


}
