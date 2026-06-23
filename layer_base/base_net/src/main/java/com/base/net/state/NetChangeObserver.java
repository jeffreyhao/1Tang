package com.base.net.state;


import android.content.Context;
import android.net.ConnectivityManager;

import com.base.api.GlobalContext;
import com.base.api.Logger;
import com.base.net.state.tracker.ITrack;
import com.base.net.state.tracker.NetworkStateTracker;
import com.base.net.state.type.NetworkState;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by haojiangfeng on 2023/9/21.
 */
public class NetChangeObserver implements NetChangeCallback{

    private static final NetChangeObserver mInstance = new NetChangeObserver();
    public static NetChangeObserver getInstance() {
        return mInstance;
    }
    private NetChangeObserver() {
        init();
    }



    private Set<NetChangeCallback> mNetChangeObservers;
    private ConnectivityManager mConnectivityManager;
    private ITrack mTracker;

    private void init() {
        mNetChangeObservers = new HashSet<>();
        mConnectivityManager = (ConnectivityManager) GlobalContext.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        mTracker = new NetworkStateTracker(mConnectivityManager, this);
        mTracker.startTracking();
    }

    public NetworkState getNetworkState(){
        return mTracker.getCurrentState();
    }

    public void registerNetChangeObserver(NetChangeCallback listener) {
        synchronized (NetChangeObserver.class) {
            if (listener != null) {
                mNetChangeObservers.add(listener);
            }
        }
    }

    public void unRegisterNetChangeObserver(NetChangeCallback ob) {
        synchronized (NetChangeObserver.class) {
            mNetChangeObservers.remove(ob);
        }
    }


    @Override
    public void onNetChanged(NetworkState oldState, NetworkState newState) {
        String log = "notifyNetChange: isNetValid = " + newState.isNetAvailable();
        Logger.w("NetworkStateTracker", log);
        Logger.textSingle("NetChangeObserver", "onNetChanged", log);

        synchronized (NetChangeObserver.class) {
            for (NetChangeCallback observer : mNetChangeObservers) {
                observer.onNetChanged(oldState, newState);
            }
        }
    }


}
