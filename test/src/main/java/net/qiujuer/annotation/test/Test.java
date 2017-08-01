package net.qiujuer.annotation.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */

public class Test {
    @Inherited
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface DbDatabase {
        String name();

        int version() default -1;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface DbTable {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Column {

        boolean notnull() default false;

        String name() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD,ElementType.CONSTRUCTOR})
    @interface PrimaryKey {
        String name() default "";
    }

    @DbTable("TB_User")
    @DbDatabase(name = "TestDatabase", version = 1)
    static class UserTable {
        @PrimaryKey
        public UserTable() {
        }

        @PrimaryKey
        long id;

        @Column(notnull = true, name = "Name")
        String name;

        @Column(name = "Sex")
        String sex;

        @Column(name = "Pic")
        String picture;
    }


    public static void main(String args[]) {

    }


}
