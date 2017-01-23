package com.lottery.api.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.quartz.CronScheduleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lottery.api.util.HttpclientTool;
import com.lottery.api.util.TaskUtil;
import com.lottery.orm.bo.LotteryRound;
import com.lottery.orm.service.JobTaskService;
import com.lottery.orm.service.LotteryRoundService;
import com.lottery.orm.util.EnumType;

@Component
public class LotteryTask {
	public final Logger log = Logger.getLogger(this.getClass());
	private final String LOTTERY_API_URL = "http://t.apiplus.cn/newly.do?code=gxklsf&format=json&extend=true";
	private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private LotteryRoundService lotteryRoundService;
	
	@Autowired
	private JobTaskService taskService;
	
	/**
	 * 获取广西快乐十分开奖结果
	 */
	public void getLotteryOriginResult() {
		try{
			String result = HttpclientTool.get(LOTTERY_API_URL);
			if(StringUtils.isNotBlank(result)&&result.trim().startsWith("{")){
				JSONObject jObj = new JSONObject(result);
				if(jObj.has("open")){
					JSONArray openArray = jObj.getJSONArray("open");//更新游戏开奖结果
					for(int i=0; i<openArray.length(); i++){
						JSONObject openObj = openArray.getJSONObject(i);
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
						Date tempOpenTime = format.parse(tempObj.getString("opentime").replace("*", "0"));
						if(nextOpenTime==null||nextOpenTime.after(tempOpenTime)){
							nextOpenTime = tempOpenTime;
							nextObj = tempObj;
						}
					}
					if(nextObj!=null){
						LotteryRound nextRound = new LotteryRound();
						nextRound.setLotterytype(EnumType.LotteryType.CornSeed.ID);//玉米籽
						nextRound.setLotteryterm(nextObj.getString("expect"));//下一轮游戏期次
						Date opentime = format.parse(nextObj.getString("opentime").replace("*", "0"));
						nextRound.setOpentime(opentime);//预计开奖时间
						if(lotteryRoundService.addLotteryRound(nextRound)){
							addRoundTask(nextRound);
							log.info("新增一期游戏:"+nextRound.getLotteryterm());
						}
					}
				}
				
				
			}else{
				log.error("获取的接口数据有问题!");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean addRoundTask(LotteryRound nextRound){
		try {
			String cronStr = TaskUtil.getCron(nextRound.getOpentime());
			CronScheduleBuilder.cronSchedule(cronStr);
			taskService.updateCron(new Long(2), cronStr);
			taskService.updateCron(new Long(3), TaskUtil.getCron(nextRound.getClosetime()));
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("更新获取开奖结果任务失败:"+nextRound.getLotteryterm());
			return false;
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
