package com.axis.common;

import java.util.Date;

/**
 * 封装java.util.Date
 * @author axis
 *
 */
public class DateTime extends Date{
	private static final long serialVersionUID = -5395712593979185936L;
	
	/**
	 * 转换JDK date为 DateTime
	 * @param date JDK Date
	 * @return DateTime
	 */
	public static DateTime parse(Date date) {
		return new DateTime(date);
	}
	
	/**
	 * 当前时间
	 */
	public DateTime() {
		super();
	}
	
	/**
	 * 给定日期的构造
	 * @param date 日期
	 */
	public DateTime(Date date) {
		this(date.getTime());
	}
	
	/**
	 * 给定日期毫秒数的构造
	 * @param timeMillis 日期毫秒数
	 */
	public DateTime(long timeMillis) {
		super(timeMillis);
	}
	
	@Override
	public String toString() {
		return Dateutil.formatDateTime(this);
	}
	
	/**
	 * @return 输出精确到毫秒的标准日期形式
	 */
	public String toMsStr() {
		return Dateutil.format(this, Dateutil.NORM_DATETIME_MS_PATTERN);
	}
}
