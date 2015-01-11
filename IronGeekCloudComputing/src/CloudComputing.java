import com.smartxls.AutoShape;
import com.smartxls.FormControlShape;
import com.smartxls.WorkBook;

public class CloudComputing {

	public static void main(String[] args) {
		
		WorkBook workBook = new WorkBook();
        try
        {
            workBook.read("IronGeekCloudInputData.csv");
            //workBook.setText(0,0,"aa");
            
//            FormControlShape comBoxShape1 = workBook.addFormControl(3.0, 3.0, 4.1, 4.1, FormControlShape.CombBox);
//            comBoxShape1.setCellRange("A1:A3");
//            comBoxShape1.setCellLink("B4");

            workBook.writeXLSX("IronGeekCloudReport.xlsx");
            
	          
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
	}

}
