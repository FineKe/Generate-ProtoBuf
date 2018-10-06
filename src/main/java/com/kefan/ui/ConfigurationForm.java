package com.kefan.ui;

import com.kefan.service.ConfigurationPersistentService;

import javax.swing.*;

/**
 * @note:
 * @author: fine
 * @date: 2018/10/6 3:00 PM
 */
@SuppressWarnings("unchecked")
public class ConfigurationForm{
    private JPanel root;
    private JTextField protocPathTextFiled;

    private ConfigurationPersistentService persistent = ConfigurationPersistentService.getInstance();

    public JComponent getRootPanel() {
        protocPathTextFiled.setText(persistent.getConfig().getProtocPath());
        return root;
    }


    public JTextField jTextField() {
        return protocPathTextFiled;
    }
}
