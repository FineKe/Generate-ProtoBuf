package com.kefan;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;

/**
 * @author fine
 * @date 2018/7/9 下午5:10
 */
public class GenerateProtoBufAction extends AnAction {


	private static final String EXTENSION_NAME = "proto";

	public GenerateProtoBufAction() {
		super("Generate ProtoBuf");
	}

	@Override
	public void actionPerformed(AnActionEvent e) {

		Project project=e.getProject();


		String extension = getFileExtension(e.getDataContext());

		if (extension!=null&&EXTENSION_NAME.equals(extension)) {


			VirtualFile file = PlatformDataKeys.VIRTUAL_FILE.getData(e.getDataContext());


			if (file == null) {

				Messages.showErrorDialog("The file is not found", "Error");

				return;
			}

			String command ="protoc --proto_path=" + file.getParent().getPath() + " " + "--go_out=plugins=grpc:"+file.getParent().getPath()+" "+ file.getPath();


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

	@Override
	public void update(AnActionEvent e) {

		String extension = getFileExtension(e.getDataContext());
		e.getPresentation().setVisible(extension!=null&&extension.equals(EXTENSION_NAME));

	}

	public static String getFileExtension(DataContext dataContext) {
		VirtualFile virtualFile =PlatformDataKeys.VIRTUAL_FILE.getData(dataContext);
		return virtualFile == null?null:virtualFile.getExtension();
	}
}
