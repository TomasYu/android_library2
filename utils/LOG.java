package com.zhangyue.iReader.tools;

import android.text.TextUtils;
import android.util.Log;

import com.zhangyue.iReader.app.PATH;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * 日志类，用于统一关闭日志，或文件输出等
 * 搜的时候，可以搜索当前的类名，因为我会打印出来MainActivity.java
 */
public class LOG {
	private static boolean IS_SHOW_LOG = true;

	public static final String DEFAULT_MESSAGE = "execute";
	private static final String TAG_DEFAULT = "LOG";
	private static final int STACK_TRACE_INDEX = 5;
	public static final String SUFFIX = ".java";
	public static final String NULL_TIPS = "Log with null object";
	public static final String PARAM = "Param";
	public static final String NULL = "null";
	public static final int V = 0x1;
	public static final int D = 0x2;
	public static final int I = 0x3;
	public static final int W = 0x4;
	public static final int E = 0x5;
	public static final int A = 0x6;

	public static void init(boolean isShowLog) {
		IS_SHOW_LOG = isShowLog;
	}

	public static void D(String tag, String msg) {
	}

	public static void e(String tag, Throwable msg) {
	}

	public static void e(String tag) {
	}

	public static void d(String tag, String msg) {
	}

	public static void d(String tag) {
		if (!IS_SHOW_LOG) {
			return;
		}
		Log.d("", tag);
	}

	/**
	 * 开启System.err日志监控
	 */
	public static void enableErrMonitor() {
	}

	public static void I(String tag, String msg) {
		if (!IS_SHOW_LOG) {
			return;
		}
		if (tag.equalsIgnoreCase("http")) {
			I2File(msg);
		}
		Log.i(tag, msg);
	}

	public static void E(String tag, String msg) {
		if (!IS_SHOW_LOG) {
			return;
		}
		Log.e(tag, msg);
	}

	public static void setSystemOutToFile() {
		if (!IS_SHOW_LOG) {
			return;
		}
		File logFile = new File(PATH.getCacheDir() + "log.txt");
		try {

			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			System.setOut(new PrintStream(new FileOutputStream(logFile, true)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void I2File(String msg) {
		if (!IS_SHOW_LOG) {
			return;
		}
		System.out.println(msg);
	}

	public static void E(String tag, String msg, Throwable tr) {
		if (!IS_SHOW_LOG) {
			return;
		}
		Log.e(tag, msg, tr);
	}

	public static void printStackTrace() {
		if (!IS_SHOW_LOG) {
			return;
		}
		Appendable out = System.err;
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		try {
			if (stack != null) {
				for (int i = 0; i < stack.length; i++) {
					out.append("");
					out.append("\tat ");
					out.append(stack[i].toString());
					out.append("\n");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}


	public static void i() {
		printLog(I, null, DEFAULT_MESSAGE);
	}

	public static void i(Object msg) {
		printLog(I, null, msg);
	}

	public static void i(String tag, Object... objects) {
		printLog(I, tag, objects);
	}

	private static void printLog(int type, String tagStr, Object... objects) {
		if (!IS_SHOW_LOG) {
			return;
		}
		String[] contents = wrapperContent(tagStr, objects);
		String tag = contents[0];
		String msg = contents[1];
		String headString = contents[2];
		switch (type) {
			case V:
			case D:
			case I:
			case W:
			case E:
			case A:
				printDefault(type, tag, headString + msg);
				break;
		}
	}

	private static String[] wrapperContent(String tagStr, Object... objects) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement targetElement = stackTrace[STACK_TRACE_INDEX];
		String className = targetElement.getClassName();
		String[] classNameInfo = className.split("\\.");
		if (classNameInfo.length > 0) {
			className = classNameInfo[classNameInfo.length - 1] + SUFFIX;
		}
		String methodName = targetElement.getMethodName();
		int lineNumber = targetElement.getLineNumber();
		if (lineNumber < 0) {
			lineNumber = 0;
		}
		String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
		String tag = (tagStr == null ? className : tagStr);
		if (TextUtils.isEmpty(tag)) {
			tag = TAG_DEFAULT;
		}
		String msg = (objects == null) ? NULL_TIPS : getObjectsString(objects);
		String headString = "[ (" + className + ":" + lineNumber + ")#" + methodNameShort + " ] ";

		return new String[]{tag, msg, headString};
	}

	private static String getObjectsString(Object... objects) {
		if (objects.length > 1) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("\n");
			for (int i = 0; i < objects.length; i++) {
				Object object = objects[i];
				if (object == null) {
					stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(NULL).append("\n");
				} else {
					stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(object.toString()).append("\n");
				}
			}
			return stringBuilder.toString();
		} else {
			Object object = objects[0];
			return object == null ? NULL : object.toString();
		}
	}

	public static void printDefault(int type, String tag, String msg) {
		int index = 0;
		int maxLength = 4000;
		int countOfSub = msg.length() / maxLength;
		if (countOfSub > 0) {
			for (int i = 0; i < countOfSub; i++) {
				String sub = msg.substring(index, index + maxLength);
				printSub(type, tag, sub);
				index += maxLength;
			}
			printSub(type, tag, msg.substring(index, msg.length()));
		} else {
			printSub(type, tag, msg);
		}
	}

	private static void printSub(int type, String tag, String sub) {
		switch (type) {
			case V:
				Log.v(tag, sub);
				break;
			case D:
				Log.d(tag, sub);
				break;
			case I:
				Log.i(tag, sub);
				break;
			case W:
				Log.w(tag, sub);
				break;
			case E:
				Log.e(tag, sub);
				break;
			case A:
				Log.wtf(tag, sub);
				break;
		}
	}
}
