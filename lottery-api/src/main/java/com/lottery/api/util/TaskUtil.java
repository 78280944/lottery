package com.lottery.api.util;

import java.text.SimpleDateFormat;

public class TaskUtil {
	public static String getCron(java.util.Date  date){  
        String dateFormat="ss mm HH dd MM ? yyyy";  
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);  
        String formatTimeStr = null;  
        if (date != null) {  
            formatTimeStr = sdf.format(date);  
        }  
        return formatTimeStr;  
    } 
}
