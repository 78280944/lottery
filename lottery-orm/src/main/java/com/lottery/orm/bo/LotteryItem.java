package com.lottery.orm.bo;

public class LotteryItem {
    private Integer itemid;

	private String lotterytype;

	private String itemno;

	private String itemname;

	private String itemtype;

	private String itemgroup;

	private Double itemscale;

	private Double itemodds;

	private Double itembonus;

	private String itemnamecn;

	public Integer getItemid() {
		return itemid;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}

	public String getLotterytype() {
		return lotterytype;
	}

	public void setLotterytype(String lotterytype) {
		this.lotterytype = lotterytype == null ? null : lotterytype.trim();
	}

	public String getItemno() {
		return itemno;
	}

	public void setItemno(String itemno) {
		this.itemno = itemno == null ? null : itemno.trim();
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname == null ? null : itemname.trim();
	}

	public String getItemtype() {
		return itemtype;
	}

	public void setItemtype(String itemtype) {
		this.itemtype = itemtype == null ? null : itemtype.trim();
	}

	public String getItemgroup() {
		return itemgroup;
	}

	public void setItemgroup(String itemgroup) {
		this.itemgroup = itemgroup == null ? null : itemgroup.trim();
	}

	public Double getItemscale() {
		return itemscale;
	}

	public void setItemscale(Double itemscale) {
		this.itemscale = itemscale;
	}

	public Double getItemodds() {
		return itemodds;
	}

	public void setItemodds(Double itemodds) {
		this.itemodds = itemodds;
	}

	public Double getItembonus() {
		return itembonus;
	}

	public void setItembonus(Double itembonus) {
		this.itembonus = itembonus;
	}

	public String getItemnamecn() {
		return itemnamecn;
	}

	public void setItemnamecn(String itemnamecn) {
		this.itemnamecn = itemnamecn == null ? null : itemnamecn.trim();
	}
    
    public boolean equals(Object o) {
        if (o instanceof LotteryItem) {
            LotteryItem i = (LotteryItem) o;
            if (itemno == null) {
                return i.getItemno() == null;
            } else {
                return itemno.equals(i.getItemno());
            }
        } else {
            return false;
        }
    }
}