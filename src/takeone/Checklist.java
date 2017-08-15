package takeone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Checklist extends HttpServlet{
	public boolean createChecklist(HttpServletRequest request){
		HttpSession session = request.getSession();
		boolean success = false;
		String w_id = request.getParameter("id").toString();
		if(request.getParameter("checklist_name")!=null)
		{
		String checklist_name = request.getParameter("checklist_name").toString();
		String note = request.getParameter("note").toString();
		PreparedStatement pstatement = null;
		ResultSet rs0 = null;
		int cl_id = 1;
		int insert =0;
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Wedding_Planner?useSSL=false",
					"root", "anu123");
			
			String query0 = ("select max(cl_id) from checklist where w_id ="+w_id);
			pstatement = con.prepareStatement(query0);
			rs0 = pstatement.executeQuery();
			if(rs0!=null && rs0.next()){
				cl_id = (rs0.getInt(1)+1);
			}
			else 
				cl_id = 1;
			
			String queryString = ("insert into checklist(cl_id, w_id, checklist_name, submit_date, status, note) VALUES (?, ?, ?, CURDATE(),?,?)");
			pstatement = con.prepareStatement(queryString);
			pstatement.setInt(1, cl_id);
			pstatement.setString(2, w_id);
			pstatement.setString(3, checklist_name);
			pstatement.setString(4, "valid");
			pstatement.setString(5, note);
			insert = pstatement.executeUpdate();
			
			if(insert!=0)
				success = true;

			request.setAttribute("cl-id", cl_id);
				
			

		con.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		}
		return success;
	}
	
	public List retrieve(HttpServletRequest request){
		List records = new ArrayList();
		String w_id = request.getParameter("id").toString();
		PreparedStatement pstatement = null;
		ResultSet rs0 = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Wedding_Planner?useSSL=false",
					"root", "anu123");
			
			String query = ("select * from checklist where w_id = "+ w_id);
			pstatement = con.prepareStatement(query);
			rs0 = pstatement.executeQuery();
			while(rs0.next()){
				records.add(rs0.getInt("cl_id"));
				records.add(rs0.getInt("w_id"));
				records.add(rs0.getString("checklist_name"));
				records.add(rs0.getDate("submit_date"));
				records.add(rs0.getString("status"));
				records.add(rs0.getString("note"));
			}
			
			if(con!=null)
				con.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			return records;
	}

}
