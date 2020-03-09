package com.zuofei.mybatis.utils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 
 * <p>
 * ClassName: DateUtil
 * </p>
 * <p>
 * Description: 日期帮助类
 * </p>
 * <p>
 * Author: 胡锐锋
 * </p>
 * <p>
 * Date: 2017年12月13日
 * </p>
 */
public class DateUtil {

	public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_MILLISECOND = "yyyyMMdd HH:mm:ss.SSS";

	public static final String SIMPLE_PATTERN = "yyyy-MM-dd";
	public static final String NO_LINE_THROUGH_PATTERN = "yyyyMMdd";
	public static final String HOUR_PATTERN = "yyyyMMdd HH";

	public static final Long DAYS_RADIX = 86400000l;
	public static final Long HOURS_RADIX = 3600000l;
	public static final Long MINUTES_RADIX = 60000l;

	private static final String TZ_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	private DateUtil() {
	}

	/**
	 * 获取指定日期所在周的第一天和最后一天
	 * 
	 * @param dataStr
	 *            日期格式：yyyy-MM-dd
	 * @return
	 * @throws ParseException
	 */
	public static String getFirstAndLastOfWeek(String dataStr) {
		String data1 = "1990-01-01";
		String data2 = "3000-01-01";

		try {
			Calendar cal = Calendar.getInstance();

			cal.setTime(new SimpleDateFormat(SIMPLE_PATTERN).parse(dataStr));

			int d = 0;
			if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
				d = -6;
			} else {
				d = 2 - cal.get(Calendar.DAY_OF_WEEK);
			}
			cal.add(Calendar.DAY_OF_WEEK, d);
			// 所在周开始日期
			data1 = new SimpleDateFormat(SIMPLE_PATTERN).format(cal.getTime());
			cal.add(Calendar.DAY_OF_WEEK, 6);
			// 所在周结束日期
			data2 = new SimpleDateFormat(SIMPLE_PATTERN).format(cal.getTime());
		} catch (ParseException e) {
			throw new IllegalStateException("获取指定日期所在周的第一天和最后一天错误,日期格式应该类似2020-01-19", e);
		}
		return data1 + "~" + data2;

	}

	/**
	 * 获取当天开始时间
	 *
	 * @return 当天开始时间
	 */
	public static Date getTodayStart() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 当前时间增加小时
	 *
	 * @param hour
	 *            小时数
	 * @return 增加后的时间
	 */
	public static Date addHour(int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + hour);
		return calendar.getTime();
	}

	/**
	 * 当前时间增加分钟
	 *
	 * @param minute
	 *            分钟数
	 * @return 增加后的时间
	 */
	public static Date addMinute(int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + minute);
		return calendar.getTime();
	}

	/**
	 * Date转String
	 *
	 * @param date
	 *            时间
	 * @return 时间字符串
	 */
	public static String dateToString(Date date) {
		return dateToString(date, SIMPLE_PATTERN);
	}

	public static String getNolineDateStr(Date date) {
		return dateToString(date, NO_LINE_THROUGH_PATTERN);
	}

	public static String dateToStringNoLine(Date date) {
		return dateToString(date, NO_LINE_THROUGH_PATTERN);
	}

	/**
	 * 
	 * @Title
	 *        <p>
	 *        Title: swagger格式的日期
	 *        </p>
	 * @Description
	 *              <p>
	 *              Description:
	 *              </p>
	 * @param date
	 * @return
	 */
	public static String dateToStringTZ(Date date) {
		return dateToString(date, TZ_PATTERN);
	}

	/**
	 * String转Date
	 *
	 * @param source
	 *            时间字符串
	 *            格式
	 * @return 时间
	 */
	public static Date stringToDateTZ(String source) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TZ_PATTERN);
		try {
			return simpleDateFormat.parse(source);
		} catch (ParseException e) {
			throw new IllegalStateException("日期格式错误,应该类似2020-01-19T05:21:18.978Z", e);
		}
	}

	/**
	 * Date转String
	 *
	 * @param date
	 *            时间
	 * @param pattern
	 *            格式
	 * @return 时间字符串
	 */
	public static String dateToString(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}

	/**
	 * 
	 * <p>
	 * Title: 根据pattern格式化date, 支持传入local本地化环境
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param date
	 *            时间
	 * @param pattern
	 *            格式
	 * @param locale
	 *            本地化环境
	 * @return 时间字符串
	 */
	public static String dateToString(Date date, String pattern, Locale locale) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
		return simpleDateFormat.format(date);
	}

	/**
	 * String转Date 默认格式为 yyyy-MM-dd
	 * 
	 * @param source
	 *            时间字符串
	 * @return 时间
	 */
	public static Date stringToDate(String source) {
		return stringToDate(source, SIMPLE_PATTERN);
	}

	public static Date stringToDateNoLine(String source) {
		return stringToDate(source, NO_LINE_THROUGH_PATTERN);
	}

	public static Date stringToDateMILLISECOND(String source) {
		return stringToDate(source, PATTERN_MILLISECOND);
	}

	/**
	 * String转Date
	 *
	 * @param source
	 *            时间字符串
	 * @param pattern
	 *            格式
	 * @return 时间
	 */
	public static Date stringToDate(String source, String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		try {
			return simpleDateFormat.parse(source);
		} catch (ParseException e) {
			throw new IllegalStateException("日期格式错误", e);
		}
	}

	/**
	 * 对日期进行推移
	 *
	 * @param dateType
	 *            日期类型 年，月，日，周 ，例如 Calendar.DATE
	 * @param count
	 *            推移因子 正数向后推移 负数向前推移
	 * @return 推移后的时间
	 */
	public static Date getDateDiff(Date date, int dateType, int count) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		switch (dateType) {
		case Calendar.DATE: // 日
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) + count);
			break;
		case Calendar.WEEK_OF_MONTH: // 周
			cal.set(Calendar.WEEK_OF_MONTH, cal.get(Calendar.WEEK_OF_MONTH) + count);
			break;
		case Calendar.MONTH: // 月
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + count);
			break;
		case Calendar.YEAR: // 年
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + count);
			break;
		default:
			cal.setTime(new Date());
			break;
		}
		return cal.getTime();

	}

	/**
	 * 
	 * <p>
	 * Title: 获取指定日期的年份
	 * </p>
	 * 
	 * @param date
	 *            日期
	 * @return 年
	 */
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);

	}

	/**
	 * 
	 * <p>
	 * Title: 获取指定日期的月份
	 * </p>
	 * 
	 * @param date
	 *            时间
	 * @return 月
	 */
	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;

	}

	/**
	 * 获取指定的日期的开始时间 如传入：2017-07-01 返回 2017-07-01 00:00:00
	 * 
	 * @param date
	 *            指定日期
	 * @return 指定的日期的开始时间
	 */
	public static Date getStartTimeOfDay(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();

	}

	/**
	 * 获取指定的小时的开始时间 如传入：2017-07-01 11 返回 2017-07-01 11:00:00
	 * 
	 * @param date
	 *            指定日期
	 * @return 指定的日期的开始时间
	 */
	public static Date getStartTimeOfHour(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();

	}

	/**
	 * 获取指定的日期的开始时间 如传入：2017-07-01 返回 2017-07-01 00:00:00
	 * 
	 * @param datestr
	 *            指定格式的日期字符串 “2017-07-01”
	 * @return 指定的日期的开始时间
	 */
	public static Date getStartTimeOfDay(String datestr) {

		Date date = stringToDate(datestr, SIMPLE_PATTERN);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();

	}

	/**
	 * 获取指定的日期的结束时间 如传入：2017-07-01 返回 2017-07-01 23:59:59
	 * 
	 * @param date
	 *            指定日期
	 * @return 指定的日期的结束时间
	 */
	public static Date getEndTimeOfDay(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();

	}

	/**
	 * 获取指定的小时的结束时间 如传入：2017-07-01 12 返回 2017-07-01 12:59:59
	 * 
	 * @param date
	 *            指定日期
	 * @return 指定的日期的结束时间
	 */
	public static Date getEndTimeOfHour(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();

	}

	/**
	 * 获取指定的日期的结束时间 如传入：2017-07-01 返回 2017-07-01 23:59:59
	 * 
	 * @param dateStr
	 *            指定格式的日期字符串 “2017-07-01”
	 * @return 指定的日期的结束时间
	 */
	public static Date getEndTimeOfDay(String dateStr) {

		Date date = stringToDate(dateStr, SIMPLE_PATTERN);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();

	}

	/**
	 * 给指定的日期增加或者减少指定的时间
	 *
	 * @param date
	 *            指定的日期
	 * @param num
	 *            时长（正的为增加，负为减少）
	 * @param unit
	 *            时间单位,参考 {@link ChronoUnit}
	 * @return 修改后的日期
	 */
	public static Date operationDate(Date date, int num, ChronoUnit unit) {
		if (date != null) {
			Instant instant = date.toInstant();
			LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			LocalDateTime dateTime = localDateTime.plus(num, unit);
			return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
		}
		return null;
	}

	/**
	 * 
	 * @Description: 在日期上增加数个整月
	 * @param date
	 *            需要增加的日期
	 * @param n
	 *            要增加的月数
	 * @return 在日期上增加数个整月后的日期
	 */
	public static Date addMonth(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		return cal.getTime();
	}

	/**
	 * 
	 * <p>
	 * Title: 在日期上增加天数
	 * </p>
	 * 
	 * @param date
	 *            日期
	 * @param n
	 *            指定天数
	 * @return 之后的日期
	 */
	public static Date addDay(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, n);
		return cal.getTime();
	}

	public static Date subDay(Date date, int n) {
		n = n * (-1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, n);
		return cal.getTime();
	}

	/**
	 * 
	 * <p>
	 * Title: 在日期上增加分钟
	 * </p>
	 * 
	 * @param date
	 *            日期
	 * @param n
	 *            指定分钟
	 * @return 之后的时间
	 */
	public static Date addMinute(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, n);
		return cal.getTime();
	}

	/**
	 * 
	 * <p>
	 * Title: 获取当天，没有时分秒
	 * </p>
	 * 
	 * @return 当天日期
	 */
	public static Date getToday() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * @Description: 获取相差天数
	 * @param date
	 *            日期
	 * @param otherDate
	 *            另一个日期
	 * @return int 相差天数
	 */
	public static int getIntervalDays(Date date, Date otherDate) {
		long time = Math.abs(date.getTime() - otherDate.getTime());
		return (int) time / (24 * 60 * 60 * 1000);
	}

	/**
	 * 
	 * @Title
	 *        <p>
	 *        Title: 获取相差小时数
	 *        </p>
	 * @param date
	 *            日期
	 * @param otherDate
	 *            另一个日期
	 * @return int相差小时数
	 */
	public static int getDiffHours(Date date, Date otherDate) {
		long time = Math.abs(date.getTime() - otherDate.getTime());
		String str = String.valueOf(time / (60 * 60 * 1000));
		return Integer.parseInt(str);
	}

	/**
	 * @Description: 日期相差分钟数
	 * @param date
	 *            日期
	 * @param otherDate
	 *            另一个日期
	 * @return long 相差分钟数
	 */
	public static Long getIntervalMinutes(Date date, Date otherDate) {
		long time = Math.abs(date.getTime() - otherDate.getTime());
		return time == 0 ? 0 : time / (60 * 1000);
	}

	/**
	 * 
	 * @Title
	 *        <p>
	 *        Title: 获取相差秒数
	 *        </p>
	 * @Description
	 *              <p>
	 *              Description:
	 *              </p>
	 * @param date
	 * @param otherDate
	 * @return
	 */
	public static Long getDiffSeconds(Date date, Date otherDate) {
		long time = Math.abs(date.getTime() - otherDate.getTime());
		String str = String.valueOf(time / 1000);
		return Long.parseLong(str);
	}

	/**
	 * 
	 * <p>
	 * Title: 获取日期相差时间(精确到分钟)
	 * </p>
	 * <p>
	 * Description: 获取日期相差时间(精确到分钟)
	 * </p>
	 * 
	 * @param endTime
	 *            结束时间
	 * @param startTime
	 *            起始时间
	 * @return
	 */
	public static String getDiffTime(Date endTime, Date startTime) {
		long diff = Math.abs(endTime.getTime() - startTime.getTime());
		long days = diff / DAYS_RADIX;
		long hours = (diff - days * DAYS_RADIX) / HOURS_RADIX;
		long minutes = (diff - days * DAYS_RADIX - hours * HOURS_RADIX) / MINUTES_RADIX;
		return new StringBuilder().append(days).append("天").append(hours).append("小时").append(minutes).append("分钟")
				.toString();
	}

	/**
	 * 
	 * @Title
	 *        <p>
	 *        Title: 根据yyyy-MM-dd HH:mm:ss格式返回当前日期
	 *        </p>
	 * @Description
	 *              <p>
	 *              Description: 根据yyyy-MM-dd HH:mm:ss格式返回当前日期
	 *              </p>
	 * @return String 时间字符串
	 */
	public static String getNow() {
		return dateToString(new Date(), PATTERN);
	}

	public static String getNowMillisecond() {
		return dateToString(new Date(), PATTERN_MILLISECOND);
	}

	/**
	 * 获取今日的日期
	 * 
	 * @return yyyyMMdd
	 */
	public static String getTodayStr() {
		return dateToString(new Date(), NO_LINE_THROUGH_PATTERN);
	}

	/**
	 * 
	 * @return 获取现在的Date
	 */
	public static Date getNowDate() {
		return new Date();
	}

	/**
	 * 获取昨天的此刻
	 * 
	 * @return
	 */
	public static Date getNowOfYesterday() {
		return subDay(getNowDate(), 1);
	}

	/**
	 * 
	 * @Title
	 *        <p>
	 *        Title: 根据yyyy-MM-dd HH:mm:ss格式返回当前日期
	 *        </p>
	 * @Description
	 *              <p>
	 *              Description: 根据yyyy-MM-dd HH:mm:ss SSS格式返回当前日期
	 *              </p>
	 * @return String 时间字符串
	 */
	public static String getNow1() {
		return dateToString(new Date(), "yyyy-MM-dd HH:mm:ss SSS");
	}

	/**
	 * @Description: 根据用户格式返回当前日期
	 * @param format
	 *            格式
	 * @return String 时间字符串
	 */
	public static String getNow(String format) {
		return dateToString(new Date(), format);
	}

	/**
	 * 
	 * <p>
	 * Title: 生成当前时间的UTC时间
	 * </p>
	 * <p>
	 * Description: 单位 毫秒
	 * </p>
	 * 
	 * @return UTC时间
	 */
	public static long buildUTC() {
		return System.currentTimeMillis();
	}

	/**
	 * 
	 * <p>
	 * Title: 生成当前时间几天之后的UTC时间
	 * </p>
	 * <p>
	 * Description: 单位 毫秒
	 * </p>
	 * 
	 * @param days
	 *            单位：天
	 * @return UTC时间
	 */
	public static long buildUTC(int days) {
		return DateUtil.operationDate(new Date(), days, ChronoUnit.DAYS).getTime();
	}

	/**
	 * 
	 * @Title
	 *        <p>
	 *        Title: 判断时间现在有没有过期
	 *        </p>
	 * @Description
	 *              <p>
	 *              Description: 指定 现在的时间<指定时间 就没过期false，否则就过期true
	 *              </p>
	 * @param utcTime
	 *            毫秒
	 * @return
	 */
	public static boolean isUtcTimeOverDue(long utcTime) {
		return new Date(utcTime).before(new Date());
	}

	/**
	 * 
	 * @Title
	 *        <p>
	 *        Title: 获取指定时间距离现在还有多少秒
	 *        </p>
	 * @Description
	 *              <p>
	 *              Description: 如果指定时间已经过了，返回差值为0
	 *              </p>
	 * @param utcTime
	 *            指定UTC时间 单位：秒
	 * @return 相差的秒
	 */
	public static long diffSeconds(long utcTime) {
		long diff = new BigDecimal(utcTime)
				.subtract(new BigDecimal(System.currentTimeMillis()).divide(new BigDecimal(1000), 0)).longValue();
		return diff > 0 ? diff : 0;
	}

	/**
	 * 
	 * @Title
	 *        <p>
	 *        Title: 获取当前UTC时间的秒数
	 *        </p>
	 * @Description
	 *              <p>
	 *              Description:
	 *              </p>
	 * @return 获取现在时间的时间戳（秒） long类型
	 */
	public static long getNowSecondLong() {
		String timestamp = String.valueOf(System.currentTimeMillis());
		return Long.valueOf(timestamp.substring(0, timestamp.length() - 3));
	}

	/**
	 * 
	 * @Title
	 *        <p>
	 *        Title: 获取当前UTC时间的秒数
	 *        </p>
	 * @Description
	 *              <p>
	 *              Description:
	 *              </p>
	 * @return 获取现在时间的时间戳（秒） String类型
	 */
	public static String getNowSecondStr() {
		String timestamp = String.valueOf(System.currentTimeMillis());
		return timestamp.substring(0, timestamp.length() - 3);
	}

	/**
	 * 
	 * @Title
	 *        <p>
	 *        Title: 获取精确到秒的时间戳
	 *        </p>
	 * @Description
	 *              <p>
	 *              Description: 获取精确到秒的时间戳
	 *              </p>
	 * @param date
	 *            目标时间
	 * @return 指定时间的时间戳 (单位 秒)
	 */
	public static long getSecondTimestamp(Date date) {
		if (null == date) {
			return 0;
		}
		String timestamp = String.valueOf(date.getTime());
		int length = timestamp.length();
		if (length > 3) {
			return Long.valueOf(timestamp.substring(0, length - 3));
		} else {
			return 0;
		}
	}

	/**
	 * 
	 * @Title
	 *        <p>
	 *        Title: 初始化开始时间参数
	 *        </p>
	 * @Description
	 *              <p>
	 *              Description: 默认是1970-01-01 01:00:00.0
	 *              </p>
	 * @param bgnTime
	 *            开始时间
	 * @return Date bgnTime = DateUtil.initBgnTime(bgnTime);
	 */
	public static Date initBgnTime(Date bgnTime) {
		bgnTime = (bgnTime == null) ? DateUtil.getStartTimeOfDay("1970-01-01") : bgnTime;
		return bgnTime;
	}

	/**
	 * 
	 * @Title
	 *        <p>
	 *        Title: 初始化结束时间参数
	 *        </p>
	 * @Description
	 *              <p>
	 *              Description: 默认是3000-01-01 23:59:59.999
	 *              </p>
	 *            结束时间
	 * @return Date bgnTime = DateUtil.initEndTime(endTime);
	 */
	public static Date initEndTime(Date endTime) {
		endTime = (endTime == null) ? DateUtil.getEndTimeOfDay("3000-01-01") : endTime;
		return endTime;
	}

	/**
	 * 将Date类转换为XMLGregorianCalendar
	 * 
	 * @param date
	 *            Date类型日期
	 * @return
	 */
	public static XMLGregorianCalendar dateToXmlDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		DatatypeFactory dtf = null;
		try {
			dtf = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
		}
		XMLGregorianCalendar dateType = dtf.newXMLGregorianCalendar();
		dateType.setYear(cal.get(Calendar.YEAR));
		// 由于Calendar.MONTH取值范围为0~11,需要加1
		dateType.setMonth(cal.get(Calendar.MONTH) + 1);
		dateType.setDay(cal.get(Calendar.DAY_OF_MONTH));
		dateType.setHour(cal.get(Calendar.HOUR_OF_DAY));
		dateType.setMinute(cal.get(Calendar.MINUTE));
		dateType.setSecond(cal.get(Calendar.SECOND));
		return dateType;
	}

	/**
	 * 将XMLGregorianCalendar转换为Date
	 * 
	 * @param cal
	 *            XmlDate类型日期
	 * @return
	 */
	public static Date xmlDate2Date(XMLGregorianCalendar cal) {
		return cal.toGregorianCalendar().getTime();
	}

	/**
	 * 取两个日期之间的所有日期
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<Date> getBetweenDates(Date start, Date end) {
		List<Date> result = new ArrayList<Date>();
		if (start.after(end)) {
			Date t = start;
			start = end;
			end = t;
		} else if (start.getDay() == end.getDay()) {
			result.add(end);
			return result;
		}

		result.add(start);
		Calendar tempStart = Calendar.getInstance();
		tempStart.setTime(start);
		tempStart.add(Calendar.DAY_OF_YEAR, 1);

		Calendar tempEnd = Calendar.getInstance();
		tempEnd.setTime(end);
		while (tempStart.before(tempEnd)) {
			result.add(tempStart.getTime());
			tempStart.add(Calendar.DAY_OF_YEAR, 1);
		}
		result.add(end);
		return result;
	}

	/**
	 * 秒长转换成日时分秒
	 * 
	 * @param second
	 * @return
	 */
	public static String secondToTime(long second) {
		long days = second / 86400;// 转换天数
		String daystr = (days < 10) ? ("0" + days) : String.valueOf(days);
		second = second % 86400;// 剩余秒数
		long hours = second / 3600;// 转换小时数
		String hourstr = (hours < 10) ? ("0" + hours) : String.valueOf(hours);
		second = second % 3600;// 剩余秒数
		long minutes = second / 60;// 转换分钟
		String minutestr = (minutes < 10) ? ("0" + minutes) : String.valueOf(minutes);
		second = second % 60;// 剩余秒数
		String secondstr = (second < 10) ? ("0" + second) : String.valueOf(second);
		if (days > 0) {
			return String.join(":", daystr, hourstr, minutestr, secondstr);
		} else {
			return String.join(":", hourstr, minutestr, secondstr);
		}
	}

	/**
	 * 秒长转换成分钟
	 * 
	 * @param second
	 * @return minutes
	 */
	public static String secondToMinutes(long second) {
		long minutes = second / 60;// 转换分钟
		return String.valueOf(minutes);
	}

	/**
	 * 获取几天之后的指定秒数
	 * 
	 * @param days
	 *            指定天数
	 * @param upTime
	 *            提前秒数 （默认提取10秒）
	 * @return
	 */
	public static Long getSecondsAfterDays(int days, Integer upTime) {
		String timestamp = String.valueOf(addDay(new Date(), days).getTime());
		if (upTime == null) {
			upTime = 10;
		}
		Long expire = Long.valueOf(timestamp.substring(0, timestamp.length() - 3)) - upTime;
		return expire;
	}

}
