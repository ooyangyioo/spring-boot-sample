package org.yangyi.project.mybatis;

import java.lang.reflect.Field;

public interface IdStrategy {
    void handle(Object object, Field field) throws IllegalAccessException;
}
