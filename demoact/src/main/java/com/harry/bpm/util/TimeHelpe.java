package com.harry.bpm.util;

import java.sql.Time;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeHelpe {

	private final static SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private final static SimpleDateFormat formatter_time = new SimpleDateFormat(
			"HH:mm:ss");
	private final static SimpleDateFormat FORMATTER_YEAR = new SimpleDateFormat(
			"yyyy");
	private final static SimpleDateFormat FORMATTER_MON = new SimpleDateFormat(
			"MM");
	private final static Calendar Cal = Calendar.getInstance();
	/**
	 * 1小时前/后的时间
	 */
	public void getTime() {
		Calendar Cal = Calendar.getInstance();
		int H = 7;
		int M = 30;
		int S = 0;
		Time time = new Time(H, M, S);
		// 如果拿当前时间是:new Date()
		Cal.setTime(new Date(time.getTime()));
		Cal.add(Calendar.HOUR_OF_DAY, -1);
		System.out.println("指定时间的前一小时:" + formatter_time.format(Cal.getTime()));
	}

	public void setTime(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 12);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		System.out.println(formatter.format(calendar.getTime()));
	}
	
	public void getTime(String time){
		Calendar cal = Calendar.getInstance();
		try {
			Date date = formatter.parse("2017-03-30 00:00:00");
			System.out.println(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取当前日期的前/后一天
	 */
	public void getDay() {
		Calendar cal = Calendar.getInstance();// 使用默认时区和语言环境获得一个日历。

		cal.add(Calendar.DAY_OF_MONTH, -1);
		String beforDay = formatter.format(cal.getTime());
		System.out.println("取当前日期的前一天:" + beforDay);

		cal.add(Calendar.DAY_OF_MONTH, +1);
		String behindDay = formatter.format(cal.getTime());
		System.out.println("取当前日期的后一天" + behindDay);

	}
	
	public void getDay(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, +1);
		String format = formatter.format(cal.getTime());
		System.out.println(format);
	}

	/**
	 * 取当前日期的前/后一月
	 */
	public void getMonth() {
		Calendar cal = Calendar.getInstance();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		Integer mon = Integer.valueOf(FORMATTER_MON.format(cal
				.getTime()));
		cal.set(Calendar.MONTH, mon - 2);
		System.out.println("上一个月:"+formatter.format(cal.getTime()));
		
//		cal.set(Calendar.MONTH, mon);
//		System.out.println("下一个月:"+formatter.format(cal.getTime()));
	}

	/**
	 * 一年前/后的时间
	 */
	public void getYear() {
		Format formatter = new SimpleDateFormat("yyyy");
		Date todayDate = new Date();
		long beforeTime = (todayDate.getTime() / 1000) - 60 * 60 * 24 * 365;// 一年前的时间
		long afterTime = (todayDate.getTime() / 1000) + 60 * 60 * 24 * 365;// 一年后的时间
		todayDate.setTime(beforeTime * 1000);
		String beforeDate = formatter.format(todayDate);
		System.out.println("一年前的时间:" + beforeDate);

		todayDate.setTime(afterTime * 1000);
		String afterDate = formatter.format(todayDate);
		System.out.println("一年后的时间:" + afterDate);
	}
	/**
	 * 根据毫秒数格式化时间
	 * @param time
	 * @return
	 */
	public static String getFormatTime(long time){
		long day=time/(24*60*60*1000);
		long hour=(time/(60*60*1000)-day*24);
		long min=((time/(60*1000))-day*24*60-hour*60);
		long s=(time/1000-day*24*60*60-hour*60*60-min*60);
		
		StringBuffer r=new StringBuffer();
		if (day>0) {
			r.append(day+"天");
		}
		if (hour>0) {
			r.append(hour+"小时");
		}
		if (min>0) {
			r.append(min+"分");
		}
		if (s>0) {
			r.append(s+"秒");
		}
		return r.toString();
	}
	
	/**
	 * 计算两个日期之间相隔的天数
	 * 
	 * @param begin
	 *            开始日期,格式:2010-10-10
	 * @param end
	 *            结束日期,格式:2010-10-10
	 */
	public void getBetweenDay(String begin, String end) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate = null;
		Date endDate = null;
		try {
			beginDate = format.parse(begin);
			endDate = format.parse(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long day = (endDate.getTime() - beginDate.getTime())
				/ (24 * 60 * 60 * 1000);
		System.out.println("相隔的天数=" + day);
	}
	
	public void getBetweenDay(String time) {
		Integer ghour=new Integer(time.substring(0, 2));
		Integer gmin=new Integer(time.substring(2, 4));
		Integer gms=(ghour*60*60*1000)+(gmin*60*1000);
		System.out.println("hour: "+ghour);
		System.out.println("min: "+gmin);
		System.out.println("gms: "+gms);
		Calendar nowcal = Calendar.getInstance();
		Integer ms=(nowcal.get(Calendar.HOUR_OF_DAY)*60*60*1000)+(nowcal.get(Calendar.MINUTE)*60*1000);
		System.out.println("now hour: "+nowcal.get(Calendar.HOUR_OF_DAY));
		System.out.println("now min: "+nowcal.get(Calendar.MINUTE));
		System.out.println("ms:"+ms);
		String temp=nowcal.get(Calendar.HOUR_OF_DAY)==ghour?nowcal.get(Calendar.MINUTE)-gmin>30?"N":"Y":"N";
		System.out.println(temp);
	}
	/**
	 * 计算两个日期之间相隔的天,时,分,秒
	 * @param time:2014-04-24 16:40:00
	 */
	public void getBetweenTime(String time){
		try {
			Date parse = formatter.parse(time);
			
			long tm=System.currentTimeMillis()-parse.getTime();
			long day=tm/(24*60*60*1000);
			long hour=(tm/(60*60*1000)-day*24);
			long min=((tm/(60*1000))-day*24*60-hour*60);
			long s=(tm/1000-day*24*60*60-hour*60*60-min*60);
			
			System.out.println(""+day+"天"+hour+"小时"+min+"分"+s+"秒");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 计算两个日期之间相隔的天,时,分,秒
	 * @param time:2014-04-24 16:40:00
	 */
	public static String getBetweenTime(long start, long end) {
		long tm = start - end;
		long day = tm / (24 * 60 * 60 * 1000);
		long hour = (tm / (60 * 60 * 1000) - day * 24);
		long min = ((tm / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (tm / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

		return hour + "小时" + min + "分" + s + "秒";
	}

	/**
	 * 获取当月第一天
	 */
	public static Date monthStart() {
		Calendar cal = Calendar.getInstance();
		int minDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, minDay);
		cal.set(Calendar.HOUR, 00);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);
		return cal.getTime();
	}

	/**
	 * 获取当月最后一天
	 */
	public void getMonthEnd() {
		Calendar cal = Calendar.getInstance();
		int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, maxDay);
	}
	public void setMonthDay(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 5);
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(formatter.format(cal.getTime()));
	}
	/**
	 * 当天处于本年的第几个星期
	 */
	public void setWeekYear(){
		Calendar cal = Calendar.getInstance();
		System.out.println(cal.get(Calendar.WEEK_OF_YEAR));
	}
	/**
	 * 本年第X期所在的日期
	 */
	public void setWeekDays(){
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.WEEK_OF_YEAR, 1);
		String day = formatter.format(cal.getTime());
		
		cal.add(Calendar.DAY_OF_MONTH, +6);
		String behindDay = formatter.format(cal.getTime());
		
		System.out.println(day+" - "+behindDay);
	}
	/**
	 * 当前凌晨0点30之前的时间点按照前一天算
	 * @return
	 */
	private String syncDate(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar threshold = Calendar.getInstance();
		threshold.set(Calendar.HOUR, 00);
		threshold.set(Calendar.MINUTE, 00);
		threshold.set(Calendar.SECOND, 00);
		
		Calendar current = Calendar.getInstance();
		current.set(Calendar.HOUR, 00);
		current.set(Calendar.MINUTE, 10);
		current.set(Calendar.SECOND, 00);
		
		System.out.println(formatter.format(threshold.getTime()));
		long tm=current.getTimeInMillis()-threshold.getTimeInMillis();
		long day=tm/(24*60*60*1000);
		long hour=(tm/(60*60*1000)-day*24);
		long min=((tm/(60*1000))-day*24*60-hour*60);
		
		System.out.println(hour + "小时" + min + "分");
		
		if (0==day&&0==hour&&min<30) {
			threshold.add(Calendar.DAY_OF_MONTH, -1);
			return format.format(new Date(threshold.getTimeInMillis()));
		}
		return format.format(new Date(System.currentTimeMillis()));
	}
}
