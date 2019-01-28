package org.landy.javai18ndemo.javase;

import java.util.Locale;

/**
 * {@link Locale} 示例
 *
 * @author Landy
 * @copyright Landy
 * @since 2018/1/28
 */
public class LocaleDemo {

    public static void main(String[] args) {
        // 安全 PropertyPermission
        // 通过启动参数调整 -D => System.setProperty("name","value");
        // 硬编码调整 en_US，无法做到一份，到处运行
        // Locale.setDefault(Locale.US);
        // 输入默认 Locale
//        System.setProperty("user.language","ru");//无效
        System.out.println(Locale.getDefault());
    }
}
