package com.base.watcher;


import com.base.api.Logger;

import java.util.ArrayList;
import java.util.List;


public class Watcher {

	private static Watcher mWatcher;

	private final List<IWatched> mIWatched;

	private Watcher() {
		mIWatched = new ArrayList<>();
	}

	public static Watcher getInstance() {
		if (mWatcher == null) {
			synchronized (Watcher.class) {
				if (mWatcher == null) {
					mWatcher = new Watcher();
				}
			}
		}
		return mWatcher;
	}

	public void registerDataSetObserver(IWatched observer) {
		if (observer == null) {
			return;
		}
		synchronized (mIWatched) {
			if (!mIWatched.contains(observer)) {
				mIWatched.add(observer);
			} else {
				Logger.w("Observer " + observer + " is already registered.");
			}

		}
	}

	public void unregisterObserver(IWatched observer) {
		if (observer == null) {
			return;
		}
		synchronized (mIWatched) {
			mIWatched.remove(observer);
		}
	}


	public void notifyWatcher(int event, Object object) {
		synchronized (mIWatched) {
			for (int i = mIWatched.size() - 1; i >= 0; i--) {
				if (mIWatched.get(i) != null) {
					mIWatched.get(i).notifyWatcher(event, object);
				}
			}
		}
	}
}