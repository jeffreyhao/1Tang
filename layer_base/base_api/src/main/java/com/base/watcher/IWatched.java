package com.base.watcher;



public interface IWatched {


	/**
	 * 通知监听者
	 *
	 * @param event 事件标记 event 	{@link WatcherEvent}
	 * @param object 通知传参
	 */
	void notifyWatcher(int event, Object object);
}
