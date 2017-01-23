package com.lottery.api.controller;

import java.util.List;

import javax.validation.constraints.Min;

import org.apache.log4j.Logger;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lottery.api.dto.PageParamVo;
import com.lottery.api.dto.RoundParamVo;
import com.lottery.api.dto.UpdateRoundVo;
import com.lottery.api.task.LotteryTask;
import com.lottery.orm.bo.LotteryRound;
import com.lottery.orm.dao.CustomLotteryMapper;
import com.lottery.orm.dao.LotteryRoundMapper;
import com.lottery.orm.result.HisRoundResult;
import com.lottery.orm.result.RoundResult;
import com.lottery.orm.service.LotteryOrderService;
import com.lottery.orm.service.LotteryRoundService;
import com.lottery.orm.util.EnumType;
import com.lottery.orm.util.MessageTool;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RequestMapping(value = "/lottery", produces = { "application/json;charset=UTF-8" })
@Api(value = "/lottery", description = "游戏信息接口")
@Controller
public class LotteryRoundController {
	public static final Logger LOG = Logger.getLogger(LotteryRoundController.class);

	@Autowired
	private Mapper mapper;

	@Autowired
	LotteryRoundMapper lotteryRoundMapper;

	@Autowired
	LotteryRoundService lotteryRoundService;

	@Autowired
	CustomLotteryMapper customLotteryMapper;

	@Autowired
	LotteryOrderService lotteryOrderService;
	
	@Autowired
	LotteryTask lotteryTask;
	
	@ApiOperation(value = "新增一期游戏", notes = "新增游戏记录", httpMethod = "POST")
	@RequestMapping(value = "/addLotteryRound", method = RequestMethod.POST)
	@ResponseBody
	public RoundResult addLotteryRound(
			@ApiParam(value = "Json参数", required = true) @Validated @RequestBody RoundParamVo param) throws Exception {
		RoundResult result = new RoundResult();
		try {
			LotteryRound round = mapper.map(param, LotteryRound.class);
			round.setLotterytype(EnumType.LotteryType.CornSeed.ID);
			round.setRoundstatus(EnumType.RoundStatus.Open.ID);
			round.setOpentime(param.getOpenTime());
			if (lotteryRoundService.addLotteryRound(round)) {
				lotteryTask.addRoundTask(round);
				result.success(round);
			} else {
				result.fail(MessageTool.FailCode);
			}
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.fail(MessageTool.ErrorCode);
			LOG.error(e.getMessage(), e);
		}
		return result;
	}

	@ApiOperation(value = "获取游戏及赔率信息", notes = "获取游戏及赔率信息", httpMethod = "POST")
	@RequestMapping(value = "/getLotteryRound", method = RequestMethod.POST)
	@ResponseBody
	public RoundResult getLotteryRound() throws Exception {
		RoundResult result = new RoundResult();
		try {
			Integer roundId = customLotteryMapper.selectRoundIdByStatus(EnumType.LotteryType.CornSeed.ID,
					EnumType.RoundStatus.Open.ID);
			if (roundId == null) {
				result.fail("目前没有游戏信息");
			} else {
				LotteryRound lotteryRound = customLotteryMapper.selectRoundByRoundId(roundId);
				if (lotteryRound != null) {
					result.success(lotteryRound);
				} else {
					result.fail(MessageTool.FailCode);
				}
			}
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.fail(MessageTool.ErrorCode);
			LOG.error(e.getMessage(), e);
		}
		return result;
	}
	
	@ApiOperation(value = "获取历史游戏信息", notes = "获取历史游戏信息", httpMethod = "POST")
	@RequestMapping(value = "/getHistoryRound", method = RequestMethod.POST)
	@ResponseBody
	public HisRoundResult getHistoryRound(@ApiParam(value = "Json参数", required = true) @Validated @RequestBody PageParamVo param) throws Exception {
		HisRoundResult result = new HisRoundResult();
		try {
				List<LotteryRound> roundList = customLotteryMapper.selectByHistoryRound(EnumType.LotteryType.CornSeed.ID, EnumType.RoundStatus.End.ID, param.getBeginRow(), param.getPageSize());
				if (roundList != null) {
					result.success(roundList);
				} else {
					result.fail(MessageTool.FailCode);
				}
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.fail(MessageTool.ErrorCode);
			LOG.error(e.getMessage(), e);
		}
		return result;
	}

	@ApiOperation(value = "游戏封盘", notes = "游戏封盘", httpMethod = "POST")
	@RequestMapping(value = "/closeLotteryRound/{roundId}", method = RequestMethod.POST)
	@ResponseBody
	public RoundResult closeLotteryRound(@Min(value = 0, message = "ID格式不正确") @PathVariable Integer roundId) throws Exception {
		RoundResult result = new RoundResult();
		try {
			LotteryRound lotteryRound = new LotteryRound();
			lotteryRound.setRoundid(roundId);
			lotteryRound.setRoundstatus(EnumType.RoundStatus.Close.ID);
			if (lotteryRoundMapper.updateByPrimaryKeySelective(lotteryRound) > 0) {
				result.success();
			} else {
				result.fail(MessageTool.FailCode);
			}
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.fail(MessageTool.ErrorCode);
			LOG.error(e.getMessage(), e);
		}
		return result;
	}

	@ApiOperation(value = "兑奖并结束游戏", notes = "兑奖并结束游戏", httpMethod = "POST")
	@RequestMapping(value = "/endLotteryRound", method = RequestMethod.POST)
	@ResponseBody
	public RoundResult endLotteryRound(
			@ApiParam(value = "Json参数", required = true) @Validated @RequestBody UpdateRoundVo param) throws Exception {
		RoundResult result = new RoundResult();
		try {
			LotteryRound round = customLotteryMapper.selectRoundByRoundId(param.getRoundId());
			if (round == null) {
				result.fail("不存在该期游戏");
			} else if (round.getRoundstatus().equals(EnumType.RoundStatus.End.ID)) {
				result.fail("该期游戏已经开过奖了");
			} else {
				round.setOriginresult(param.getResultStr());
				lotteryRoundService.endLotteryRound(round);
				result.success(round);
			}
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.fail(MessageTool.ErrorCode);
			LOG.error(e.getMessage(), e);
		}
		return result;
	}

}
