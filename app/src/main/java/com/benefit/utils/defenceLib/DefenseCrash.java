package com.benefit.utils.defenceLib;

import com.benefit.utils.defenceLib.core.hook.HookH;
import com.benefit.utils.defenceLib.core.hook.HookInstrumentation;
import com.benefit.utils.defenceLib.core.hook.HookThread;
import com.benefit.utils.defenceLib.core.hook.IHook;
import com.benefit.utils.defenceLib.handler.ExceptionDispatcher;
import com.benefit.utils.defenceLib.handler.IExceptionHandler;

public final class DefenseCrash {

    private static ExceptionDispatcher mExceptionDispatcher;

    private static boolean installed = false;

    private static boolean initialized = false;

    private static IHook hookThread;

    private static HookH hookH;

    private static IHook hookInstrumentation;

    private DefenseCrash() {
    }

    /**
     * initialize the defense lib
     */
    public static void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        mExceptionDispatcher = new ExceptionDispatcher();
        hookThread = new HookThread(mExceptionDispatcher);
        hookH = new HookH(mExceptionDispatcher);
        hookInstrumentation = new HookInstrumentation(mExceptionDispatcher);
    }

    /**
     * install the defense fire wall
     *
     * @param handler
     */
    public static void install(IExceptionHandler handler) {
        if (!initialized) {
            throw new IllegalStateException("need call DefenseCrash.initialize() first");
        }
        if (installed) {
            return;
        }
        installed = true;
        mExceptionDispatcher.setIExceptionHandler(handler);
        hookInstrumentation.hook();
        hookThread.hook();
        hookH.hook();
        hookH.setHookedInstrumentation(hookInstrumentation.isHooked());
    }

    /**
     * uninstall the defense fire wall
     */
    public static void unInstall() {
        if (!initialized) {
            throw new IllegalStateException("need call DefenseCrash.initialize() first");
        }
        if (!installed) {
            return;
        }
        installed = false;
        hookInstrumentation.unHook();
        hookThread.unHook();
        hookH.unHook();
        hookH.setHookedInstrumentation(hookInstrumentation.isHooked());
    }


}
