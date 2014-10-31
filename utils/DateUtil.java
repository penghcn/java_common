
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.joda.time.DateTime;

public class DateUtil {
	private static final Map<String,Integer>monthQuarterMap = new HashMap<String, Integer>();
	public static final String YEAR_FORMAT = "yyyy";
	public static final String MONTH_FORMAT = "yyyyMM";
	public static final String DAY_FORMAT = "yyyyMMdd";
	public static final String LOCALE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private static final int NOON = 12;
	
	static{
		monthQuarterMap.put("01", 1);
		monthQuarterMap.put("02", 1);
		monthQuarterMap.put("03", 1);
		monthQuarterMap.put("04", 2);
		monthQuarterMap.put("05", 2);
		monthQuarterMap.put("06", 2);
		monthQuarterMap.put("07", 3);
		monthQuarterMap.put("08", 3);
		monthQuarterMap.put("09", 3);
		monthQuarterMap.put("10", 4);
		monthQuarterMap.put("11", 4);
		monthQuarterMap.put("12", 4);
	}
	
	private static Calendar _getCalendarInstance(){
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		return cal;
	}

	
	public static String toLocaleTime() {
		Calendar cal = _getCalendarInstance();
		return new SimpleDateFormat(LOCALE_FORMAT).format(cal.getTime());
	}
	
	public static String getCurrDay() {
		Calendar cal = _getCalendarInstance();
		return new SimpleDateFormat(DAY_FORMAT).format(cal.getTime());
	}
	
	/**
	 * 当月 201407
	 * @return
	 */
	public static String getCurrMonth() {
		Calendar cal = _getCalendarInstance();
		return new SimpleDateFormat(MONTH_FORMAT).format(cal.getTime());
	}
	/**
	 * 上一月 201406
	 * @return
	 */
	public static String getLastMonth() {
		return getLastMonth(MONTH_FORMAT);
	}
	public static String getLastMonthYear() {
		return getLastMonth(YEAR_FORMAT);
	}
	/**
	 * 上一月 
	 * @param format
	 * @return
	 */
	public static String getLastMonth(String format) {
		Calendar cal = _getCalendarInstance();
		cal.add(Calendar.MONTH, -1);
		return new SimpleDateFormat(format).format(cal.getTime());
	}
	
	/**
	 * 获取某月的下一月
	 * @param currMonth
	 * @param format
	 * @return
	 */
	private static String getNextMonth(String currMonth,String format, boolean isNext) {
		Calendar cal = _getCalendarInstance();
        try {
			Date d = new SimpleDateFormat(format).parse(currMonth);
			cal.setTime(d);
		} catch (ParseException e) {
			// 
			e.printStackTrace();
		}
		cal.add(Calendar.MONTH, isNext ? 1 : -1);
		return new SimpleDateFormat(format).format(cal.getTime());
	}
	public static String getNextMonth(String currMonth) {
		return getNextMonth(currMonth,MONTH_FORMAT,true);
	}
	public static String getNextMonth() {
		//return getNextMonth(getCurrMonth(),MONTH_FORMAT);
		Calendar cal = _getCalendarInstance();
		cal.add(Calendar.MONTH, 1);
		return new SimpleDateFormat(MONTH_FORMAT).format(cal.getTime());
	}
	public static String getPrevMonth(String currMonth) {
		return getNextMonth(currMonth,MONTH_FORMAT,false);
	}
	public static String getPrevMonth() {
		return getLastMonth();
	}
	
	public static String getNextDay(String currDay,String format,int day){
		Calendar cal = _getCalendarInstance();
        try {
			Date d = new SimpleDateFormat(format).parse(currDay);
			cal.setTime(d);
		} catch (ParseException e) {
			// 
			e.printStackTrace();
		}
		cal.add(Calendar.DATE, day);
		return new SimpleDateFormat(format).format(cal.getTime());
	}
	
	public static String getNextDay(String currDay,int day){
		return getNextDay(currDay, DAY_FORMAT, day);
	}
	
	public static String getNextDay(int day){
		//return getNextDay(getCurrDay(), day);
		return new DateTime().plusDays(day).toString(DAY_FORMAT);
	}
	
	private static String getNextMonthDay(String currDay,String format, boolean isNext) {
		Calendar cal = _getCalendarInstance();
        try {
			Date d = new SimpleDateFormat(format).parse(currDay);
			cal.setTime(d);
		} catch (ParseException e) {
			// 
			e.printStackTrace();
		}
		cal.add(Calendar.MONTH, isNext ? 1 : -1);
		return new SimpleDateFormat(format).format(cal.getTime());
	}
	
	public static String getNextMonthDay(String currDay,String format){
		return getNextMonthDay(currDay, format, true);
	}
	public static String getNextMonthDay(String currDay){
		return getNextMonthDay(currDay, DAY_FORMAT);
	}
	public static String getNextMonthDay(){
		//return getNextMonthDay(getCurrDay());
		return new DateTime().plusMonths(1).toString(DAY_FORMAT);
	}
	public static String getPrevMonthDay(String currDay,String format){
		return getNextMonthDay(currDay, format, false);
	}
	public static String getPrevMonthDay(String currDay){
		return getNextMonthDay(currDay, DAY_FORMAT);
	}
	public static String getPrevMonthDay(){
		return getNextMonthDay(getCurrDay());
	}
	/**
	 * Date -> String 
	 * @param date
	 * @return
	 */
	public static String Date2Str(Date date) {
		SimpleDateFormat f = new SimpleDateFormat(DAY_FORMAT);
		return f.format(date);
	}
	/**
	 * Date -> String 
	 * @param date
	 * @return
	 */
	public static String Date2Str(Date date,String fmt) {
		SimpleDateFormat f = new SimpleDateFormat(fmt);
		return f.format(date);
	}
	/**
	 * String -> Date
	 * @param ds
	 * @return
	 */
	public static Date str2Date(String ds) {
		return str2Date(ds,DAY_FORMAT);
	}
	/**
	 * 指定格式，String -> Date
	 * @param ds
	 * @param format
	 * @return
	 */
	public static Date str2Date(String ds,String format) {
		Calendar cal = new GregorianCalendar();
		SimpleDateFormat f = new SimpleDateFormat(format);
		try {
			Date d = f.parse(ds);
			cal.setTime(d);
			return cal.getTime();
		} catch (ParseException e) {
			// 
			e.printStackTrace();
			return null;
		}
	}
	
	
	

