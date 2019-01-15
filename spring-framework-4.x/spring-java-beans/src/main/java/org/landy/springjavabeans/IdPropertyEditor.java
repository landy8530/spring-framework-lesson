package org.landy.springjavabeans;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 * Created by Landy on 2019/1/8.
 */
public class IdPropertyEditor extends PropertyEditorSupport {

    public void setAsText(String text) {
        //text相当于事件源对应的值
        //PropertyEditor source = (PropertyEditor) event.getSource();
        if (StringUtils.hasText(text)) {
            long id = Long.parseLong(text);
            setValue(id);
        } else {
            setValue(Long.MIN_VALUE);
        }
    }
}
