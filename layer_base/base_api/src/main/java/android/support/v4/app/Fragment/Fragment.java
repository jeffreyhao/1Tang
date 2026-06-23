package android.support.v4.app.Fragment;

/**
 *  app已全面使用androidx，不再使用supportV4里的东西。
 *  这里使用这个Fragment，是为了CustomFragment能被神策统计到。
 *
 *  @see com.sensorsdata.analytics.android.sdk.util.SAFragmentUtils#isFragment(Object)
 */
public interface Fragment {

    boolean isResumed();

    boolean isHidden();

    boolean getUserVisibleHint();

}
