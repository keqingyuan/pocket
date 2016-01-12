//(C) Copyright 2012 FEITIAN.LTD
/**
* 读取属性文件
* @author STEVEN
* @version V1  
*/
package com.axis.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.axis.common.log.LogWrapper;

public class ConfigTool {

	private final static LogWrapper log = Log.get();

	/**
	 * 通过文件名（相对路径）得到文件名（绝对路径）
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getConfigName(String fileName) {
		return ConfigTool.class.getClassLoader().getResource("").getPath() + fileName;
	}

	/**
	 * 通过文件名（相对路径）得到文件
	 * 
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getConfigInputStream(String fileName) throws FileNotFoundException {
		return new FileInputStream(getConfigName(fileName));
	}

	/**
	 * 通过配置文件名（相对路径）得到Properties
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static Properties getConfigProperties(String fileName) {
		Properties env = new Properties();
		InputStream is = null;
		try {
			is = getConfigInputStream(fileName);
			env.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {

				}
			}
		}
		return env;
	}

	/**
	 * 获得Properties文件对象
	 * @param propertiesName
	 * @return
	 */
	public static Properties getProperties(String propertiesName) {
		Properties env = new Properties();
		InputStream in = null;
		try {
			ClassLoader loader = ConfigTool.class.getClassLoader();
			in = loader.getResourceAsStream(propertiesName);
			env.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return env;
	}
}
