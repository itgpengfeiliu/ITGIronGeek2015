
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
	// gcloud compute copy-files CloudComputingWeb.war  instance-3:/home/lpf66fpl --zone us-central1-f 
	// gcloud compute copy-files ExplodeFile.class  instance-3:/home/lpf66fpl --zone us-central1-f
	// gcloud compute copy-files IronGeekCloudInputData.csv  instance-3:/home/lpf66fpl --zone us-central1-f
	// gcloud compute copy-files cloudcomputingapp.jar  instance-3:/home/lpf66fpl --zone us-central1-f
	
	// gcloud compute copy-files CloudComputingWeb.war  instance-group-2-yija:/home/lpf66fpl --zone us-central1-f
	// gcloud compute copy-files ExplodeFile.class  instance-group-2-yija:/home/lpf66fpl --zone us-central1-f
	// gcloud compute copy-files IronGeekCloudInputData.csv  instance-group-2-yija:/home/lpf66fpl --zone us-central1-f
	// gcloud compute copy-files cloudcomputingapp.jar  instance-group-2-yija:/home/lpf66fpl --zone us-central1-f
	// sudo java ExplodeFile
	
	// sudo java -Xmx8G -jar cloudcomputingapp.jar out_500_IronGeekCloudInputData.csv out_500
	private String csvFileName = "IronGeekCloudInputData.csv";
	private String reportPart2 = "report_part2.xlsx";
	private String reportPart1 = "report_part1.csv";
	private String tmpFolder = "";
	
	private HashMap<BrokerTradeDateSideKey, Double> brokerTradingSummeryMapAll;
	
	private HashMap<BrokerTradeDateKey, Double> brokerRankingMap;
	
	public static void main(String[] args) {
		if(args.length == 0) {
			MTCreateReportThreadPool mtCRThreadPool = new MTCreateReportThreadPool();
			mtCRThreadPool.loadFile();
		}
		else {
			MTCreateReportThreadPool mtCRThreadPool = new MTCreateReportThreadPool(args[0], args[1], "");
			mtCRThreadPool.loadFile();
		}
		
	}
	
	public MTCreateReportThreadPool(String csvFile, String reportFile, String tmpPath) {
		this.csvFileName = csvFile;
		this.reportPart2 = "report_" + reportFile + "_part2.xlsx";
		this.reportPart1 = "report_" + reportFile + "_part1.csv";
		this.tmpFolder = tmpPath;
		System.out.println("csv : " + this.csvFileName + " , report : " + this.reportPart1 +" , " + this.reportPart2 + " , tmp : " + this.tmpFolder);
	}
	
	public MTCreateReportThreadPool() {
	}
	
	public void loadFile() {
		CreateReportThread.lines = null;
		CreateReportThread.fileRows = 0;
		CreateReportThread.brokerTradingSummeryMapList = new ArrayList<HashMap<BrokerTradeDateSideKey, Double>>(10);
		
		List<Thread> allThread = new ArrayList<Thread>();
		for(int i = 0; i < 10; ++ i){
			CreateReportThread cr;
			try {
				cr = new CreateReportThread(this.csvFileName, i, this.tmpFolder); // , i * 1000 + 1, i * 1000 + 1001
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
				        
	        System.out.println("prepare to print report 1, total rows : " + CreateReportThread.fileRows);
	        
	        //FileInputStream[] fIns = new FileInputStream[10];
    	    //FileOutputStream fOut;
    	    //FileChannel[] fIChans= new FileChannel[10]; 
    	    //FileChannel fOChan;
    	    //long[] fSizes = new long[10];
	        FileInputStream fIn = null;
		    FileOutputStream fOut;
		    FileChannel fIChan = null, fOChan;
		    long fSize;
    	    MappedByteBuffer mBuf;
    	    
    	    String outFileName = tmpFolder + reportPart1;
    	    fOut = new FileOutputStream(outFileName, false);
    	    //fOut.write("");
    	    fOut.close();
	        fOut = new FileOutputStream(outFileName, true);
	        fOChan = fOut.getChannel();
    	    
    	    for(int i = 0; i < 10; ++ i){
//    	    	fIns[i] = new FileInputStream("tmp" + i + ".csv");
//    	    	fIChans[i] = fIns[i].getChannel();
//    	    	fSizes[i] = fIChans[i].size();
    	    	
    	    	fIn = new FileInputStream(tmpFolder + "tmp" + i + ".csv");
    	    	fIChan = fIn.getChannel();
    	    	fSize = fIChan.size();
    	    	
    	    	mBuf = fIChan.map(FileChannel.MapMode.READ_ONLY, 0, fSize);
    	    	fIChan.transferTo(0, fSize, fOChan);
	        }
	        

	        fIChan.close();
	        fIn.close();

	        fOChan.close();
	        fOut.close();
    	    
	        //workBook.read(tmpFolder + "tmp_all.csv");
	        
	        //workBook.setSheetName(0, "Report");
			
//	        workBook.setText(sheet1RowCount, 0, "Symbol");
//			workBook.setText(sheet1RowCount, 1, "Shares");
//			workBook.setText(sheet1RowCount, 2, "Trade Price");
//			workBook.setText(sheet1RowCount, 3, "Trade Date");
//			workBook.setText(sheet1RowCount, 4, "Shares Outstanding");
//			workBook.setText(sheet1RowCount, 5, "Open Price");
//			workBook.setText(sheet1RowCount, 6, "Close Price");
//			workBook.setText(sheet1RowCount, 7, "Avg Daily Volum");
//			workBook.setText(sheet1RowCount, 8, "Side");
//			workBook.setText(sheet1RowCount, 9, "Broker");
//			workBook.setText(sheet1RowCount, 10, "Traded Value");
//			workBook.setText(sheet1RowCount, 11, "Market Cap");
//			workBook.setText(sheet1RowCount, 12, "Net Realized Gain vs. Open Price");
//			workBook.setText(sheet1RowCount, 13, "Net Realized Gain vs. Close Price");
			
//			RangeStyle rangeStyle = workBook.getRangeStyle(0, 0, 0, 13);
//	        rangeStyle.setFontBold(true);
//	        workBook.setRangeStyle(rangeStyle, 0, 0, 0, 13);
//	        
//	        workBook.setColWidth(0, 10*256);
//	        workBook.setColWidth(1, 12*256);
//	        workBook.setColWidth(2, 15*256);
//	        workBook.setColWidth(3, 12*256);
//	        workBook.setColWidth(4, 15*256);
//	        workBook.setColWidth(5, 12*256);
//	        workBook.setColWidth(6, 12*256);
//	        workBook.setColWidth(7, 15*256);
//	        workBook.setColWidth(8, 8*256);
//	        workBook.setColWidth(9, 10*256);
//	        workBook.setColWidth(10, 15*256);
//	        workBook.setColWidth(11, 20*256);
//	        workBook.setColWidth(12, 20*256);
//	        workBook.setColWidth(13, 20*256);
//	        
//	        //move chart sheet to index 1,index are from left to right,start from 0.
            //workBook.setSheet(1);
            //workBook.moveSheet(0);
	        
//	        for (int j=0; j<CreateReportThread.fileRows; j++) {
//	        	if (j % 10000 == 0) {
//	        		System.out.println("print report 1 on row : " + j);
//	        	}
//	        	sheet1RowCount++;
//	        	workBook.setText(sheet1RowCount, 0, CreateReportThread.symbol[j]);
//				workBook.setNumber(sheet1RowCount, 1, CreateReportThread.shares[j]);
//				workBook.setNumber(sheet1RowCount, 2, CreateReportThread.tradePrice[j]);
//				workBook.setText(sheet1RowCount, 3, CreateReportThread.tradeDateStr[j]);
//				workBook.setNumber(sheet1RowCount, 4, CreateReportThread.sharesOutstanding[j].longValue());
//				workBook.setNumber(sheet1RowCount, 5, CreateReportThread.openPrice[j]);
//				workBook.setNumber(sheet1RowCount, 7, CreateReportThread.avgDailyVolum[j].longValue());
//				workBook.setText(sheet1RowCount, 8, CreateReportThread.side[j]);
//				workBook.setText(sheet1RowCount, 9, CreateReportThread.broker[j]);
//				workBook.setNumber(sheet1RowCount, 10, CreateReportThread.tradedValueAll[j]);
//				workBook.setNumber(sheet1RowCount, 11, CreateReportThread.marketCapAll[j].doubleValue());
//				workBook.setNumber(sheet1RowCount, 12, CreateReportThread.netRealizedGainOpenPriceAll[j]);
//				workBook.setNumber(sheet1RowCount, 13, CreateReportThread.netRealizedGainClosePriceAll[j]);
//	        }
	        //System.out.println("report 1 print done");
	        
	        
	        brokerTradingSummeryMapAll = new HashMap<BrokerTradeDateSideKey, Double>();
	        
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
	        System.out.println("prepare to print report 2, broker trading summary report, total rows : " + brokerTradingSummeryMapAll.size());
	        
	        int sheet2Count = 0;
			workBook.insertSheets(0, 1);
			workBook.setSheetName(0, "aggregate report");
			
			workBook.setText(sheet2Count, 0, "Broker Trading Summary");
			sheet2Count++;
			
			workBook.setText(sheet2Count, 0, "Broker");
			workBook.setText(sheet2Count, 1, "Trade Date");
			workBook.setText(sheet2Count, 2, "Side");
			workBook.setText(sheet2Count, 3, "Total Traded Value (in Millions)");
			
			RangeStyle rangeStyle = workBook.getRangeStyle(0, 0, 1, 3);
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
		        workBook.setNumber(sheet2Count, 3, (Double) pairs.getValue() / 1000000);
		        //it.remove(); // avoids a ConcurrentModificationException
		    }
		    System.out.println("broker trading summary report done");
	        
		    brokerRankingMap = new HashMap<BrokerTradeDateKey, Double>();
		    Iterator itMT = brokerTradingSummeryMapAll.entrySet().iterator();
		    while (itMT.hasNext()) {
		    	Map.Entry pairs = (Map.Entry)itMT.next();
		        BrokerTradeDateSideKey tmpKey = (BrokerTradeDateSideKey) pairs.getKey();
		        BrokerTradeDateKey rKey = new BrokerTradeDateKey(tmpKey.getBroker(), tmpKey.getTradeDate());
		        //System.out.println("... " + tmpKey.getBroker() + " , " + tmpKey.getSide());
		        double tmpValue = 0;
		        if (tmpKey.getSide().equalsIgnoreCase("buy")) {
		        	tmpValue = (double) brokerTradingSummeryMapAll.get(tmpKey);
		        }
		        else {
		        	tmpValue = 0 - (double) brokerTradingSummeryMapAll.get(tmpKey);
		        }
		        //System.out.println("value =  " + tmpValue);
				if (brokerRankingMap.containsKey(rKey)) {
					double sumTradedValue = (double) brokerRankingMap.get(rKey);
					sumTradedValue += tmpValue;
					brokerRankingMap.put(rKey, sumTradedValue);
					//System.out.println("update :  " + rKey.getBroker());
				}
				else {
					brokerRankingMap.put(rKey, tmpValue);
					//System.out.println("add :  " + rKey.getBroker());
				}
		    }
		    
		    HashMap<BrokerTradeDateKey, Double> sortedMap = sortByValues(brokerRankingMap); 
		    
		    System.out.println("prepare to print report 3, broker trading ranking report, total rows : " + brokerRankingMap.size());
	        
		    int sheet3Count = 0;
			workBook.insertSheets(1, 1);
			workBook.setSheetName(1, "ranking report");
			
			workBook.setText(sheet3Count, 0, "Broker Ranking Report");
			sheet3Count++;
			
			workBook.setText(sheet3Count, 0, "Broker");
			workBook.setText(sheet3Count, 1, "Trade Date");
			workBook.setText(sheet3Count, 2, "Total Realized Gain (in Millions)");
			
			rangeStyle = workBook.getRangeStyle(0, 0, 1, 2);
            rangeStyle.setFontBold(true);
            workBook.setRangeStyle(rangeStyle, 0, 0, 1, 2);
            
            workBook.setColWidth(0, 15*256);
            workBook.setColWidth(1, 15*256);
            workBook.setColWidth(2, 20*256);
			
			//DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			
			Iterator itR = sortedMap.entrySet().iterator();
		    while (itR.hasNext()) {
		    	sheet3Count++;
		    	
		        Map.Entry pairs = (Map.Entry)itR.next();
		        BrokerTradeDateKey btdKey = (BrokerTradeDateKey) pairs.getKey();
		        workBook.setText(sheet3Count, 0,  btdKey.getBroker());
		        workBook.setText(sheet3Count, 1,  format.format(btdKey.getTradeDate()));
		        workBook.setNumber(sheet3Count, 2, (Double) pairs.getValue() / 1000000);
		        //it.remove(); // avoids a ConcurrentModificationException
		    }
		    
		    System.out.println("report 3, broker trading ranking report, done ");
	        
		    System.out.println("prepare to print report 3 chart");
	        
		    //workBook.insertSheets(1, 1);
			//workBook.setSheetName(1, "ranking report chart");
			
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
            chart.setLinkRange("ranking report!$A$3:$C$"+sheet3Count,false);
            //chart.setYAxisCount(arg0);
            //set axis title
            chart.setAxisTitle(ChartShape.XAxis, 0, "X-axis Broker");
			chart.setAxisTitle(ChartShape.YAxis, 0, "Y-axis Total Traded Value");
            //set series name
			//chart.setSeriesName(0, "My Series number 1");
			//chart.setSeriesName(1, "My Series number 2");
			//chart.setSeriesName(2, "My Series number 3");
			chart.setTitle("Chart");
            //set chart type to 3D
            //chart.set3Dimensional(true);

            
            System.out.println("print report 3 chart done");
		    
			workBook.writeXLSX(reportPart2);
			
			System.out.println("All done");
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static HashMap sortByValues(HashMap map) { 
	       List list = new LinkedList(map.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o2)).getValue())
	                  .compareTo(((Map.Entry) (o1)).getValue());
	            }
	       });
	       
	       // Here I am copying the sorted list in HashMap
	       // using LinkedHashMap to preserve the insertion order
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	  }
}

