package com.github.bean.app;

import java.util.Objects;

/**
 * 版本升级信息 Bean
 * 对应 Kotlin 的 data class VersionUpgradeBean
 */
public class VersionUpgradeBean {

    // 1-非强制更新, 2-强制更新
    private final int isupgrade;
    private final String upgrade_url;
    private final String upgrade_note;
    private final int refresh_ver;
    private final int screenshot;
    private final Integer screenshot_flag; // 使用 Integer 以便可以为 null
    private final int need_login;

    // 全参数构造函数
    public VersionUpgradeBean(int isupgrade, String upgrade_url, String upgrade_note, int refresh_ver, int screenshot, Integer screenshot_flag, int need_login) {
        this.isupgrade = isupgrade;
        this.upgrade_url = upgrade_url;
        this.upgrade_note = upgrade_note;
        this.refresh_ver = refresh_ver;
        this.screenshot = screenshot;
        this.screenshot_flag = screenshot_flag;
        this.need_login = need_login;
    }

    // Getter 方法
    public int getIsupgrade() {
        return isupgrade;
    }

    public String getUpgrade_url() {
        return upgrade_url;
    }

    public String getUpgrade_note() {
        return upgrade_note;
    }

    public int getRefresh_ver() {
        return refresh_ver;
    }

    public int getScreenshot() {
        return screenshot;
    }

    public Integer getScreenshot_flag() {
        return screenshot_flag;
    }

    public int getNeed_login() {
        return need_login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VersionUpgradeBean that = (VersionUpgradeBean) o;
        return isupgrade == that.isupgrade &&
                refresh_ver == that.refresh_ver &&
                screenshot == that.screenshot &&
                need_login == that.need_login &&
                Objects.equals(upgrade_url, that.upgrade_url) &&
                Objects.equals(upgrade_note, that.upgrade_note) &&
                Objects.equals(screenshot_flag, that.screenshot_flag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isupgrade, upgrade_url, upgrade_note, refresh_ver, screenshot, screenshot_flag, need_login);
    }

    @Override
    public String toString() {
        return "VersionUpgradeBean{" +
                "isupgrade=" + isupgrade +
                ", upgrade_url='" + upgrade_url + '\'' +
                ", upgrade_note='" + upgrade_note + '\'' +
                ", refresh_ver=" + refresh_ver +
                ", screenshot=" + screenshot +
                ", screenshot_flag=" + screenshot_flag +
                ", need_login=" + need_login +
                '}';
    }
}
