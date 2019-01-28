package org.landy.javai18ndemo.javase;

import java.text.MessageFormat;

/**
 * {@link MessageFormat} 示例
 *
 * @author Landy
 * @copyright Landy
 * @since 2018/1/28
 */
public class MessageFormatDemo {

    public static void main(String[] args) {
        // Formatter
        MessageFormat format = new MessageFormat("Hello,{0},{1}!");
        System.out.println(format.format(new Object[]{"World","Gupao"}));
    }
}
