package com.lottery.orm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.CronScheduleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lottery.orm.bo.LotteryItem;
import com.lottery.orm.bo.LotteryOrder;
import com.lottery.orm.bo.LotteryRound;
import com.lottery.orm.bo.LotteryRoundItem;
import com.lottery.orm.bo.ScheduleJob;
import com.lottery.orm.dao.CustomLotteryMapper;
import com.lottery.orm.dao.LotteryRoundItemMapper;
import com.lottery.orm.dao.LotteryRoundMapper;
import com.lottery.orm.dao.ScheduleJobMapper;
import com.lottery.orm.util.EnumType;
import com.lottery.orm.util.TaskUtils;

@Service
@Transactional
public class LotteryRoundService {
	public final Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private LotteryRoundMapper lotteryRoundMapper;

	@Autowired
	private LotteryRoundItemMapper roundItemMapper;

	@Autowired
	private CustomLotteryMapper customLotteryMapper;
	
	@Autowired
	private ScheduleJobMapper scheduleJobMapper;

	@Autowired
	private LotteryOrderService lotteryOrderService;
	
	@Autowired
	private JobTaskService taskService;

	// 添加投注单
	public boolean addLotteryRound(LotteryRound round) throws Exception {
		round.setRoundstatus(EnumType.RoundStatus.Open.ID);
		round.setStarttime(new Date());
		DateTime openTime = new DateTime(round.getOpentime());
		round.setClosetime(openTime.minusMinutes(5).toDate());//开奖前5分钟封盘
		LotteryRound existRound = customLotteryMapper.selectRoundByTypeAndTerm(round.getLotterytype(), round.getLotteryterm());
		if(existRound==null){
			List<LotteryItem> itemList = customLotteryMapper.selectItemByLotteryType(round.getLotterytype());
			List<LotteryRoundItem> roundItemList = new ArrayList<LotteryRoundItem>();
			lotteryRoundMapper.insertSelective(round);
			if (round.getRoundid() > 0) {
				for (LotteryItem item : itemList) {
					LotteryRoundItem roundItem = new LotteryRoundItem();
					roundItem.setRoundid(round.getRoundid());
					roundItem.setItemno(item.getItemno());
					roundItem.setItemscale(item.getItemscale());
					roundItem.setItemodds(item.getItemodds());
					roundItem.setItembonus(item.getItembonus());
					roundItemMapper.insertSelective(roundItem);
					roundItemList.add(roundItem);
				}
				round.setRoundItemList(roundItemList);
				addRoundTask(round);
				return true;
			}
		}
		return false;
	}
	
	//添加游戏定时任务
	public void addRoundTask(LotteryRound nextRound) throws Exception{
		String cronStr = "";
		try {
			DateTime runGetOpenTime = new DateTime(nextRound.getOpentime()).plusSeconds(3);//执行任务时间最好比开奖晚3秒
			cronStr = TaskUtils.getCron(runGetOpenTime.toDate());
			CronScheduleBuilder.cronSchedule(cronStr);
			Long task1 = new Long(2);
			//taskService.updateCron(task1, cronStr);
			
			ScheduleJob job = taskService.getTaskById(task1);
			if (job != null) {
				job.setCronExpression(cronStr);
				if (ScheduleJob.STATUS_RUNNING.equals(job.getJobStatus())&&taskService.getRunningJob().contains(job)) {
					taskService.updateJobCron(job);
				}else{
					taskService.changeStatus(task1, "start");
				}
				scheduleJobMapper.updateByPrimaryKeySelective(job);
			}
			
			cronStr = TaskUtils.getCron(nextRound.getClosetime());
			CronScheduleBuilder.cronSchedule(cronStr);
			Long task2 = new Long(3);
			job = taskService.getTaskById(task2);
			
			if (job != null) {
				job.setCronExpression(cronStr);
				if (ScheduleJob.STATUS_RUNNING.equals(job.getJobStatus())&&taskService.getRunningJob().contains(job)) {
					taskService.updateJobCron(job);
				}else{
					taskService.changeStatus(task2, "start");
				}
				scheduleJobMapper.updateByPrimaryKeySelective(job);
			}
			//taskService.updateCron(task2, cronStr);
			
			/*if(!taskService.getTaskById(task1).getJobStatus().equals("1")){
				taskService.changeStatus(task1, "start");
			}
			
			if(!taskService.getTaskById(task2).getJobStatus().equals("1")){
				taskService.changeStatus(task2, "start");
			}*/
			
		} catch (Exception e) {
			log.error("更新获取开奖结果任务失败:"+nextRound.getLotteryterm()+":"+cronStr);
			throw new Exception(e);
		}
	}

	// 游戏结束并兑奖
	public boolean endLotteryRound(LotteryRound round) {
		round.setResultstr(getCronLotteryResult(round.getOriginresult()));
		round.setRoundstatus(EnumType.RoundStatus.End.ID);
		round.setEndtime(new Date());
		if (lotteryRoundMapper.updateByPrimaryKeySelective(round) > 0) {
			List<LotteryItem> itemList = customLotteryMapper.selectItemByLotteryType(EnumType.LotteryType.CornSeed.ID);
			List<LotteryOrder> orderList = customLotteryMapper.selectOrderByRoundId(round.getRoundid());
			for (LotteryOrder order : orderList) {
				lotteryOrderService.updateOrderByRound(round, order, itemList);
			}
			return true;
		}
		return false;
	}
	
	//根据广西快乐十分的开奖结果计算玉米籽的开奖结果
	private String getCronLotteryResult(String originResult){
		if(originResult.contains(",")){
			String[] prizeNumbers = originResult.split(",");
			int prizeNumber = Integer.parseInt(prizeNumbers[prizeNumbers.length-1]);
			int cronNumber = prizeNumber % 4 == 0 ? 4 : prizeNumber % 4;
			return String.valueOf(cronNumber);
		}else{
			return null;
		}
	}
	
	// 游戏结束并兑奖
	public boolean endLotteryRoundByTerm(LotteryRound round) {
		LotteryRound existRound = customLotteryMapper.selectRoundByTypeAndTerm(round.getLotterytype(), round.getLotteryterm());
		if(existRound!=null&&!existRound.getRoundstatus().equals(EnumType.RoundStatus.End.ID)){
			round.setRoundid(existRound.getRoundid());
			return endLotteryRound(round);
		}
		return false;
	}
	
	// 游戏封盘
	public boolean closeLotteryRound() {
		List<LotteryRound> roundList =  customLotteryMapper.selectRoundByTime(EnumType.LotteryType.CornSeed.ID, new Date());
		if(roundList.size()>0){
			for (LotteryRound round : roundList) {
				round.setClosetime(new Date());
				round.setRoundstatus(EnumType.RoundStatus.Close.ID);
				lotteryRoundMapper.updateByPrimaryKeySelective(round);
			}
			return true;
		}
		return false;
	}
}
