/**
 * 
 */
package com.morefun.XSanGo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * 文件操作类
 * 
 * @author linyun.su
 *
 */
public class FileUtil {
	/**
	 * 保存文件
	 * 
	 * @param fileName
	 *            文件名完整路径
	 * @param data
	 *            文件二进制内容
	 */
	public static void saveFile(String fileName, byte[] data) {
		File f = new File(fileName);
		File parent = f.getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}

		try {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(data);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			LogManager.error(e);
		}
	}

	/**
	 * 从一个文件地址中读取序列化的对象
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object readObjectFromFile(File f) throws IOException,
			ClassNotFoundException {
		FileInputStream fis = new FileInputStream(f);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Object obj = ois.readObject();
		ois.close();
		fis.close();

		return obj;
	}

	/**
	 * 递归删除指定文件或目录
	 * 
	 * @param file
	 */
	public static void delete(File file) {
		if (file == null || !file.exists()) { // 判断文件是否存在
			return;
		}

		if (file.isFile()) { // 判断是否是文件
			file.delete();
		} else if (file.isDirectory()) { // 否则如果它是一个目录
			File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
			for (File subFile : files) {// 遍历目录下所有的文件
				delete(subFile);// 递归删除
			}
		}

		file.delete();
	}
}
