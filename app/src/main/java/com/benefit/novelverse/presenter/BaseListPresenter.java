package com.benefit.novelverse.presenter;


import com.benefit.novelverse.view.base.ListPage;
import com.meta.bean.model.api.APIError;
import com.meta.bean.model.api.APIManager;
import com.meta.bean.model.api.APIService;
import com.meta.bean.model.api.ResponseResultCallback;
import com.tencent.common.util.EmptyUtils;
import com.base.util.net.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import retrofit2.Call;

import static com.meta.bean.model.api.APIError.ERROR_LIST_EMPTY;

/**
 * 列表presenter
 */
public abstract class BaseListPresenter<T> extends MvpActivityOrFragmentPresenter<ListPage> {
    public static String TAG="ListPresenter";

    /**
     * 默认每次获取数量
     */
    public static int DEFAULT_LIMIT=8;
    protected List<T> mDataList=new ArrayList<>();

    /**
     * 请求偏移量
     */
    protected int offset;

    protected boolean isLoading;

    protected APIService mAPIService;

    protected Call<List<T>> call;

    protected boolean isForceRefresh;

    public BaseListPresenter(ListPage view) {
        super(view);
        mAPIService = APIManager.get().getApi();
        resetData();
    }

    /**
     * 重置数据
     */
    protected void resetData() {
        offset = 0;
        isLoading = false;
        mDataList=new ArrayList();
    }

    /**
     * 获取数据
     *
     * @param forceRefresh 是否强制刷新，如果为true则删除历史数据，重新获取
     */
    public void getDatas(final boolean forceRefresh) {
       getDatas(DEFAULT_LIMIT,forceRefresh);
    }

    /**
     * 获取数据
     *
     * @param forceRefresh 是否强制刷新，如果为true则删除历史数据，重新获取
     * @param limit 获取的数量
     */
    // TODO: 2018/6/28 这里强制数据来源为network，修改实现
    public void getDatas(final int limit, final boolean forceRefresh) {
        isForceRefresh=forceRefresh;
        if(!NetworkUtils.isConnected()){
            //没网
            APIError apiError=new APIError(APIError.ERROR_NO_NET);
            if(isViewAttached()){
                getView().refreshData(null, forceRefresh, apiError);
            }
            return;
        }
        if (forceRefresh) {
            resetData();
        }
        isLoading = true;
        call = generateCall(offset,limit);
        offset+=limit;
        if(call==null){
            isLoading = false;
            if(isViewAttached()){
                getView().refreshData(null, forceRefresh, new APIError(APIError.ERROR_NO_DATA_CODE));
                int diff=offset-limit;
                offset=diff>=0?diff:0;
            }
        }else{
            call.enqueue(new ResponseResultCallback<List<T>>() {
                @Override
                protected void onSuccess(Call<List<T>> call, List<T> response) {
                    isLoading = false;
                    response = hijackRespone(response);

                    if(!response.isEmpty()){
                        if(!forceRefresh){
                            mDataList.addAll(response);
                        }else {
                            //avoid java.lang.IndexOutOfBoundsException: Inconsistency detected
                            mDataList=new ArrayList<T>(response);
                        }
                        if(isViewAttached()){
                            getView().refreshData(mDataList, forceRefresh, null);
                        }
                    }else{
                        APIError apiError=new APIError(ERROR_LIST_EMPTY);
                        if(isViewAttached()){
                            getView().refreshData(null, forceRefresh, apiError);
                        }
                    }
                }

                @Override
                protected void onFail(Call<List<T>> call, APIError exception) {
                    isLoading = false;
                    if(isViewAttached()){
                        if(exception.errorCode!=APIError.ERROR_NO_DATA_CODE){
                            int diff=offset-limit;
                            offset=diff>=0?diff:0;
                        }
                        getView().refreshData(null, forceRefresh, exception);
                    }
                }

                @Override
                protected void onResponse204(Call<List<T>> call) {
                    isLoading = false;
                    APIError apiError=new APIError(ERROR_LIST_EMPTY);
                    if(isViewAttached()){
                        getView().refreshData(null, forceRefresh, apiError);
                    }
                }
            });
        }

    }



    /**
     * API生成
     * @param offset 起点
     * @param limit 数量
     * @return
     */
    protected abstract Call<List<T>> generateCall(int offset,int limit);


    /**
     * 是否是空数据
     *
     * @return
     */
    public boolean isEmptyData() {
        return EmptyUtils.isEmpty(mDataList);
    }

    /**
     * 是否加载中
     *
     * @return 是否
     */
    public boolean isLoading() {
        return isLoading;
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public boolean isForceRefresh() {
        return isForceRefresh;
    }

    protected List<T> hijackRespone(@Nullable List<T> res) { return res; }


    @Override
    public void onPause() {
        super.onPause();
        isLoading = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(call!=null&&!call.isCanceled()){
            call.cancel();
        }
    }
}
