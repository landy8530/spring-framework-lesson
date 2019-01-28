package org.landy.javai18ndemo.javase;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * {@link NumberFormat} 示例
 *
 * @author Landy
 * @copyright Landy
 * @since 2018/1/28
 */
public class NumberFormatDemo {

    public static void main(String[] args) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        System.out.println(numberFormat.format(10000));

        numberFormat = NumberFormat.getNumberInstance(Locale.FRANCE);
        System.out.println(numberFormat.format(10000));
    }
}
