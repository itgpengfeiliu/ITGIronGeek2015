package itg.opt.irongeek;

import itg.opt.irongeek.util.ReadData;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SourceDataServlet
 */
@WebServlet("/SourceDataServlet")
public class SourceDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SourceDataServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		
		Set<String> filenames = getServletContext().getResourcePaths("/data/");
	    // Actual logic goes here.
	    PrintWriter out = response.getWriter();
	    for (String fn : filenames) {
	    	out.println("<a href='/CloudComputingWeb" + fn + "' target='_blank'>" + fn + "</a><br />");
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		ReadData obj = new ReadData(getServletContext().getRealPath("/data")+"/IronGeekCloudInputData.csv", getServletContext().getRealPath("/report")+"/", "report"+time+"IronGeekCloudInputData.xlsx");
		obj.run();
	}

}
