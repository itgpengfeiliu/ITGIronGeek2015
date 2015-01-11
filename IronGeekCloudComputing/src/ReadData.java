import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.smartxls.ChartShape;

import com.smartxls.RangeStyle;
import com.smartxls.WorkBook;

public class ReadData {

//	public String csvFile = "IronGeekCloudInputData.csv";
//	public BufferedReader br = null;
//	public String line = "";
//	public String cvsSplitBy = ",";
	
	public ReadData() {
	}
	
	public void run() {
		 
		String csvFile = "IronGeekCloudInputData.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		
		WorkBook workBook = new WorkBook();
		
		HashMap<BrokerTradeDateSideKey, Double> brokerTradingSummeryMap = new HashMap<BrokerTradeDateSideKey, Double>();
	 
		try {
			int sheet1RowCount = 0;
			int dataRowCount = 0;
			
			workBook.setSheetName(0, "Report");
			workBook.setText(sheet1RowCount, 0, "Symbol");
			workBook.setText(sheet1RowCount, 1, "Shares");
			workBook.setText(sheet1RowCount, 2, "Trade Price");
			workBook.setText(sheet1RowCount, 3, "Trade Date");
			workBook.setText(sheet1RowCount, 4, "Shares Outstanding");
			workBook.setText(sheet1RowCount, 5, "Open Price");
			workBook.setText(sheet1RowCount, 6, "Close Price");
			workBook.setText(sheet1RowCount, 7, "Avg Daily Volum");
			workBook.setText(sheet1RowCount, 8, "Side");
			workBook.setText(sheet1RowCount, 9, "Broker");
			workBook.setText(sheet1RowCount, 10, "Traded Value");
			workBook.setText(sheet1RowCount, 11, "Market Cap");
			workBook.setText(sheet1RowCount, 12, "Net Realized Gain vs. Open Price");
			workBook.setText(sheet1RowCount, 13, "Net Realized Gain vs. Close Price");
			
			RangeStyle rangeStyle = workBook.getRangeStyle(0, 0, 0, 13);
            rangeStyle.setFontBold(true);
            workBook.setRangeStyle(rangeStyle, 0, 0, 0, 13);
            
            workBook.setColWidth(0, 10*256);
            workBook.setColWidth(1, 12*256);
            workBook.setColWidth(2, 15*256);
            workBook.setColWidth(3, 12*256);
            workBook.setColWidth(4, 15*256);
            workBook.setColWidth(5, 12*256);
            workBook.setColWidth(6, 12*256);
            workBook.setColWidth(7, 15*256);
            workBook.setColWidth(8, 8*256);
            workBook.setColWidth(9, 10*256);
            workBook.setColWidth(10, 15*256);
            workBook.setColWidth(11, 20*256);
            workBook.setColWidth(12, 20*256);
            workBook.setColWidth(13, 20*256);
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				dataRowCount++;
				
				if (dataRowCount == 1) {
					continue; // first line is header
				}
				
				sheet1RowCount++;
				
			    // use comma as separator
				String[] row = line.split(cvsSplitBy);
				
				String symbol = row[0];
				int shares = Integer.parseInt(row[1]);
				double tradePrice = Double.valueOf(row[2]);
				DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date tradeDate = format.parse(row[3]);
				String tradeDateStr = row[3];
				BigDecimal sharesOutstanding = new BigDecimal(row[4]); // why not integer?
				double openPrice = Double.valueOf(row[5]);
				double closePrice = Double.valueOf(row[6]);
				BigInteger avgDailyVolum = new BigInteger("0");
				try {
					avgDailyVolum = new BigInteger(row[7]);
				}
			    catch( Exception e ) {
			        
			    }
				String side = row[8];
				//chop($lineValues[9]); //# remove newline char
				String broker = row[9];
				
				double tradedValue = shares * tradePrice; // double or BigInteger?
				BigDecimal marketCap = sharesOutstanding.multiply(new BigDecimal(closePrice));
				double netRealizedGainOpenPrice  = (side == "buy") ? (openPrice - tradePrice) * shares : (tradePrice - openPrice) * shares; // double or BigInteger?
				double netRealizedGainClosePrice = (side == "buy") ? (closePrice - tradePrice) * shares : (tradePrice - closePrice) * shares; // double or BigInteger?
				
				workBook.setText(sheet1RowCount, 0, symbol);
				//workBook.setText(count, 1, Integer.toString(shares));
				workBook.setNumber(sheet1RowCount, 1, shares);
				//workBook.setText(count, 2, Double.toString(tradePrice));
				workBook.setNumber(sheet1RowCount, 2, tradePrice);
				workBook.setText(sheet1RowCount, 3, tradeDateStr);
				
				//workBook.setText(sheet1RowCount, 4, sharesOutstanding.toString());
				workBook.setNumber(sheet1RowCount, 4, sharesOutstanding.longValue());
				
				//workBook.setText(count, 5, Double.toString(openPrice));
				workBook.setNumber(sheet1RowCount, 5, openPrice);
				//workBook.setText(count, 6, Double.toString(closePrice));
				workBook.setNumber(sheet1RowCount, 6, closePrice);
				
				//workBook.setText(count, 7, avgDailyVolum.toString());
				workBook.setNumber(sheet1RowCount, 7, avgDailyVolum.longValue());
				
				workBook.setText(sheet1RowCount, 8, side);
				workBook.setText(sheet1RowCount, 9, broker);
				//workBook.setText(count, 10, Double.toString(tradedValue));
				workBook.setNumber(sheet1RowCount, 10, tradedValue);
				
				//workBook.setText(count, 11, marketCap.toString());
				workBook.setNumber(sheet1RowCount, 11, marketCap.doubleValue());
				
				//workBook.setText(count,	12, Double.toString(netRealizedGainOpenPrice));
				workBook.setNumber(sheet1RowCount, 12, netRealizedGainOpenPrice);
				//workBook.setText(count, 13, Double.toString(netRealizedGainClosePrice));
				workBook.setNumber(sheet1RowCount, 13, netRealizedGainClosePrice);
				
				BrokerTradeDateSideKey btdsKey = new BrokerTradeDateSideKey(broker, tradeDate, side);
				if (brokerTradingSummeryMap.containsKey(btdsKey)) {
					double sumTradedValue = (double) brokerTradingSummeryMap.get(btdsKey);
					sumTradedValue += tradedValue;
					//brokerTradingSummeryMap.put(btdsKey, brokerTradingSummeryMap.get(brokerTradingSummeryMap) + tradedValue);
					brokerTradingSummeryMap.put(btdsKey, sumTradedValue);
				}
				else {
					brokerTradingSummeryMap.put(btdsKey, tradedValue);
				}
	 
				
				
			}
			
			int sheet2Count = 0; // aaaa
			workBook.insertSheets(1, 1);
			workBook.setSheetName(1, "aggregate report");
			
			workBook.setText(sheet2Count, 0, "Broker Trading Summary");
			sheet2Count++;
			
			workBook.setText(sheet2Count, 0, "Broker");
			workBook.setText(sheet2Count, 1, "Trade Date");
			workBook.setText(sheet2Count, 2, "Side");
			workBook.setText(sheet2Count, 3, "Total Traded Value");
			
			rangeStyle = workBook.getRangeStyle(0, 0, 1, 3);
            rangeStyle.setFontBold(true);
            workBook.setRangeStyle(rangeStyle, 0, 0, 1, 3);
            
            workBook.setColWidth(0, 15*256);
            workBook.setColWidth(1, 15*256);
            workBook.setColWidth(2, 10*256);
            workBook.setColWidth(3, 20*256);
			
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			
			Iterator it = brokerTradingSummeryMap.entrySet().iterator();
		    while (it.hasNext()) {
		    	sheet2Count++;
		    	
		        Map.Entry pairs = (Map.Entry)it.next();
		        BrokerTradeDateSideKey btdKey = (BrokerTradeDateSideKey) pairs.getKey();
		        workBook.setText(sheet2Count, 0,  btdKey.getBroker());
		        workBook.setText(sheet2Count, 1,  format.format(btdKey.getTradeDate()));
		        workBook.setText(sheet2Count, 2,  btdKey.getSide());
		        workBook.setNumber(sheet2Count, 3, (Double) pairs.getValue());
		        //it.remove(); // avoids a ConcurrentModificationException
		    }
		    
		    int left = 2;
		    int top = 15;
		    int right = 18;
		    int bottom = 50;
		    
		    //workBook.insertSheets(1, 1);
			//workBook.setSheetName(2, "chart report");
		    //ChartShape chart = workBook.addChartSheet(0);
		    ChartShape chart = workBook.addChart(left, top, right, bottom);
		    chart.setChartType(ChartShape.Column);
		    //link data source, link each series to columns(true to rows).
            chart.setLinkRange("aggregate report!$A$3:$F$"+sheet2Count,false);
            //set axis title
            chart.setAxisTitle(ChartShape.XAxis, 0, "X-axis Broker");
			chart.setAxisTitle(ChartShape.YAxis, 0, "Y-axis Total Traded Value");
            //set series name
			//chart.setSeriesName(0, "My Series number 1");
			//chart.setSeriesName(1, "My Series number 2");
			//chart.setSeriesName(2, "My Series number 3");
			chart.setTitle("My Chart");
            //set chart type to 3D
            //chart.set3Dimensional(true);

            //move chart sheet to index 1,index are from left to right,start from 0.
            workBook.setSheet(1);
            workBook.moveSheet(0);
		    
			workBook.writeXLSX("IronGeekCloudReport.xlsx");
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 
		System.out.println("Done");
	}

	
	public static void main(String[] args) {
		ReadData obj = new ReadData();
		obj.run();
	}

}
