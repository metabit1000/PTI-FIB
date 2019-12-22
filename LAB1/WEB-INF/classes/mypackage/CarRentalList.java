package mypackage;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class CarRentalList extends HttpServlet {

  int cont = 0;

  public void doGet(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();

    JSONParser parser = new JSONParser();
    String user = req.getParameter("userid");
	String pass = req.getParameter("password");
	
	if (!user.equals("user") || !pass.equals("pass")) out.println("<html>Incorrecte. Vuelva a intentarlo.</html>");
    else {
    	try (Reader reader = new FileReader("/home/alumne/apache-tomcat-9.0.5/webapps/my_webapp/WEB-INF/classes/mypackage/rentals.json")) {

	        JSONObject jsonObject = (JSONObject) parser.parse(reader);
	        System.out.println(jsonObject);

	        // loop array
	        JSONArray msg = (JSONArray) jsonObject.get("lista");
	        
	        Iterator<JSONObject> iteratorModel = msg.iterator();
	        Iterator<JSONObject> iteratorSub = msg.iterator();
	        Iterator<JSONObject> iteratorDies = msg.iterator();
	        Iterator<JSONObject> iteratorVehicles = msg.iterator();
	        Iterator<JSONObject> iteratorDes = msg.iterator();
	        //out.println("<html>");
	        while (iteratorModel.hasNext()) {
	            out.println("model_vehicle:"+ iteratorModel.next().get("model_vehicle") + "<br> sub_model_vehicle:"+ iteratorSub.next().get("sub_model_vehicle") 
					+ "<br> dies_lloguer:"+ iteratorDies.next().get("dies_lloguer") + "<br> num_vehicles:" + iteratorVehicles.next().get("num_vehicles") + "<br> descompte:" 
					+ iteratorDes.next().get("descompte")+"<br><br>");
	        }
	        //out.println("</html>");
	    } catch (IOException e) {
	            e.printStackTrace();
	    } catch (ParseException e) {
	            e.printStackTrace();
	    }
    }
    
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    doGet(req, res);
  }
}
