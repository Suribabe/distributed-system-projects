package task2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mongodb.DBCollection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Jingshiqing
 */
@WebServlet(urlPatterns = {"/task2"})
public class task2 extends HttpServlet {
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    ConnectMongoDB connectMongoDB = new ConnectMongoDB();

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DBCollection dbdatase = connectMongoDB.getConnectMongo();
        Date pre = new Date();
        String search = request.getParameter("searchWord");
        //format the searchWord to replace blank by %20
        String searchformat = search.replace(" ", "%20");

        URL curl = new URL("https://api.coursera.org/api/courses.v1?q=search&query=" + searchformat + "&&fields=name,photoUrl,description");
        HttpURLConnection connection = (HttpURLConnection) curl.openConnection();
        connection.connect();
        
        //if connect sucess
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK || connection.getResponseCode() != 400) {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder sb = new StringBuilder(); //append all information in sb StringBuilder
            while ((inputLine = br.readLine()) != null) {
                System.out.println(inputLine);
                sb.append(inputLine); //append all information to sb StringBuilder
            }
            br.close();
            JSONArray courses = null;
            String course_name = "", description = "";
            String photoUrl = "";
            //course_json is the json formated information ready to write in response
            StringBuilder course_json = new StringBuilder();
            try {
                JSONObject jsonObject = new JSONObject(sb.toString());
                courses = jsonObject.getJSONArray("elements");
                String name = courses.getJSONObject(1).toString();
                System.out.println(name);
                //constuct a json formated informaiton
                if (courses.length() == 0) {
                    course_json.append("");
                } else {
                    course_json.append("{\"elements\":[");
                    //randomly choose a course from the result
                    int i = new Random().nextInt(courses.length());
                    //get course name
                    course_name = courses.getJSONObject(i).getString("name");
                    //get course photo url
                    photoUrl = courses.getJSONObject(i).getString("photoUrl");
                    //get course description
                    description = courses.getJSONObject(i).getString("description");
                    //append all information
                    course_json.append("{\"name\":\"");
                    course_json.append(course_name);
                    course_json.append("\",\"description\":\"");
                    course_json.append(description);
                    course_json.append("\",\"photoUrl\":\"");
                    course_json.append(photoUrl);
                    course_json.append("\"}]}");
                }
            } catch (JSONException ex) {
                Logger.getLogger(task2.class.getName()).log(Level.SEVERE, null, ex);
            }
            String client_request = search;
            //get user-agent from client request
            String user_agent = request.getHeader("User-Agent");
            //
            String api_request = search;
            //get client_url from client request
            StringBuffer client_url = request.getRequestURL();
            Date datenow = new Date();
            String date = datenow.toString();
            //latency is the time difference before and after the search
            long latency = (datenow.getTime() - pre.getTime());
            //store all information in mongodb
            connectMongoDB.storeMongo(client_request, user_agent, api_request, date, client_url.toString(), course_name, photoUrl, latency);
            //write response
            response.getOutputStream().println(course_json.toString());
            System.out.println(course_json.toString());
        } else {

        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
