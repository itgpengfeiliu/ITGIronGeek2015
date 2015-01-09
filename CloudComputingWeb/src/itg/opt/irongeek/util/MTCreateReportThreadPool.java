package itg.opt.irongeek.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.smartxls.ChartShape;
import com.smartxls.RangeStyle;
import com.smartxls.WorkBook;

public class MTCreateReportThreadPool {
	
	private String csvFileName = "IronGeekCloudInputData.csv";
	private String reportFolder = "";
	private String reportFileName = "IronGeekCloudReport.xlsx";
	
	private HashMap<BrokerTradeDateSideKey, Double> brokerTradingSummeryMapAll = new HashMap<BrokerTradeDateSideKey, Double>();
	
	private HashMap<BrokerTradeDateKey, Double> brokerRankingMap = new HashMap<BrokerTradeDateKey, Double>();
	
	
	public MTCreateReportThreadPool(String csvFileName, String reportFolder, String reportFileName) {
		this.csvFileName = csvFileName;
		this.reportFolder = reportFolder;
		this.reportFileName = reportFileName;
	}
	
	public void loadFile() {
		List<Thread> allThread = new ArrayList<Thread>();
		for(int i = 0; i < 10; ++ i){
			CreateReportThread cr;
			try {
				cr = new CreateReportThread(csvFileName, i); // , i * 1000 + 1, i * 1000 + 1001
				Thread th = new Thread(cr);
				allThread.add(th);
				th.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for(int i = 0; i < 10; ++ i){
			try {
				allThread.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		int sheet1RowCount = 0;
		WorkBook workBook = new WorkBook();
		
		try {
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
	        
	        for (int j=0; j<CreateReportThread.fileRows; j++) {
	        	sheet1RowCount++;
	        	workBook.setText(sheet1RowCount, 0, CreateReportThread.symbol[j]);
				workBook.setNumber(sheet1RowCount, 1, CreateReportThread.shares[j]);
				workBook.setNumber(sheet1RowCount, 2, CreateReportThread.tradePrice[j]);
				workBook.setText(sheet1RowCount, 3, CreateReportThread.tradeDateStr[j]);
				workBook.setNumber(sheet1RowCount, 4, CreateReportThread.sharesOutstanding[j].longValue());
				workBook.setNumber(sheet1RowCount, 5, CreateReportThread.openPrice[j]);
				workBook.setNumber(sheet1RowCount, 7, CreateReportThread.avgDailyVolum[j].longValue());
				workBook.setText(sheet1RowCount, 8, CreateReportThread.side[j]);
				workBook.setText(sheet1RowCount, 9, CreateReportThread.broker[j]);
				workBook.setNumber(sheet1RowCount, 10, CreateReportThread.tradedValueAll[j]);
				workBook.setNumber(sheet1RowCount, 11, CreateReportThread.marketCapAll[j].doubleValue());
				workBook.setNumber(sheet1RowCount, 12, CreateReportThread.netRealizedGainOpenPriceAll[j]);
				workBook.setNumber(sheet1RowCount, 13, CreateReportThread.netRealizedGainClosePriceAll[j]);
	        }
	        
	        for (int x = 0; x < CreateReportThread.brokerTradingSummeryMapList.size(); x++)
	        {
	        	HashMap<BrokerTradeDateSideKey, Double> tmpMap = CreateReportThread.brokerTradingSummeryMapList.get(x);
	        	
	        	Iterator it = tmpMap.entrySet().iterator();
			    while (it.hasNext()) {
			    	Map.Entry pairs = (Map.Entry)it.next();
			        BrokerTradeDateSideKey tmpKey = (BrokerTradeDateSideKey) pairs.getKey();
			        double tmpValue = (double) tmpMap.get(tmpKey);
			        
					if (brokerTradingSummeryMapAll.containsKey(tmpKey)) {
						double sumTradedValue = (double) brokerTradingSummeryMapAll.get(tmpKey);
						sumTradedValue += tmpValue;
						brokerTradingSummeryMapAll.put(tmpKey, sumTradedValue);
					}
					else {
						brokerTradingSummeryMapAll.put(tmpKey, tmpValue);
					}
			    }
	        	
	        } 
	        
	        int sheet2Count = 0;
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
			
			Iterator it = brokerTradingSummeryMapAll.entrySet().iterator();
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
		    
		    
		    Iterator itMT = brokerTradingSummeryMapAll.entrySet().iterator();
		    while (itMT.hasNext()) {
		    	Map.Entry pairs = (Map.Entry)itMT.next();
		        BrokerTradeDateSideKey tmpKey = (BrokerTradeDateSideKey) pairs.getKey();
		        BrokerTradeDateKey rKey = new BrokerTradeDateKey(tmpKey.getBroker(), tmpKey.getTradeDate());
		        double tmpValue = 0;
		        if (tmpKey.getSide() == "buy") {
		        	tmpValue = (double) brokerTradingSummeryMapAll.get(tmpKey);
		        }
		        else {
		        	tmpValue = 0 - (double) brokerTradingSummeryMapAll.get(tmpKey);
		        }
		        
				if (brokerRankingMap.containsKey(rKey)) {
					double sumTradedValue = (double) brokerRankingMap.get(rKey);
					sumTradedValue += tmpValue;
					brokerRankingMap.put(rKey, sumTradedValue);
				}
				else {
					brokerRankingMap.put(rKey, tmpValue);
				}
		    }
		    
		    int sheet3Count = 0;
			workBook.insertSheets(1, 1);
			workBook.setSheetName(1, "ranking report");
			
			workBook.setText(sheet2Count, 0, "Broker Ranking Report");
			sheet3Count++;
			
			workBook.setText(sheet3Count, 0, "Broker");
			workBook.setText(sheet3Count, 1, "Trade Date");
			workBook.setText(sheet3Count, 2, "Total Realized Gain");
			
			rangeStyle = workBook.getRangeStyle(0, 0, 1, 2);
            rangeStyle.setFontBold(true);
            workBook.setRangeStyle(rangeStyle, 0, 0, 1, 2);
            
            workBook.setColWidth(0, 15*256);
            workBook.setColWidth(1, 15*256);
            workBook.setColWidth(2, 20*256);
			
			//DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			
			Iterator itR = brokerRankingMap.entrySet().iterator();
		    while (itR.hasNext()) {
		    	sheet3Count++;
		    	
		        Map.Entry pairs = (Map.Entry)itR.next();
		        BrokerTradeDateKey btdKey = (BrokerTradeDateKey) pairs.getKey();
		        workBook.setText(sheet3Count, 0,  btdKey.getBroker());
		        workBook.setText(sheet3Count, 1,  format.format(btdKey.getTradeDate()));
		        workBook.setNumber(sheet3Count, 3, (Double) pairs.getValue());
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
            chart.setLinkRange("aggregate report!$A$3:$F$"+sheet3Count,false);
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
		    
			workBook.writeXLSX(reportFolder+reportFileName);
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args) {
//		MTCreateReportThreadPool mtCRThreadPool = new MTCreateReportThreadPool("IronGeekCloudInputData.csv");
//		mtCRThreadPool.loadFile();
//		
//	}
}

class CreateReportThread implements Runnable {
	
	String csvFile;
	int startLine;
	int endLine;
	int id;
	static List<String> lines;
	static int fileRows;
	
	static String[] symbol;
	static int[] shares;
	static double[] tradePrice;
	static Date[] tradeDate;
	static String[] tradeDateStr;
	static BigDecimal[] sharesOutstanding;
	static double[] openPrice;
	static double[] closePrice;
	static BigInteger[] avgDailyVolum;
	static String[] side;
	static String[] broker;
	
	static double[] tradedValueAll;
	static BigDecimal[] marketCapAll;
	static double[] netRealizedGainOpenPriceAll;
	static double[] netRealizedGainClosePriceAll;
	
	static ArrayList<HashMap<BrokerTradeDateSideKey, Double>> brokerTradingSummeryMapList = new ArrayList<HashMap<BrokerTradeDateSideKey, Double>>(10);
	
	public CreateReportThread(String csv, int tid) throws IOException{ // , int start, int end
		csvFile = csv;
		id = tid;
		//startLine = start;
		//endLine = end;
		brokerTradingSummeryMapList.add(id, new HashMap<BrokerTradeDateSideKey, Double>());
		
		if(lines == null){
			Path csvFile = Paths.get("IronGeekCloudInputData.csv");
			lines = Files.readAllLines(csvFile, Charset.defaultCharset());
			
			fileRows = lines.size() - 1;
			System.out.println("total lines : " + fileRows);
			
			symbol = new String[fileRows];
			shares = new int[fileRows];
			tradePrice = new double[fileRows];
			tradeDate = new Date[fileRows];
			tradeDateStr = new String[fileRows];
			sharesOutstanding = new BigDecimal[fileRows];
			openPrice = new double[fileRows];
			closePrice = new double[fileRows];
			avgDailyVolum = new BigInteger[fileRows];
			side = new String[fileRows];
			broker = new String[fileRows];
			
			tradedValueAll = new double[fileRows];
			marketCapAll = new BigDecimal[fileRows];
			netRealizedGainOpenPriceAll = new double[fileRows];
			netRealizedGainClosePriceAll = new double[fileRows];	
		}
		
		int reminder = fileRows % 10;
		int ave = (fileRows - reminder) / 10;
		
		startLine = id * ave + 1;
		endLine = id * ave + ave;
		
		if (id == 9) {
			endLine += reminder;
		}
		//System.out.println("from  " + startLine + " to " + endLine);
	}

	@Override
	public void run() {
		BufferedReader br = null;
		//String line = "";
		String cvsSplitBy = ",";
		
		
		try {
			//int sheet1RowCount = 0;
			//int dataRowCount = startLine;
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			            
            for(int i = startLine; i <= endLine; ++ i){
            	//sheet1RowCount ++;
            
            	String curLine = lines.get(i);
            	String[] row = curLine.split(cvsSplitBy);
            	
            	symbol[i-1] = row[0];
				shares[i-1] = Integer.parseInt(row[1]);
				tradePrice[i-1] = Double.valueOf(row[2]);
				tradeDate[i-1] = format.parse(row[3]);
				tradeDateStr[i-1] = row[3];
				sharesOutstanding[i-1] = new BigDecimal(row[4]); // why not integer?
				openPrice[i-1] = Double.valueOf(row[5]);
				closePrice[i-1] = Double.valueOf(row[6]);
				avgDailyVolum[i-1] = new BigInteger("0");
				try {
					avgDailyVolum[i-1] = new BigInteger(row[7]);
				}
			    catch( Exception e ) {
			        
			    }
				side[i-1] = row[8];
				broker[i-1] = row[9];
				
				double tradedValue = shares[i-1] * tradePrice[i-1]; // double or BigInteger?
				tradedValueAll[i-1] = tradedValue;
				BigDecimal marketCap = sharesOutstanding[i-1].multiply(new BigDecimal(closePrice[i-1]));
				marketCapAll[i-1] = marketCap;
				double netRealizedGainOpenPrice  = (side[i-1] == "buy") ? (openPrice[i-1] - tradePrice[i-1]) * shares[i-1] : (tradePrice[i-1] - openPrice[i-1]) * shares[i-1]; // double or BigInteger?
				netRealizedGainOpenPriceAll[i-1] = netRealizedGainOpenPrice;
				double netRealizedGainClosePrice = (side[i-1] == "buy") ? (closePrice[i-1] - tradePrice[i]) * shares[i-1] : (tradePrice[i-1] - closePrice[i-1]) * shares[i-1]; // double or BigInteger?
				netRealizedGainClosePriceAll[i-1] = netRealizedGainClosePrice;
				
				BrokerTradeDateSideKey btdsKey = new BrokerTradeDateSideKey(broker[i-1], tradeDate[i-1], side[i-1]);
				if (brokerTradingSummeryMapList.get(id).containsKey(btdsKey)) {
					double sumTradedValue = (double) brokerTradingSummeryMapList.get(id).get(btdsKey);
					sumTradedValue += tradedValue;
					//brokerTradingSummeryMap.put(btdsKey, brokerTradingSummeryMap.get(brokerTradingSummeryMap) + tradedValue);
					brokerTradingSummeryMapList.get(id).put(btdsKey, sumTradedValue);
				}
				else {
					brokerTradingSummeryMapList.get(id).put(btdsKey, tradedValue);
				}
            }
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
		System.out.println("Done thread: " + startLine);
	}
	
	
}
