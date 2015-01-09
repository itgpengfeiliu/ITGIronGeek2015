package itg.opt.irongeek.util;

import java.util.Date;


public class BrokerTradeDateKey {

	private Date tradeDate;
	//private String tradeDateStr;
	private String broker;
	
	public BrokerTradeDateKey(String broker, Date tradeDate) {
		this.broker = broker;
		this.tradeDate = tradeDate;
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
        
        BrokerTradeDateKey other = (BrokerTradeDateKey) obj;
        if (broker != other.broker || tradeDate.equals(other.tradeDate)) {
            return false;
        }
        return true;
    }
}
