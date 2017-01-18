package com.lottery.orm.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class RemarkDto {
	
	    @ApiModelProperty(value = "在线客服")
	    private String online;
	    
	    @ApiModelProperty(value = "分享链接")
	    private String share; 
	    
	    @ApiModelProperty(value = "规则说明")
	    private String rule;

		public String getOnline() {
			return online;
		}

		public void setOnline(String online) {
			this.online = online;
		}

		public String getShare() {
			return share;
		}

		public void setShare(String share) {
			this.share = share;
		}

		public String getRule() {
			return rule;
		}

		public void setRule(String rule) {
			this.rule = rule;
		}

}
