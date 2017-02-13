package com.lottery.orm.util;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class QueryTool {
	public static Date getPeroidStartTime(Date initDate){
		DateTime initJotaDate=new DateTime(initDate);
		DateTime today=new DateTime(new Date());
		int days = Days.daysBetween(initJotaDate, today).getDays();
		int margin = 28;//每期为28天
		if(days>margin){
			return today.minusDays(days%margin+margin-1).toDate();
		}else{
			return initJotaDate.toDate();
		}
	}
	
	public static void main(String[] args) {
		DateTime today=new DateTime(new Date());
		System.out.println(today.minusDays(60).toString());
		//System.out.println(getPeroidStartTime(today.minusDays(60).toDate()).toGMTString());
	}
}
