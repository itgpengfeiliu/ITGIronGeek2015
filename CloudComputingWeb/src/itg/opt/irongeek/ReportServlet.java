package itg.opt.irongeek;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import itg.opt.irongeek.util.FileProcessor;
import itg.opt.irongeek.util.MTCreateReportThreadPool;
import itg.opt.irongeek.util.ReadData;

/**
 * Servlet implementation class Report
 */
@MultipartConfig
public class ReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		Set<String> filenames = getServletContext().getResourcePaths("/report/");
	    // Actual logic goes here.
	    PrintWriter out = response.getWriter();
	    for (String fn : filenames) {
	    	out.println("<a href='/CloudComputingWeb" + fn + "' target='_blank'>" + fn + "</a><br />");
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
	    Part filePart = request.getPart("csvFile"); // Retrieves <input type="file" name="file">
	    String fileName = getFileName(filePart);
	    String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	    String newFileName = time + fileName;
	    //System.out.println("Done : " + fileName);
	    InputStream fileContent = filePart.getInputStream();
	    // ... (do your job here)
	    FileProcessor fileProcessor = new FileProcessor();
	    if (fileContent != null) {
	    	System.out.println("path = " + getServletContext().getRealPath("/data"));
            //if ( fileProcessor.writeToWebapp(context.getRealPath("/classroom_materials")+"/", lessionPlanInputStream, lessionPlanDetail) ) {
            if (fileProcessor.writeToFile(getServletContext().getRealPath("/data")+"/", fileContent, newFileName)) {
            	//ReadData obj = new ReadData(getServletContext().getRealPath("/data")+"/"+newFileName, getServletContext().getRealPath("/report")+"/", "report"+newFileName+".xlsx");
        		//obj.run();
            	MTCreateReportThreadPool mtCRThreadPool = new MTCreateReportThreadPool(
            			getServletContext().getRealPath("/data")+"/"+newFileName, getServletContext().getRealPath("/report")+"/", "report"+time+".xlsx");
            	mtCRThreadPool.loadFile();
            	
            }
        }
	}

	private static String getFileName(Part part) {
	    for (String cd : part.getHeader("content-disposition").split(";")) {
	        if (cd.trim().startsWith("filename")) {
	            String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
	            return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
	        }
	    }
	    return null;
	}
}
