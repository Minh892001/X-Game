package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 秒杀活动物品购买信息
 * 
 * @author lixiongming
 *
 */
@Entity
@Table(name = "seckill_item")
public class SeckillItem implements Serializable{
	private static final long serialVersionUID = 4375843242985519092L;
	private String id;
	private int seckillId;// 秒杀的商品ID
	private int buyNum;
	private Date createDate;

	public SeckillItem() {

	}

	public SeckillItem(String id, int seckillId, int buyNum,Date createDate) {
		super();
		this.id = id;
		this.seckillId = seckillId;
		this.buyNum = buyNum;
		this.createDate = createDate;
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "seckill_id")
	public int getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(int seckillId) {
		this.seckillId = seckillId;
	}

	@Column(name = "buy_num")
	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
