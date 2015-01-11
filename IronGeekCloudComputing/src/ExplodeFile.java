import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ExplodeFile {
	
//	public static void main(String[] args) {
//		//explode("IronGeekCloudInputData.csv", 10);
//		//explode("IronGeekCloudInputData.csv", 50);
//		//explode("IronGeekCloudInputData.csv", 100);
//		//explode("IronGeekCloudInputData.csv", 500);
//		//explode("IronGeekCloudInputData.csv", 1000);
//		//explode("IronGeekCloudReport.xlsx", 10);
//
//	}
	public static void explode(String inputfile, int times){
		FileInputStream fIn;
	    FileOutputStream fOut;
	    FileChannel fIChan, fOChan;
	    long fSize;
	    MappedByteBuffer mBuf;
	    try {
	        fIn = new FileInputStream(inputfile);
	        String outFileName = "out_" + times + "_" + inputfile;
	        fOut = new FileOutputStream(outFileName, true);

	        fIChan = fIn.getChannel();
	        fOChan = fOut.getChannel();

	        fSize = fIChan.size();

	        mBuf = fIChan.map(FileChannel.MapMode.READ_ONLY, 0, fSize);
	        fIChan.transferTo(0, fSize, fOChan);
	        for(int i = 1; i < times; ++ i){
	        	fIChan.transferTo(109, fSize, fOChan);
	        }

	        fIChan.close();
	        fIn.close();

	        fOChan.close();
	        fOut.close();
	        System.out.println("Done");
	      } catch (IOException exc) {
	        System.out.println(exc);
	        System.exit(1);
	      } catch (ArrayIndexOutOfBoundsException exc) {
	        System.out.println("Usage: Copy from to");
	        System.exit(1);
	      }
	}
}
