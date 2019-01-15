package org.landy.springjavabeans.spring;

import org.landy.springjavabeans.DatePropertyEditor;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

import java.util.Date;

/**
 * Created by Landy on 2019/1/8.
 */
public class MyPropertyEditorRegistrar implements PropertyEditorRegistrar {
    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        // 将Date 类型 date 字段设置 PropertyEditor
        registry.registerCustomEditor(Date.class,"date",new DatePropertyEditor());
    }
}
