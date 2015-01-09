package itg.opt.irongeek.util;

import java.util.Date;


public class BrokerTradeDateSideKey {

	private Date tradeDate;
	//private String tradeDateStr;
	private String broker;
	private String side;
	
	public BrokerTradeDateSideKey(String broker, Date tradeDate, String side) {
		this.broker = broker;
		this.tradeDate = tradeDate;
		this.side = side;
	}
		
    public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getBroker() {
		return broker;
	}

	public void setBroker(String broker) {
		this.broker = broker;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}


	@Override
    public int hashCode() {
        return tradeDate.getMonth() + tradeDate.getDate(); 
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        BrokerTradeDateSideKey other = (BrokerTradeDateSideKey) obj;
        if (broker.equalsIgnoreCase(other.getBroker()) && side.equalsIgnoreCase(other.getSide())) {
            return true;
        }
        return false;
    }
}
