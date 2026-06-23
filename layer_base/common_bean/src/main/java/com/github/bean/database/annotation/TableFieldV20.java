package com.github.bean.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p/>
 * 该注解用来彻底解决各马甲包的历史数据库字段兼容问题。
 * <p/>
 *
 * <p>
 *  由于  @Column  注解只设定在编译器存在，运行代码将不存在 @Column 注解。<br/>
 *  所以使用  @TableFieldV20 ，用来获得运行期的注解。<br/>
 * </p><br/>
 *
 * <p>
 *  方案说明：<br/>
 *      1. 将各个马甲包的数据库版本更新到V20版本。<br/>
 *      2. 该版本的数据库，自动检查  @TableFieldV20  注解的字段，如果数据库没有该字段，则添加该字段。<br/>
 *      3. V20之后的数据库，按照以前的方式使用即可。<br/>
 *      4. 已经使用  @TableFieldV20  标记的字段，不要删除。 <br/>
 * </p><br/>
 *
 * @see com.raizlabs.android.dbflow.annotation.Column
 * @author haojiangfeng
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableFieldV20 {

    String name() default "";

}
