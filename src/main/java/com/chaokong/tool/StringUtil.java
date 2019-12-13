/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.chaokong.tool;

import java.math.BigDecimal;

/**
 * @project gps-bubiao
 * @version 0.1
 * @author zhengxin
 * @created at 2006-7-4 20:57:58
 * @purpose: provide common mehtod for string operations;
 * @edited by zhengxin
 */
public class StringUtil {

	

	/**
	 * 判断是否是有效的字符串，空串为无效串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isValidStr(String str) {
		return str != null && str.trim().length() > 0;
	}
	
	/**
	 * 将字符串的换行替换成HTML的换行符号
	 * @param str
	 * @return
	 */
	public static String formatHtmlString(String str){
		
		return str.replaceAll("\r\n", "<BR/>");
	}
	/**
	 * 获取字符串的字节长度
	 * @param str
	 * @return
	 */
	public static int getByteLength(String str) {
		try {
			return str.getBytes("GBK").length;
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
	/**
	 * 将数组中的字符，连成以逗号分隔的形式
	 * 如果 obj 为null，并不抛出异常，直接返回为空
	 * @param obj
	 * @return
	 */
//	public static String joinStr(Object[] obj){
//		if (obj == null)
//			return null;
//		
//		StringBuffer buffer = new StringBuffer();
//		
//		if (obj.length > 0);
//		  buffer.append(obj[0]);
//		
//		for(int m = 1; m < obj.length; m++){
//			buffer.append(StringConstants.COMMA).append(obj[m]);
//		}
//		
//		return buffer.toString();
//	}

	/**
	 * 根据字符串转换为布尔值ֵ
	 * 
	 * @param str
	 * @return
	 */
//	public static boolean getBooleanValue(String str) {
//		return isValidStr(str) ? str.toLowerCase().trim().equals(
//				StringConstants.TRUE) : false;
//	}

	public static int getIntValue(String str, int defaultValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static BigDecimal getBigDecimal(String param) {
		if (StringUtil.isValidStr(param)) {
			try {
				return new BigDecimal(param);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		return new BigDecimal(-1);
	}
	
	public static String describe(Object[] values){
		StringBuffer buff = new StringBuffer();
		
		for(int m = 0; m < values.length; m++){
			buff.append(values[m]).append(", ");
		}
		
		return buff.toString();
	}
	
	public static long getStrTolong(String value){
		long result=0;
		if (value==null||(value!=null && value.equals(""))) return result;
	
		try{
			result=Long.parseLong(value);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public static double getStrTodouble(String value){
		double result=0;
		if (value==null ||(value!=null && value.equals(""))) return result;
		try{
			result=Double.parseDouble(value);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;		
	}	
	
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.equals("") || str.toLowerCase().equals("null");
	}

	public static String leftPad(String str, int size, char padChar) {
		if (str == null) {
			return null;
		}
		int pads = size - str.length();
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		return padding(pads, padChar).concat(str);
	}

	private static String padding(int repeat, char padChar)
			throws IndexOutOfBoundsException {
		if (repeat < 0) {
			throw new IndexOutOfBoundsException(
					"Cannot pad a negative amount: " + repeat);
		}
		final char[] buf = new char[repeat];
		for (int i = 0; i < buf.length; i++) {
			buf[i] = padChar;
		}
		return new String(buf);
	}

	public static String leftPad(String str, int size, String padStr) {
		if (str == null) {
			return null;
		}
		if (isNullOrEmpty(padStr)) {
			padStr = " ";
		}
		int padLen = padStr.length();
		int strLen = str.length();
		int pads = size - strLen;
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (padLen == 1) {
			return leftPad(str, size, padStr.charAt(0));
		}

		if (pads == padLen) {
			return padStr.concat(str);
		} else if (pads < padLen) {
			return padStr.substring(0, pads).concat(str);
		} else {
			char[] padding = new char[pads];
			char[] padChars = padStr.toCharArray();
			for (int i = 0; i < pads; i++) {
				padding[i] = padChars[i % padLen];
			}
			return new String(padding).concat(str);
		}
	}

	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'Join' (2 parameter
	// version).
	// ------------------------------------------------------------------------------------
	public static String join(String separator, String[] stringarray) {
		if (stringarray == null)
			return null;
		else
			return join(separator, stringarray, 0, stringarray.length);
	}

	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'Join' (4 parameter
	// version).
	// ------------------------------------------------------------------------------------
	public static String join(String separator, String[] stringarray,
			int startindex, int count) {
		String result = "";

		if (stringarray == null)
			return null;

		for (int index = startindex; index < stringarray.length
				&& index - startindex < count; index++) {
			if (separator != null && index > startindex)
				result += separator;

			if (stringarray[index] != null)
				result += stringarray[index];
		}

		return result;
	}

	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'TrimEnd'.
	// ------------------------------------------------------------------------------------
	public static String trimEnd(String string, Character... charsToTrim) {
		if (string == null || charsToTrim == null)
			return string;

		int lengthToKeep = string.length();
		for (int index = string.length() - 1; index >= 0; index--) {
			boolean removeChar = false;
			if (charsToTrim.length == 0) {
				if (Character.isWhitespace(string.charAt(index))) {
					lengthToKeep = index;
					removeChar = true;
				}
			} else {
				for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++) {
					if (string.charAt(index) == charsToTrim[trimCharIndex]) {
						lengthToKeep = index;
						removeChar = true;
						break;
					}
				}
			}
			if (!removeChar)
				break;
		}
		return string.substring(0, lengthToKeep);
	}

	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'TrimStart'.
	// ------------------------------------------------------------------------------------
	public static String trimStart(String string, Character... charsToTrim) {
		if (string == null || charsToTrim == null)
			return string;

		int startingIndex = 0;
		for (int index = 0; index < string.length(); index++) {
			boolean removeChar = false;
			if (charsToTrim.length == 0) {
				if (Character.isWhitespace(string.charAt(index))) {
					startingIndex = index + 1;
					removeChar = true;
				}
			} else {
				for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++) {
					if (string.charAt(index) == charsToTrim[trimCharIndex]) {
						startingIndex = index + 1;
						removeChar = true;
						break;
					}
				}
			}
			if (!removeChar)
				break;
		}
		return string.substring(startingIndex);
	}

	// ------------------------------------------------------------------------------------
	// This method replaces the .NET static string method 'Trim' when arguments
	// are used.
	// ------------------------------------------------------------------------------------
	public static String trim(String string, Character... charsToTrim) {
		return trimEnd(trimStart(string, charsToTrim), charsToTrim);
	}

	// ------------------------------------------------------------------------------------
	// This method is used for string equality comparisons when the option
	// 'Use helper 'stringsEqual' method to handle null strings' is selected
	// (The Java String 'equals' method can't be called on a null instance).
	// ------------------------------------------------------------------------------------
	public static boolean stringsEqual(String s1, String s2) {
		if (s1 == null && s2 == null)
			return true;
		else
			return s1 != null && s1.equals(s2);
	}

}
