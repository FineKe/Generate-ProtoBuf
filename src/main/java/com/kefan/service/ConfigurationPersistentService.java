package com.kefan.service;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.kefan.bean.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @note:
 * @author: fine
 * @date: 2018/10/6 3:59 PM
 */
@State(name = "GPConfig", storages = {@Storage(file = "$APP_CONFIG$/GP.xml")})
public class ConfigurationPersistentService implements PersistentStateComponent<Config> {


    private Config config = new Config();

    /**
     * 单例模式
     * @return
     */
    public static ConfigurationPersistentService getInstance() {
        return ServiceManager.getService(ConfigurationPersistentService.class);
    }


    /**
     * 存入属性时 这个方法会调用
     * @return
     */
    @Nullable
    @Override
    public Config getState() {
        return config;
    }

    /**
     * 获取属性时调用
     * @param state
     */
    @Override
    public void loadState(@NotNull Config state) {
        XmlSerializerUtil.copyBean(state,config);
    }

    public Config getConfig() {
        return config;
    }
}