	/**
	 * 获取当前月份前num个月的yyyyMM
	 * @param num
	 * @return
	 */
	public static String getPreviousMonthYyyyMM(String yyyyMM,int num) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDateByYyyyMM(yyyyMM));
		cal.add(Calendar.MONTH, -num);
		return getDateStrYyyyMM(cal.getTime());
	}

	public static String getDateStrYyyyMM(Date date) {
		return new SimpleDateFormat("yyyyMM").format(date);
	}

	public static Date getDateByYyyyMM(String yyyyMM){
		try {
			return new SimpleDateFormat("yyyyMM").parse(yyyyMM);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("日期格式非yyyyMM");
		}
	}
	/**
	 * 获取当前所属季度
	 * @param yyyyMM
	 * @return
	 */
	public static int getQuarterByMM(String MM) {
		return monthQuarterMap.get(MM);
	}
	/**
	 * 返回上一月所在季度
	 * @return
	 */
	public static int getLastQuarter() {
		return getQuarterByMM(getLastMonth("MM"));
	}

	public static boolean isSameMonth(String preMonth, String month) {
		return preMonth.substring(4,6).equals(month.substring(4,6));
	}
	/**
	 * 获取本月第一天
	 * @param dateStr
	 * @return
	 */
	public static String getMonthFirstDay(String yyyyMMdd) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(str2Date(yyyyMMdd));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return Date2Str(cal.getTime());
	}
	
	/**
	 * 获取本月最后一天
	 * @param dateStr
	 * @return
	 */
	public static String getMonthLastDay(String yyyyMMdd) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(str2Date(yyyyMMdd));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return Date2Str(cal.getTime());
	}
	
	/**
	 * 获取本月第一天
	 * @param dateStr
	 * @return
	 */
	public static String getMonthFirstDayYyyyMM(String month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDateByYyyyMM(month));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return Date2Str(cal.getTime());
	}
	
	/**
	 * 获取本月最后一天
	 * @param dateStr
	 * @return
	 */
	public static String getMonthLastDayYyyyMM(String month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDateByYyyyMM(month));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return Date2Str(cal.getTime());
	}
	
	
	
	
	
	/**
	 * 获取下月第一天
	 */
	public static String getNextMonthFirstDayYyyyMM(String month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDateByYyyyMM(month));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, 1);
		return Date2Str(cal.getTime());
	}
	/**
	 * 获取下月第一天
	 */
	public static String getNextMonthFirstDay(String dateStr) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(str2Date(dateStr));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, 1);
		return Date2Str(cal.getTime());
	}

	public static List<String> getTotalMonthList(String beginMonth, String endMonth) {
		List<String>tMtList=new ArrayList<String>();
		if(beginMonth.compareTo(endMonth)>0)
			throw new IllegalArgumentException("起始月份不能大于结束月份");
		Calendar beginDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		beginDate.setTime(str2Date(beginMonth,MONTH_FORMAT));
		endDate.setTime(str2Date(endMonth,MONTH_FORMAT));
		int count=0;
		while(beginDate.compareTo(endDate)<=0){
			count++;
			tMtList.add(Date2Str(beginDate.getTime(),MONTH_FORMAT));
			beginDate.add(Calendar.MONTH, 1);
		}
		System.out.println("getTotalMonthList::count: "+count);
		return tMtList;
	}
	/**
	 * 获取月最后一天或者deadline中最小的那天
	 * @param month
	 * @param deadline
	 * @return
	 */
	public static String getMonthLastDayOrDeadlineElier(String month,String deadline) {
		String lastDay = getMonthLastDayYyyyMM(month);
		return lastDay.compareTo(deadline)>0?deadline:lastDay;
	}
	/**
	 * 获取年月信息通过yyyyMMdd
	 * @param yyyyMMdd
	 * @return
	 */
	public static String getMonthByDate(String yyyyMMdd) {
		return yyyyMMdd.substring(0,6);
	}

	public static String getMonthFirstDayOrBeginDateLater(String month, String beginDate) {
		String firstDate = getMonthFirstDayYyyyMM(month);
		return firstDate.compareTo(beginDate)<0?beginDate:firstDate;
	}

	public static boolean isAftenoonHr(int hr) {
		return hr>=NOON;
	}
	
	public static boolean isMorningHr(int hr) {
		return hr<=NOON;
	}
	

	public static boolean isSmallLater(int workLateMinute) {
		return workLateMinute<10;
	}

	public static boolean isBigLater(int workLateMinute) {
		return workLateMinute >= 10 && workLateMinute < 60;
	}
	
	
	public static void test(){
		DateTime dateTime = new DateTime("2000-01-01");
		System.out.println(dateTime.plusDays(-1).toString(DAY_FORMAT));
	}
	public static void main(String[] args) {
		test();
	}

	
	


}
