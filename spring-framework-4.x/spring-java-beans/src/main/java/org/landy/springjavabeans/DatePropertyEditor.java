package org.landy.springjavabeans;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Landy on 2019/1/8.
 */
public class DatePropertyEditor extends PropertyEditorSupport {

    public void setAsText(String text){
        if(StringUtils.hasText(text)){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = dateFormat.parse(text);
                setValue(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


}
