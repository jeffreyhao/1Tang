package com.github.bean.database;

import com.baidu.baselibrary.log.ALog;
import com.github.bean.database.table.BookInfo;
import com.github.bean.database.table.CatalogList;
import com.github.bean.database.table.ReadHistory;
import com.github.bean.database.table.ReadingRecord;
import com.github.bean.database.table.RechargeFailOrder;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

/**
 * 数据库版本维护
 */
@Database(version = AppDatabase.VERSION)
public class AppDatabase {


    /**
     * 数据库版本
     */
    public static final int VERSION = 29;


    @Migration(version = 20, database = AppDatabase.class)
    public static class DatabaseAutoUpdate extends DbAutoUpdateMigration {
        // auto upgrade
    }

    @Migration(version = 21, database = AppDatabase.class)
    public static class Migration21 extends AlterTableMigration<CatalogList> {

        public Migration21(Class<CatalogList> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.TEXT, "comBookId");
        }
    }

    @Migration(version = 22, database = AppDatabase.class)
    public static class Migration22 extends AlterTableMigration<RechargeFailOrder> {

        public Migration22(Class<RechargeFailOrder> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.TEXT, "price");
        }
    }

    @Migration(version = 23, database = AppDatabase.class)
    public static class Migration23 extends AlterTableMigration<BookInfo> {

        public Migration23(Class<BookInfo> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "is_vip");
        }
    }

    @Migration(version = 24, database = AppDatabase.class)
    public static class Migration24 extends AlterTableMigration<ReadHistory> {

        public Migration24(Class<ReadHistory> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "is_vip");
        }
    }

    @Migration(version = 25, database = AppDatabase.class)
    public static class Migration25 extends AlterTableMigration<ReadingRecord> {

        public Migration25(Class<ReadingRecord> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "startFeeChapter");
        }
    }


    @Migration(version = 26, database = AppDatabase.class)
    public static class Migration26 extends BaseMigration {

        @Override
        public void migrate(DatabaseWrapper db) {
            db.beginTransaction();
            try {
                db.execSQL("ALTER TABLE ReadingRecord ADD COLUMN cid TEXT");

                // 数据迁移 chapterId -> cid
                db.execSQL("UPDATE ReadingRecord SET cid = chapterId");

                // clear chapterId
                db.execSQL("UPDATE ReadingRecord SET chapterId = ''");

                db.setTransactionSuccessful();
            } catch (Throwable e) {
                ALog.crash("Migration26", "migrate", e);
            } finally {
                db.endTransaction();
            }
        }
    }

    @Migration(version = 27, database = AppDatabase.class)
    public static class Migration27 extends AlterTableMigration<BookInfo> {

        public Migration27(Class<BookInfo> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "external_book_id");
        }
    }


    @Migration(version = 28, database = AppDatabase.class)
    public static class Migration28 extends AlterTableMigration<ReadingRecord> {

        public Migration28(Class<ReadingRecord> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "is_short_story");
        }
    }

    @Migration(version = 29, database = AppDatabase.class)
    public static class Migration29 extends AlterTableMigration<BookInfo> {

        public Migration29(Class<BookInfo> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "is_short_story");
            addColumn(SQLiteType.INTEGER, "billing_begin");
        }
    }

}


