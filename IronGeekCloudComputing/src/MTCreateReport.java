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
import java.util.List;
import java.util.Arrays;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.smartxls.RangeStyle;
import com.smartxls.WorkBook;

public class MTCreateReport {

//	public static void main(String[] args) {
//		List<Thread> allThread = new ArrayList<Thread>();
//		for(int i = 0; i < 10; ++ i){
//			CreateReport cr;
//			try {
//				cr = new CreateReport("IronGeekCloudInputData.csv", i * 1000 + 1, i * 1000 + 1001);
//				Thread th = new Thread(cr);
//				allThread.add(th);
//				th.start();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		for(int i = 0; i < 10; ++ i){
//			try {
//				allThread.get(i).join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		//System.out.print(Arrays.toString(CreateReport.tradedValueAll));
//		//System.out.print(Arrays.toString(CreateReport.marketCapAll));
//		//System.out.print(Arrays.toString(CreateReport.netRealizedGainOpenPriceAll));
//		System.out.print(Arrays.toString(CreateReport.netRealizedGainClosePriceAll));
//	}
	public static void f(){
		Path csvFile = Paths.get("IronGeekCloudInputData.csv");
		try {
			List<String> lines = Files.readAllLines(csvFile, Charset.defaultCharset());
			System.out.print(lines.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class CreateReport implements Runnable {
	
	String csvFile;
	int startLine;
	int endLine;
	static List<String> lines;
	static double[] tradedValueAll;
	static BigDecimal[] marketCapAll;
	static double[] netRealizedGainOpenPriceAll;
	static double[] netRealizedGainClosePriceAll;
	
	public CreateReport(String csv, int start, int end) throws IOException{
		csvFile = csv;
		startLine = start;
		endLine = end;
		if(lines == null){
			Path csvFile = Paths.get("IronGeekCloudInputData.csv");
			lines = Files.readAllLines(csvFile, Charset.defaultCharset());
			tradedValueAll = new double[lines.size()];
			marketCapAll = new BigDecimal[lines.size()];
			netRealizedGainOpenPriceAll = new double[lines.size()];
			netRealizedGainClosePriceAll = new double[lines.size()];	
		}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		
		WorkBook workBook = new WorkBook();
		HashMap<BrokerTradeDateSideKey, Double> brokerTradingSummeryMap = new HashMap<BrokerTradeDateSideKey, Double>();
		try {
			int sheet1RowCount = 0;
			int dataRowCount = startLine;
			            
            for(int i = startLine; i < endLine; ++ i){
            	//sheet1RowCount ++;
            
            	String curLine = lines.get(i);
            	String[] row = curLine.split(cvsSplitBy);
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
				tradedValueAll[i] = tradedValue;
				BigDecimal marketCap = sharesOutstanding.multiply(new BigDecimal(closePrice));
				marketCapAll[i] = marketCap;
				double netRealizedGainOpenPrice  = (side == "buy") ? (openPrice - tradePrice) * shares : (tradePrice - openPrice) * shares; // double or BigInteger?
				netRealizedGainOpenPriceAll[i] = netRealizedGainOpenPrice;
				double netRealizedGainClosePrice = (side == "buy") ? (closePrice - tradePrice) * shares : (tradePrice - closePrice) * shares; // double or BigInteger?
				netRealizedGainClosePriceAll[i] = netRealizedGainClosePrice;
				
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
            String outputFile = "report_" + startLine + "_" + endLine + ".xlsx";
			workBook.writeXLSX(outputFile);
            //workBook.writeXLSX("IronGeekCloudReport.xlsx");
            
            
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
		System.out.println("Done thread: " + startLine);
	}
	
	
}