class CreateReportThread implements Runnable {
	
	private String csvFile;
	private int startLine;
	private int endLine;
	private int id;
	private String tmpReportFilename;
	
	static String[] lines;
	static int fileRows;
	
	//static 
	//String[] symbol;
	int[] shares;
	double[] tradePrice;
	Date[] tradeDate;
	//String[] tradeDateStr;
	BigDecimal[] sharesOutstanding;
	double[] openPrice;
	double[] closePrice;
	//BigInteger[] avgDailyVolum;
	String[] side;
	//String[] broker;
	
	double[] tradedValueAll;
	BigDecimal[] marketCapAll;
	double[] netRealizedGainOpenPriceAll;
	double[] netRealizedGainClosePriceAll;
	
	static ArrayList<HashMap<BrokerTradeDateSideKey, Double>> brokerTradingSummeryMapList;
	
	public CreateReportThread(String csv, int tid, String tmpFolder) throws IOException{ // , int start, int end
		this.csvFile = csv;
		this.id = tid;
		this.tmpReportFilename = tmpFolder + "tmp" + id + ".csv";
		//startLine = start;
		//endLine = end;
		brokerTradingSummeryMapList.add(id, new HashMap<BrokerTradeDateSideKey, Double>());
		
		if(lines == null){
			System.out.println("if lines==null, filename : " + this.csvFile);
			Path csvFilePath = Paths.get(this.csvFile);
			ArrayList<String> templines = (ArrayList<String>) Files.readAllLines(csvFilePath, Charset.defaultCharset());
			lines = new String[templines.size()];
			templines.toArray(lines);
			
			fileRows = templines.size() - 1;
			System.out.println("total lines : " + fileRows);
			
//			symbol = new String[fileRows];
//			shares = new int[fileRows];
//			tradePrice = new double[fileRows];
//			tradeDate = new Date[fileRows];
//			tradeDateStr = new String[fileRows];
//			sharesOutstanding = new BigDecimal[fileRows];
//			openPrice = new double[fileRows];
//			closePrice = new double[fileRows];
//			avgDailyVolum = new BigInteger[fileRows];
//			side = new String[fileRows];
//			broker = new String[fileRows];
//			
//			tradedValueAll = new double[fileRows];
//			marketCapAll = new BigDecimal[fileRows];
//			netRealizedGainOpenPriceAll = new double[fileRows];
//			netRealizedGainClosePriceAll = new double[fileRows];	
		}
		
		int reminder = fileRows % 10;
		int ave = (fileRows - reminder) / 10;
		
		startLine = id * ave + 1;
		endLine = id * ave + ave;
		
		if (id == 9) {
			endLine += reminder;
		}
		System.out.println("from  " + startLine + " to " + endLine);
		
		//symbol = new String[endLine-startLine];
		shares = new int[endLine-startLine+1];
		tradePrice = new double[endLine-startLine+1];
		tradeDate = new Date[endLine-startLine+1];
		//tradeDateStr = new String[endLine-startLine];
		sharesOutstanding = new BigDecimal[endLine-startLine+1];
		openPrice = new double[endLine-startLine+1];
		closePrice = new double[endLine-startLine+1];
		//avgDailyVolum = new BigInteger[endLine-startLine];
		side = new String[endLine-startLine+1];
		//broker = new String[endLine-startLine];
		
		tradedValueAll = new double[endLine-startLine+1];
		marketCapAll = new BigDecimal[endLine-startLine+1];
		netRealizedGainOpenPriceAll = new double[endLine-startLine+1];
		netRealizedGainClosePriceAll = new double[endLine-startLine+1];	
	}

