package com.lottery.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lottery.api.dto.OrderDetailVo;
import com.lottery.api.dto.OrderParamVo;
import com.lottery.orm.bo.LotteryOrder;
import com.lottery.orm.bo.LotteryOrderDetail;
import com.lottery.orm.bo.LotteryRound;
import com.lottery.orm.dao.LotteryRoundMapper;
import com.lottery.orm.result.OrderResult;
import com.lottery.orm.service.LotteryOrderService;
import com.lottery.orm.util.EnumType;
import com.lottery.orm.util.MessageTool;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RequestMapping(value = "/order", produces = { "application/json;charset=UTF-8" })
@Api(value = "/order", description = "投注信息接口")
@Controller
public class LotteryOrderController {
	public static final Logger LOG = Logger.getLogger(LotteryOrderController.class);

	@Autowired
	private Mapper mapper;

	@Autowired
	private LotteryOrderService lotteryOrderService;

	@Autowired
	private LotteryRoundMapper lotteryRoundMapper;

	@ApiOperation(value = "新增投注记录", notes = "新增资金交易记录", httpMethod = "POST")
	@RequestMapping(value = "/addLotteryOrder", method = RequestMethod.POST)
	@ResponseBody
	public OrderResult addLotteryOrder(
			@ApiParam(value = "Json参数", required = true) @Validated @RequestBody OrderParamVo param) throws Exception {
		OrderResult result = new OrderResult();
		try {
			LotteryOrder order = mapper.map(param, LotteryOrder.class);
			LotteryRound round = lotteryRoundMapper.selectByPrimaryKey(order.getRoundid());
			if (round == null) {
				result.fail("不存在该期游戏");
			} else if (!round.getRoundstatus().equals(EnumType.RoundStatus.Open.ID)) {
				result.fail("该期游戏目前无法投注");
			} else {
				List<LotteryOrderDetail> orderDetails = new ArrayList<LotteryOrderDetail>();
				for (OrderDetailVo orderDetailVo : param.getOrderDetails()) {
					LotteryOrderDetail orderDetail = mapper.map(orderDetailVo, LotteryOrderDetail.class);
					orderDetails.add(orderDetail);
				}
				order.setOrderDetailList(orderDetails);
				if (lotteryOrderService.checkLotteryOrder(order)) {
					lotteryOrderService.addLotteryOrder(order);
					result.success(order);
				} else {
					result.fail(MessageTool.FailCode);
				}
			}
			LOG.info(result.getMessage());
		} catch (Exception e) {
			result.error();
			LOG.error(e.getMessage(), e);
		}
		return result;
	}

	/*
	 * @ApiOperation(value = "更新投注记录", notes = "更新投注记录", httpMethod = "POST")
	 * 
	 * @RequestMapping(value = "/updateLotteryOrder", method =
	 * RequestMethod.POST)
	 * 
	 * @ResponseBody public RestResult updateLotteryOrder(
	 * 
	 * @ApiParam(value = "Json参数", required = true) @Validated @RequestBody
	 * OrderParamVo param) throws Exception { RestResult result = new
	 * RestResult(); try { LotteryOrder lotteryOrder = mapper.map(param,
	 * LotteryOrder.class); List<LotteryOrderDetail> orderDetails = new
	 * ArrayList<LotteryOrderDetail>(); for (OrderDetailVo orderDetailVo :
	 * param.getOrderDetails()) { LotteryOrderDetail lotteryOrderDetail =
	 * mapper.map(orderDetailVo, LotteryOrderDetail.class);
	 * orderDetails.add(lotteryOrderDetail); } if
	 * (lotteryOrderService.updateLotteryOrder(lotteryOrder)) { result =
	 * RestResult.success(lotteryOrder); } else { result =
	 * RestResult.fail(MessageTool.FailCode); }
	 * 
	 * LOG.info(result.getMessage()); } catch (Exception e) {
	 * LOG.error(e.getMessage(), e); } return result; }
	 */

}
