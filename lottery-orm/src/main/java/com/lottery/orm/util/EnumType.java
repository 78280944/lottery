package com.lottery.orm.util;

public interface EnumType {
  enum LotteryType {
    CornSeed("01", "玉米籽");
    private LotteryType(String ID, String NAME) {
      this.ID = ID;
      this.NAME = NAME;
    }

    public final String ID;
    public final String NAME;
    public final static String enumDesc = "游戏类型";
  }
  enum ItemType {
    Type_01("01", "番摊玉米籽"), Type_02("02", "广西快乐十分");
    private ItemType(String ID, String NAME) {
      this.ID = ID;
      this.NAME = NAME;
    }

    public final String ID;
    public final String NAME;
    public final static String enumDesc = "下注项类型";
  }
  
  enum RoundStatus {
    Open("Open", "开盘中"), Close("Close", "已封盘"), End("End", "已开奖");
    private RoundStatus(String ID, String NAME) {
      this.ID = ID;
      this.NAME = NAME;
    }

    public final String ID;
    public final String NAME;
    public final static String enumDesc = "游戏状态";
  }
  
  enum TradeType {
    Inout("Inout", "出入金"), Trade("Trade", "交易");
    private TradeType(String ID, String NAME) {
      this.ID = ID;
      this.NAME = NAME;
    }

    public final String ID;
    public final String NAME;
    public final static String enumDesc = "业务类型";
  }
  
  enum RalativeType {
    In("In", "入金"),Out("Out", "出金"), Commision("Commision", "佣金"), Pay("Pay", "付款"), Order("Order", "下注"), Prize("Prize", "奖金"), Return("Return", "洗码");
    private RalativeType(String ID, String NAME) {
      this.ID = ID;
      this.NAME = NAME;
    }

    public final String ID;
    public final String NAME;
    public final static String enumDesc = "业务相关类型";
  }
}