	@Override
	public void run() {
		BufferedReader br = null;
		//String line = "";
		String cvsSplitBy = ",";
		
		
		try {
			FileWriter writer = new FileWriter(this.tmpReportFilename, false);
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			            
			for(int i = startLine; i <= endLine; ++ i){
            	            
            	String curLine = lines[i];
            	String[] row = curLine.split(cvsSplitBy);
            	
            	//symbol[i-1] = row[0];
				shares[i-startLine] = Integer.parseInt(row[1]);
				tradePrice[i-startLine] = Double.valueOf(row[2]);
				tradeDate[i-startLine] = format.parse(row[3]);
				//tradeDateStr[i-1] = row[3];
				sharesOutstanding[i-startLine] = new BigDecimal(row[4]); // why not integer?
				openPrice[i-startLine] = Double.valueOf(row[5]);
				closePrice[i-startLine] = Double.valueOf(row[6]);
				//avgDailyVolum[i-startLine] = new BigInteger("0");
				//try {
				//	avgDailyVolum[i-startLine] = new BigInteger(row[7]);
				//}
			    //catch( Exception e ) {
			    //    
			    //}
				side[i-startLine] = row[8];
				//broker[i-1] = row[9];
				
				double tradedValue = shares[i-startLine] * tradePrice[i-startLine]; // double or BigInteger?
				tradedValueAll[i-startLine] = tradedValue;
				BigDecimal marketCap = sharesOutstanding[i-startLine].multiply(new BigDecimal(closePrice[i-startLine]));
				marketCapAll[i-startLine] = marketCap;
				double netRealizedGainOpenPrice  = (side[i-startLine] == "buy") ? (openPrice[i-startLine] - tradePrice[i-startLine]) * shares[i-startLine] : (tradePrice[i-startLine] - openPrice[i-startLine]) * shares[i-startLine]; // double or BigInteger?
				netRealizedGainOpenPriceAll[i-startLine] = netRealizedGainOpenPrice;
				double netRealizedGainClosePrice = (side[i-startLine] == "buy") ? (closePrice[i-startLine] - tradePrice[i-startLine]) * shares[i-startLine] : (tradePrice[i-startLine] - closePrice[i-startLine]) * shares[i-startLine]; // double or BigInteger?
				netRealizedGainClosePriceAll[i-startLine] = netRealizedGainClosePrice;
				
				BrokerTradeDateSideKey btdsKey = new BrokerTradeDateSideKey(row[9], tradeDate[i-startLine], side[i-startLine]);
				if (brokerTradingSummeryMapList.get(id).containsKey(btdsKey)) {
					double sumTradedValue = (double) brokerTradingSummeryMapList.get(id).get(btdsKey);
					sumTradedValue += tradedValue;
					//brokerTradingSummeryMap.put(btdsKey, brokerTradingSummeryMap.get(brokerTradingSummeryMap) + tradedValue);
					brokerTradingSummeryMapList.get(id).put(btdsKey, sumTradedValue);
				}
				else {
					brokerTradingSummeryMapList.get(id).put(btdsKey, tradedValue);
				}
				
				if (i == 1) {
					writer.append("Symbol");
					writer.append(',');
					writer.append("Shares");
					writer.append(',');
					writer.append("Trade Price");
					writer.append(',');
					writer.append("Trade Date");
					writer.append(',');
					writer.append("Shares Outstanding");
					writer.append(',');
					writer.append("Open Price");
					writer.append(',');
					writer.append("Close Price");
					writer.append(',');
					writer.append("Avg Daily Volum");
					writer.append(',');
					writer.append("Side");
					writer.append(',');
					writer.append("Broker");
					writer.append(',');
					writer.append("Traded Value");
					writer.append(',');
					writer.append("Market Cap");
					writer.append(',');
					writer.append("Net Realized Gain vs. Open Price");
					writer.append(',');
					writer.append("Net Realized Gain vs. Close Price");
					writer.append('\n');
            	}
				
				writer.append(row[0]);
			    writer.append(',');
			    writer.append(row[1]);
			    writer.append(',');
			    writer.append(row[2]);
			    writer.append(',');
			    writer.append(row[3]);
			    writer.append(',');
			    writer.append(row[4]);
			    writer.append(',');
			    writer.append(row[5]);
			    writer.append(',');
			    writer.append(row[6]);
			    writer.append(',');
			    writer.append(row[7]);
			    writer.append(',');
			    writer.append(row[8]);
			    writer.append(',');
			    writer.append("" + tradedValueAll[i-startLine]);
			    writer.append(',');
			    writer.append("" + marketCapAll[i-startLine].doubleValue());
			    writer.append(',');
			    writer.append("" + netRealizedGainOpenPriceAll[i-startLine]);
			    writer.append(',');
			    writer.append("" + netRealizedGainClosePriceAll[i-startLine]);
			    writer.append('\n');
				//System.out.println("thread : " + id + " prepare to print report 1");

            }
            

			writer.flush();
		    writer.close();
			System.out.println("thread : " + id + " print report 1 done ");
			
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
