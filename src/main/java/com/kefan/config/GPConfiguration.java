package com.kefan.config;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.kefan.service.ConfigurationPersistentService;
import com.kefan.ui.ConfigurationForm;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * created by fine
 * on 2018/9/29
 */
public class GPConfiguration implements Configurable {

    private ConfigurationForm configurationForm;

    private ConfigurationPersistentService persistent = ConfigurationPersistentService.getInstance();


    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "GenerateProtobuf";
    }

    /**
     * 返回跟界面
     * @return
     */
    @Nullable
    @Override
    public JComponent createComponent() {
        configurationForm = new ConfigurationForm();
        return configurationForm.getRootPanel();
    }

    /**
     * 内容有没有改变
     * @return
     */
    @Override
    public boolean isModified() {
        return !configurationForm.jTextField().getText().equals(persistent.getConfig().getProtocPath());
    }

    /**
     * 点击apply 时 重新赋值
     * @throws ConfigurationException
     */
    @Override
    public void apply() throws ConfigurationException {
        persistent.getConfig().setProtocPath(configurationForm.jTextField().getText());
    }

    /**
     * 重置原来的值
     */
    @Override
    public void reset() {
        configurationForm.jTextField().setText(persistent.getConfig().getProtocPath());
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "Configuration for the Protoc path";
    }


}
