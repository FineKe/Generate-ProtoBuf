package com.kefan;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

/**
 * @author fine
 * @date 2018/7/11 下午7:15
 */
public class HelloAction extends AnAction {
	@Override
	public void actionPerformed(AnActionEvent e) {
		Messages.showInfoMessage("ssss","test" );
	}
}
