package services;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import connector.DatabaseConnector;

public class Services extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
    public Services() {
        super();
    }
	
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String function = request.getParameter("function");
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		PrintWriter output = response.getWriter();
		
		DatabaseConnector databaseConnector = new DatabaseConnector();
		Connection connection = databaseConnector.connect();
		
		if(connection == null){
			output.print("Error en base de datos");
		}else{
			if(function.equals("get_students")){
				output.print(getStudents(request, connection));
			}else if(function.equals("get_courses")){
				output.print(getCourses(request, connection));
			}else{
				output.print("Entra a servidor");
			}
		}
	}
	
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
    	
    	String function = request.getParameter("function");
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		PrintWriter output = response.getWriter();
		
		DatabaseConnector databaseConnector = new DatabaseConnector();
		Connection connection = databaseConnector.connect();
		
		if(function.equals("get_courses_by_student")){
			output.print(getCoursesByStudent(request, connection));
		}else if(function.equals("add_course")){
			output.print(addCourse(request, connection));
		}else if(function.equals("delete_course")){
			output.print(deleteCourse(request, connection));
		}else if(function.equals("change_score")){
			output.print(changeScore(request, connection));
		}
	}
    
    public String changeScore(HttpServletRequest request, Connection connection){
    	long studentId = Long.parseLong(request.getParameter("studentId"));
    	long courseId = Long.parseLong(request.getParameter("courseId"));
    	float newScore = Float.parseFloat(request.getParameter("newCourseScore"));
    	String resultMessage = "200";
    	
    	try{
	    	Statement statement = connection.createStatement();
			String sql = "update notas set nota='" + newScore + "' where estudianteID = " + studentId 
					+ " and cursoID = " + courseId;
			statement.execute(sql);
    	}catch (SQLException e) {
			e.printStackTrace();
			resultMessage = "500";
		}
    	
    	return resultMessage;
    }
    
    public String addCourse(HttpServletRequest request, Connection connection){
    	long studentId = Long.parseLong(request.getParameter("studentId"));
    	long courseId = Long.parseLong(request.getParameter("courseId"));
    	String resultMessage = "200";
    	
    	try{
	    	Statement statement = connection.createStatement();
			String sql = "insert into notas values (" + studentId + "," + courseId + ",0.0)";
			statement.execute(sql);
    	}catch (SQLException e) {
    		resultMessage = "500";
			e.printStackTrace();
		}
    	
    	return resultMessage;
    }
    
    public String deleteCourse(HttpServletRequest request, Connection connection){
    	long studentId = Long.parseLong(request.getParameter("studentId"));
    	long courseId = Long.parseLong(request.getParameter("courseId"));
    	String resultMessage = "200";
    	
    	try{
	    	Statement statement = connection.createStatement();
			String sql = "delete from notas where estudianteID = " + studentId +" and cursoID = " + courseId;
			statement.execute(sql);
    	}catch (SQLException e) {
			e.printStackTrace();
			resultMessage = "500";
		}
    	
    	return resultMessage;
    }
    
    public String getCourses(HttpServletRequest request, Connection connection){
    	JSONArray courseList = new JSONArray();
    	
		try {
	    	Statement statement = connection.createStatement();
			String sql = "select * from cursos";
			ResultSet resulSet = statement.executeQuery(sql);
			while(resulSet.next()) {
				JSONObject json = new JSONObject();
				json.put("code", resulSet.getString("id")) ;
				json.put("name", resulSet.getString("name")) ;
				courseList.put(json);
            }
			return courseList.toString(1);
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    public String getStudents(HttpServletRequest request, Connection connection){
    	
    	JSONArray studentList = new JSONArray();
		try {
			
	    	Statement statement = connection.createStatement();
			String sql = "select * from estudiantes";
			ResultSet resulSet = statement.executeQuery(sql);
			while(resulSet.next()) {
				JSONObject json = new JSONObject();
				json.put("identification", resulSet.getString("id")) ;
				json.put("lastName", resulSet.getString("lastname")) ;
				json.put("name", resulSet.getString("name")) ;
				studentList.put(json);
            }
			return studentList.toString(1);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
    }
	
	public String getCoursesByStudent(HttpServletRequest request, Connection connection){
		long studentId = Long.parseLong(request.getParameter("studentId"));
		JSONArray courseNoteList = new JSONArray();
		
		try {
			Statement statement = connection.createStatement();
			String sql = "select c.id, c.name, n.nota from notas as n "
					+ "inner join cursos as c on c.id = n.cursoID where estudianteID = " + studentId;
			ResultSet resulSet = statement.executeQuery(sql);
			while(resulSet.next()) {
				JSONObject json = new JSONObject();
				json.put("code", resulSet.getString("id")) ;
				json.put("name", resulSet.getString("name")) ;
				json.put("score", resulSet.getString("nota")) ;
				courseNoteList.put(json);
            }
			return courseNoteList.toString(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
