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

public class CarRentalNew extends HttpServlet {

  int cont = 0;

  public void doGet(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    
    /* Obtenemos valores que introduce el usuario */
    String model_vehicle = req.getParameter("model_vehicle");
    String sub_model_vehicle = req.getParameter("sub_model_vehicle");
    String dies_lloguer = req.getParameter("dies_lloguer");
    String num_vehicles = req.getParameter("num_vehicles");
    String descompte = req.getParameter("descompte");
    
    /* Mostramos los valores */
	out.println("<html> model_vehicle:"+ model_vehicle + "<br> sub_model_vehicle:"+
                sub_model_vehicle + "<br> dies_lloguer:"+ dies_lloguer+ "<br> num_vehicles :"+ num_vehicles + "<br> descompte:" + descompte + "</html>");
                
	
    JSONParser parser = new JSONParser();
    JSONArray list = new JSONArray(); //Sera un array de JSONObject

    /* Leemos el fichero y lo ponemos en list; en caso de ser vacío, no hace nada*/
    try (Reader reader = new FileReader("/home/alumne/apache-tomcat-9.0.5/webapps/my_webapp/WEB-INF/classes/mypackage/rentals.json")) {

        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        System.out.println(jsonObject);

        // loop array
        JSONArray msg = (JSONArray) jsonObject.get("lista");
        Iterator<String> iterator = msg.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
    } catch (IOException e) {
            e.printStackTrace();
    } catch (ParseException e) {
            e.printStackTrace();
    }

    JSONObject obj = new JSONObject();
    JSONObject objfinal = new JSONObject(); //necesario para escribir

    /* Ponemos el nuevo rental en la list */
    obj.put("model_vehicle", model_vehicle);
    obj.put("sub_model_vehicle", sub_model_vehicle);
    obj.put("dies_lloguer", dies_lloguer);
    obj.put("num_vehicles", num_vehicles);
    obj.put("descompte", descompte);

    list.add(obj);

    /* Ponemos list en el JSONObject para la escritura del fichero JSON*/
    objfinal.put("lista",list);
    

    try (FileWriter file = new FileWriter("/home/alumne/apache-tomcat-9.0.5/webapps/my_webapp/WEB-INF/classes/mypackage/rentals.json")) {
        file.write(objfinal.toJSONString());
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    System.out.print(obj);
	
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    doGet(req, res);
  }
}
