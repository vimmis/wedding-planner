package takeone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Photographer extends HttpServlet{

	public List photographers(HttpServletRequest request){
		List photos = new ArrayList();
		HttpSession session = request.getSession();
		String w_date = session.getAttribute("wedding_date").toString();
		PreparedStatement pstatement = null;
		ResultSet rs0;
		
		DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
		String dateInString = w_date;
		Date date = null;
		java.sql.Date sqlDate = null;

		try {
			if (dateInString != null) {
				date = formatter.parse(dateInString);
				sqlDate = new java.sql.Date(date.getTime());
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Wedding_Planner?useSSL=false",
					"root", "anu123");
			
			String query0 = ("select * from photographer where ph_id not in (select ph_id from wedding_component wc inner join wedding wed on wc.w_id = wed.w_id where w_date= '"
					+ sqlDate +"')");
			pstatement = con.prepareStatement(query0);
			rs0 = pstatement.executeQuery();
			while(rs0.next())
			{
				photos.add(rs0.getInt("ph_id"));
				photos.add(rs0.getString("web_address"));
				photos.add(rs0.getString("mgr_name"));
				photos.add(rs0.getString("mgr_email_id"));
				photos.add(rs0.getInt("cost"));
				photos.add(rs0.getInt("contact_no"));
			}
			
			con.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
		return photos;
	}

public boolean updatePhotographer(HttpServletRequest request){
	HttpSession session = request.getSession();
	String value = request.getParameter("optradio").toString();
	//String value = session.getAttribute("optradio").toString();
	boolean status = false;
	PreparedStatement pstatement = null;
	ResultSet rs0;
	String wc_id = session.getAttribute("wc_id").toString(); 
	
	
	try {
		Class.forName("com.mysql.jdbc.Driver");

		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Wedding_Planner?useSSL=false",
				"root", "anu123");
		
		String query0 = ("update wedding_component set ph_id ="+ value + " where wc_id = '"+ wc_id+"'" );
		pstatement = con.prepareStatement(query0);
		int insert = pstatement.executeUpdate();
		
		if(insert!=0)
			status = true;
		
		
		con.close();
	}
	catch(Exception ex){
		ex.printStackTrace();
	}
	return status;
	}
	
}
