package com.kefan.ui;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.kefan.service.ConfigurationPersistentService;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GenerateConfigDialog extends JDialog {

	private static final String EXTENSION_NAME = "proto";

	private ConfigurationPersistentService persistentService = ConfigurationPersistentService.getInstance();

	private AnActionEvent anActionEvent;

	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JComboBox pluginComboBox;
	private JComboBox languageComboBox;
	private JLabel pluginSelected;
	private JLabel languageSelected;

	private Map<String,String> languageMap=new HashMap<>();
	private Map<String,String> pluginMap=new HashMap<>();

	public GenerateConfigDialog(AnActionEvent anActionEvent) {

		this.anActionEvent=anActionEvent;

		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

		initView();
		//初始胡数据
		initData();


		//选择框设置数据
		languageMap.forEach( (key,value)->languageComboBox.addItem(key));
		pluginMap.forEach((key,value)->pluginComboBox.addItem(key));


		//添加监听事件
		languageComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String s = (String) e.getItem();
				languageSelected.setText(s);
			}
		});

		pluginComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String s = (String) e.getItem();
				pluginSelected.setText(s);
			}
		});

		//设置默认选中
		languageSelected.setText(String.valueOf(languageComboBox.getSelectedItem()));
		pluginSelected.setText(String.valueOf(pluginComboBox.getSelectedItem()));

		buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});

		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});

		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

	}

	private void initView() {
		this.setTitle("Configuration");
	}

	private void onOK() {
		// add your code here
		//逻辑处理部分
		process();
		dispose();
	}

	private void onCancel() {
		// add your code here if necessary
		dispose();
	}

	private void initData() {

		languageMap.put("C++", "cpp_out");
		languageMap.put("C#", "csharp_out");
		languageMap.put("Go", "go_out");
		languageMap.put("Java", "java_out");
		languageMap.put("Pyhton", "python_out");
		languageMap.put("Objective C", "objc_out");
		languageMap.put("Javascript", "js_out");
		languageMap.put("Php", "php_out");

		pluginMap.put("None", "none");
		pluginMap.put("GRPC", "grpc");
	}

	private void process() {

		Project project = anActionEvent.getProject();

		String extension = getFileExtension(anActionEvent.getDataContext());

		if (extension!=null&&EXTENSION_NAME.equals(extension)) {


			VirtualFile file = PlatformDataKeys.VIRTUAL_FILE.getData(anActionEvent.getDataContext());


			if (file == null) {

				Messages.showErrorDialog("The file is not found", "Error");

				return;
			}



			//命令生成处理

			String command ="";

			//优先使用配置的protoc 路径
			if (persistentService.getConfig().getProtocPath() != null&&persistentService.getConfig().getProtocPath().endsWith("protoc")) {
				command += persistentService.getConfig().getProtocPath();
			} else {
				//使用 系统变量
				command += "protoc";
			}

			 command +=" --proto_path=" + file.getParent().getPath() + " ";

			if (!pluginSelected.getText().equals("None")) {

				command += "--" + languageMap.get(languageSelected.getText()) + "=plugins=" + pluginMap.get(pluginSelected.getText()) + ":" + file.getParent().getPath() + " " + file.getPath();

			} else {
				command += "--" + languageMap.get(languageSelected.getText()) + "=:" + file.getParent().getPath() + " " + file.getPath();
			}



			//执行命令

			try {


				Process process = Runtime.getRuntime().exec(command);


				int exitValue = process.waitFor();

				if (exitValue == 0) {

					Messages.showInfoMessage(project, "success", "Generate");

					System.out.println("generate success");

					//刷新目录
					file.getParent().refresh(false, false);

				} else {

					Messages.showErrorDialog(project, "error exit " + exitValue, "Generate Failed");

				}

			} catch (IOException e1) {
				e1.printStackTrace();

				Messages.showErrorDialog(e1.getMessage()+" "+command, "exception");

			} catch (InterruptedException e1) {
				e1.printStackTrace();
				Messages.showErrorDialog(e1.getMessage()+" "+command, "exception");
			}

		} else {
			Messages.showErrorDialog(project, "file not found", "Select Failed");
		}
	}


	private  String getFileExtension(DataContext dataContext) {
		VirtualFile virtualFile =PlatformDataKeys.VIRTUAL_FILE.getData(dataContext);
		return virtualFile == null?null:virtualFile.getExtension();
	}
}
