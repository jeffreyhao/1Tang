package com.benefit.novelverse.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

/**
 * Description:
 */
public class SearchHotModel extends ViewModel {
    private final MutableLiveData<List<String>> listStr=new MutableLiveData<List<String>>();

    public LiveData<List<String>> getSelected() {
        return listStr;
    }

    public void select(List<String> mList) {
        listStr.setValue(mList);
    }
}