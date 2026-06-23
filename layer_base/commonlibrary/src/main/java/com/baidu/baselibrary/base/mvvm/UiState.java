package com.baidu.baselibrary.base.mvvm;

public final class UiState {

    public static final int IDLE = 0;
    public static final int LOADING = 1;
    public static final int SUCCESS = 2;
    public static final int EMPTY = 3;
    public static final int ERROR = 4;

    private static final UiState IDLE_STATE = new UiState(IDLE, 0, 0);
    private static final UiState LOADING_STATE = new UiState(LOADING, 0, 0);
    private static final UiState SUCCESS_STATE = new UiState(SUCCESS, 0, 0);

    public final int state;
    public final int textResId;
    public final int iconResId;

    private UiState(int state, int textResId, int iconResId) {
        this.state = state;
        this.textResId = textResId;
        this.iconResId = iconResId;
    }

    public static UiState idle() { return IDLE_STATE; }
    public static UiState loading() { return LOADING_STATE; }
    public static UiState success() { return SUCCESS_STATE; }
    public static UiState empty(int textResId, int iconResId) {
        return new UiState(EMPTY, textResId, iconResId);
    }
    public static UiState error(int textResId, int iconResId) {
        return new UiState(ERROR, textResId, iconResId);
    }

    public boolean isLoading() { return state == LOADING; }
    public boolean isSuccess() { return state == SUCCESS; }
    public boolean isEmpty() { return state == EMPTY; }
    public boolean isError() { return state == ERROR; }
    public boolean isShowEmpty() { return state == EMPTY || state == ERROR; }
}
