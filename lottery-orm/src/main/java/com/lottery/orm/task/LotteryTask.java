package com.lottery.orm.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lottery.orm.bo.LotteryRound;
import com.lottery.orm.service.JobTaskService;
import com.lottery.orm.service.LotteryRoundService;
import com.lottery.orm.util.EnumType;
import com.lottery.orm.util.HttpclientTool;

@Component
public class LotteryTask {
	public final Logger log = Logger.getLogger(this.getClass());
	private final String LOTTERY_API_URL = "http://t.apiplus.cn/newly.do?code=cqklsf&format=json&extend=true";
	private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final Integer ROUND_INTERVAL_MINUTES = 10;//游戏间隔时间
	
	@Autowired
	private LotteryRoundService lotteryRoundService;
	
	/**
	 * 获取广西快乐十分开奖结果
	 */
	public void getLotteryOriginResult() {
		try{
			String result = HttpclientTool.get(LOTTERY_API_URL);
			if(StringUtils.isNotBlank(result)&&result.trim().startsWith("{")){
				JSONObject jObj = new JSONObject(result);
				//Date latestOpenTime = (new DateTime()).toDate();
				JSONObject latestOpenObj = null;
				if(jObj.has("open")){
					JSONArray openArray = jObj.getJSONArray("open");//更新游戏开奖结果
					for(int i=0; i<openArray.length(); i++){
						JSONObject openObj = openArray.getJSONObject(i);
						if(i==0){
							latestOpenObj = openObj;
						}
						LotteryRound openRound = new LotteryRound();
						openRound.setLotterytype(EnumType.LotteryType.CornSeed.ID);//玉米籽
						openRound.setLotteryterm(openObj.getString("expect"));//开奖游戏期次
						openRound.setOriginresult(openObj.getString("opencode"));
						openRound.setOpentime(format.parse(openObj.getString("opentime")));//更新开奖时间
						if(lotteryRoundService.endLotteryRoundByTerm(openRound)){
							log.info("更新开奖信息:"+openRound.getLotteryterm());
						}else{
							break;
						}
					}
				}
				
				
				
				if(jObj.has("next")){
					JSONArray nextArray = jObj.getJSONArray("next");//获取下一轮游戏期次
					Date nextOpenTime = null;//最近的下一轮游戏
					JSONObject nextObj = null;
					for(int i=0; i<nextArray.length(); i++){
						JSONObject tempObj = nextArray.getJSONObject(i);
						String opentimeStr = tempObj.getString("opentime");
						Date tempOpenTime = format.parse(opentimeStr.replace("**", "01"));
						if(nextOpenTime==null||nextOpenTime.after(tempOpenTime)){
							nextOpenTime = tempOpenTime;
							nextObj = tempObj;
						}
					}
					if(nextObj!=null){
						String opentimeStr = nextObj.getString("opentime");
						Date opentime = format.parse(opentimeStr.replace("**", "01"));
						if(opentimeStr.contains("**")&&latestOpenObj!=null){//用于测试
							Date latestOpenTime = format.parse(latestOpenObj.getString("opentime"));
							opentime = (new DateTime(latestOpenTime)).plusMinutes(ROUND_INTERVAL_MINUTES).toDate();
						}
						addNextRound(nextObj.getString("expect"), opentime);
					}
				}else{
					/*if(latestOpenObj!=null){//当没有下期信息时用于测试
						DateTime latestOpenTime = new DateTime(format.parse(latestOpenObj.getString("opentime")));
						DateTime opentime = latestOpenTime.plusMinutes(ROUND_INTERVAL_MINUTES);
						Period period=new Period(latestOpenTime,opentime);
						String nextExpect = null;
						if(period.getDays()==0){
							nextExpect = String.valueOf(Long.parseLong(latestOpenObj.getString("expect"))+1);
						}else{
							nextExpect = String.valueOf(Integer.parseInt(opentime.toString("yyyyMMdd"))*1000+1);
						}
						addNextRound(nextExpect, opentime.toDate());
					}*/
					log.info("未获取到下期游戏信息！");
				}
				
				
			}else{
				log.error("获取的接口数据有问题!");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void addNextRound(String term, Date opentime) throws Exception{
		LotteryRound nextRound = new LotteryRound();
		nextRound.setLotterytype(EnumType.LotteryType.CornSeed.ID);//玉米籽
		nextRound.setLotteryterm(term);//下一轮游戏期次
		nextRound.setOpentime(opentime);//预计开奖时间
		if(lotteryRoundService.addLotteryRound(nextRound)){
			log.info("新增一期游戏:"+nextRound.getLotteryterm());
		}
	}
	
	
	public void closeLottery() {
		try{
			if(lotteryRoundService.closeLotteryRound()){
				log.info("游戏封盘成功!");
			}else{
				log.info("游戏封盘失败!");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	 
}
