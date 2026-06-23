package com.github.bean.database;

import android.database.Cursor;
import android.text.TextUtils;

import com.baidu.baselibrary.log.ALog;
import com.baidu.baselibrary.util.App;
import com.common.bean.R;
import com.github.bean.database.annotation.TableFieldV20;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.InvalidDBConfiguration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by haojiangfeng on 2024/2/23.
 */
class DbAutoUpdateMigration extends BaseMigration {

    protected String getDatabaseName() {
        return App.getString(R.string.database_name);
    }

    @Override
    public void migrate(DatabaseWrapper database) {
        try {
            List<Class<?>> classes = FlowManager.getDatabase(getDatabaseName()).getModelClasses();
            for (Class c : classes) {
                try {
                    Cursor cursor = database.rawQuery("SELECT * FROM " + c.getSimpleName(), null);
                    Field[] fields = c.getDeclaredFields();
                    for (Field field : fields) {

                        // 查找注解：TableFieldV20
                        if (field.isAnnotationPresent(TableFieldV20.class)) {

                            Annotation annotation = field.getAnnotation(TableFieldV20.class);
                            TableFieldV20 tableFieldV20 = (TableFieldV20) annotation;

                            String fieldName = field.getName();
                            if(tableFieldV20 != null){
                                String tableFieldName = tableFieldV20.name();
                                if(!TextUtils.isEmpty(tableFieldName)){
                                    fieldName = tableFieldName;
                                }
                            }
                            if (cursor.getColumnIndex(fieldName) < 0) {
                                // 缺少的字段
                                ALog.textSingle("DbAutoUpdateMigration", "migrate(DatabaseWrapper)", "Database table:" + c.getSimpleName() + ", add field: " + fieldName);


                                QueryBuilder queryBuilder = new QueryBuilder().append("ALTER")
                                        .appendSpaceSeparated("TABLE")
                                        .appendSpaceSeparated(c.getSimpleName())
                                        .appendSpaceSeparated("ADD COLUMN")
                                        .appendSpaceSeparated(QueryBuilder.quoteIfNeeded(field.getName()));
                                String sql = queryBuilder.getQuery();
                                database.execSQL(sql);
                            }
                        }
                    }
                    cursor.close();
                } catch (Exception e) {
                    ALog.exception("DbAutoUpdateMigration", "migrate1", e);
                }
            }
        } catch (InvalidDBConfiguration e) {
            ALog.exception("DbAutoUpdateMigration", "migrate2", e);
        }
    }
}
