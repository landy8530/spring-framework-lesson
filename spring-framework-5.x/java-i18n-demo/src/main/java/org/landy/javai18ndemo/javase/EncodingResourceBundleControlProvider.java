package org.landy.javai18ndemo.javase;

import java.util.ResourceBundle;
import java.util.spi.ResourceBundleControlProvider;

/**
 * SPI机制：ServiceLoader<ResourceBundleControlProvider> serviceLoaders = ServiceLoader.loadInstalled(ResourceBundleControlProvider.class);
 * @author Landy
 * @copyright Landy
 * @since 2018/1/28
 */
public class EncodingResourceBundleControlProvider implements
        ResourceBundleControlProvider {

    @Override
    public ResourceBundle.Control getControl(String baseName) {
        return new ResourceBundleDemo.EncodedControl();
    }
}
