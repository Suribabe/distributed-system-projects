package task2;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jingshiqing
 */
public class ConnectMongoDB {

    //searchItemMap store the client search item
    private HashMap<String, Integer> searchItemMap = new HashMap<>();
    //agentMap store the user agent
    private HashMap<String, Integer> agentMap = new HashMap<>();
    //courseMap store the course returned from api
    private HashMap<String, Integer> courseMap = new HashMap<>();
    //max_client_request stores the top 5 search words
    private String[] max_client_request = new String[5];
    //max_client_request stores the lasted visit time
    private String latest_date = "";
    //max_client_request stores the top user agent
    private String top_user_agent = "";
    private int[] week = {0, 0, 0, 0, 0, 0, 0};
    private long latency = 0;

    /**
     * @return the client_request_array
     */
    public ArrayList getClient_request_array() {
        return client_request_array;
    }

    /**
     * @return the user_agent_array
     */
    public ArrayList getUser_agent_array() {
        return user_agent_array;
    }

    /**
     * @return the api_request_array
     */
    public ArrayList getApi_request_array() {
        return api_request_array;
    }

    /**
     * @return the date_array
     */
    public ArrayList getDate_array() {
        return date_array;
    }

    /**
     * @return the client_requesturl_array
     */
    public ArrayList getClient_requesturl_array() {
        return client_requesturl_array;
    }

    /**
     * @return the course_name_array
     */
    public ArrayList getCourse_name_array() {
        return course_name_array;
    }

    /**
     * @return the course_pic_array
     */
    public ArrayList getCourse_pic_array() {
        return course_pic_array;
    }

    /**
     * @return the api_url_array
     */
    public ArrayList getApi_url_array() {
        return api_url_array;
    }
    private String[] popular_course = new String[10];
    private String log = "";
    private ArrayList client_request_array = new ArrayList<String>();
    private ArrayList user_agent_array = new ArrayList<String>();
    private ArrayList api_request_array = new ArrayList<String>();
    private ArrayList date_array = new ArrayList<String>();
    private ArrayList client_requesturl_array = new ArrayList<String>();
    private ArrayList course_name_array = new ArrayList<String>();
    private ArrayList course_pic_array = new ArrayList<String>();
    private ArrayList api_url_array = new ArrayList<String>();


    /**
     * @return the latency
     */
    public long getLatency() {
        return latency;
    }

    /**
     * @return the top_user_agent
     */
    public String getTop_user_agent() {
        return top_user_agent;
    }

    /**
     * @return the popular_course
     */
    public String[] getPopular_course() {
        return popular_course;
    }

    /**
     * @return the latest_date
     */
    public String getLatest_date() {
        return latest_date;
    }

    /**
     * @return the max_client_request
     */
    public String[] getMax_client_request() {
        return max_client_request;
    }

    /**
     * getConnectMongo() is to connect the mongodb in mLab
     */
    public DBCollection getConnectMongo() {
        MongoClientURI uri = new MongoClientURI("mongodb://jingshiqing:jsq5417@ds139567.mlab.com:39567/dsproject4");
        MongoClient client = new MongoClient(uri);
        DB db = client.getDB(uri.getDatabase());
        DBCollection dsproject4 = db.getCollection("dsproject4");
        return dsproject4;
    }

    /**
     * storeMongo() is to store the log information in mongodb
     */
    public void storeMongo(String client_request, String user_agent, String api_reqeust, String date, String client_requesturl,
            String course_name, String course_pic, long latency) {
        DBCollection database = getConnectMongo();
        BasicDBObject document = new BasicDBObject();
        //put all the informaiton into BasicDBObject
        document.put("client_request", client_request);
        document.put("user_agent", user_agent);
        document.put("api_request", api_reqeust);
        document.put("date", date);
        document.put("client_requesturl", client_requesturl);
        document.put("course_name", course_name);
        document.put("course_pic", course_pic);
        document.put("latency", latency);
        //insert the BasicDBObject to DBCollection
        database.insert(document);
    }

    /**
     * dashborad() is to generate the dashboard
     */
    public void dashborad() {
        long sum = 0;
        int count = 0;
        DBCollection database = getConnectMongo();
        //initiate a cursor to iterate all records in DBCollection database
        DBCursor cursor = database.find();
        String date = "";
        //iterate  all records
        while (cursor.hasNext()) {
            DBObject o = cursor.next();
            String client_request = o.get("client_request").toString();
            String user_agent = o.get("user_agent").toString();
            System.out.println(user_agent);
            //generate a searchItemMap
            if (getSearchItemMap().containsKey(client_request)) {
                int index = getSearchItemMap().get(client_request);
                getSearchItemMap().put(client_request, ++index);
            } else {
                getSearchItemMap().put(client_request, 1);
            }
            getUserAgent(user_agent);
            getCourseMap(o.get("course_name").toString());
            getWeek(o.get("date").toString());
            System.out.println(o.get("date").toString());
            //calculate the total latency
            if (o.get("latency") != null) {
                sum += Long.valueOf(o.get("latency").toString());
                count++;
            }
            //add according information to according arraylist
            getClient_request_array().add(client_request);
            getUser_agent_array().add(user_agent);
            getApi_request_array().add(o.get("api_request").toString());
            getDate_array().add(o.get("date").toString());
            getClient_requesturl_array().add(o.get("client_requesturl").toString());
            getCourse_name_array().add(o.get("course_name").toString());
            getCourse_pic_array().add(o.get("course_pic").toString());
            if (o.get("latency") != null) {
                getApi_url_array().add(o.get("latency").toString());
            }
        }
        //calculate the average latency
        setLatency(sum / count);
        TreeMap<String, Integer> treemap = new TreeMap<>();
        TreeMap<String, Integer> treeAgent = new TreeMap<>();
        TreeMap<String, Integer> treeCourse = new TreeMap<>();
        //sort SearchItemMap
        treemap = sortMapByValue(getSearchItemMap());
        //sort AgentMap
        treeAgent = sortMapByValue(getAgentMap());
        //sort CourseMap
        treeCourse = sortMapByValue(getCourseMap());
        //get top 5 search item
        setMax_client_request(getTopFiveTerm(treemap));
        //get lasted data
        setLatest_date(date);
        //get top user agent
        setTop_user_agent(getTopTerm(treeAgent));
        //get top ten popular course
        setPopular_course(getTopTenTerm(treeCourse));
    }

    public int[] getWeek() {
        return week;
    }

    /**
     * @param date the date in log
     * getWeek() is to calculate the visit number each day in a week
     */
    public int[] getWeek(String date) {
        if (date.contains("Mon")) {
            getWeek()[0]++;
        } else if (date.contains("Tues")) {
            getWeek()[1]++;
        } else if (date.contains("Wed")) {
            getWeek()[2]++;
        } else if (date.contains("Thur")) {
            getWeek()[3]++;
        } else if (date.contains("Fri")) {
            getWeek()[4]++;
        } else if (date.contains("Sat")) {
            getWeek()[5]++;
        } else {
            getWeek()[6]++;
        }
        return getWeek();
    }

    /**
     * @param treemap the map
     * getTopFiveTerm() is to get Top 5 items from a map
     */
    public String[] getTopFiveTerm(TreeMap<String, Integer> treemap) {
        Iterator it = treemap.entrySet().iterator();
        for (int i = 0; i < 5; i++) {
            Map.Entry key = (Map.Entry) it.next();
            getMax_client_request()[i] = key.getKey().toString();
        }
        return getMax_client_request();
    }

    /**
     * @param treemap the map 
     * getTopTenTerm() is to get Top 10 items from a map
     */
    public String[] getTopTenTerm(TreeMap<String, Integer> treemap) {
        Iterator it = treemap.entrySet().iterator();

        for (int i = 0; i < 10; i++) {
            Map.Entry key = (Map.Entry) it.next();
            getPopular_course()[i] = key.getKey().toString();
        }
        return getPopular_course();
    }

    /**
     * @param treemap the map 
     * getTopTenTerm() is to get Top 1 item from a map
     */
    public String getTopTerm(TreeMap<String, Integer> treemap) {
        Iterator it = treemap.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1; i++) {
            Map.Entry key = (Map.Entry) it.next();
            sb.append(key.getKey());
        }
        return sb.toString();
    }

    /** 
     * getCourseMap() is to construct a coursemap
     */
    public void getCourseMap(String course_name) {
        if (getCourseMap().containsKey(course_name)) {
            int index = getCourseMap().get(course_name);
            getCourseMap().put(course_name, ++index);
        } else {
            getCourseMap().put(course_name, 1);
        }
    }

    /** 
     * getCourseMap() is to construct a user_agent map
     */
    public void getUserAgent(String user_agent) {
        String ios = "IOS";
        String blackBerry = "BlackBerry";
        String andriod = "Andriod";
        
        if (user_agent.contains("IOS")) {
            //when user_agent contains, add 1
            if (getAgentMap().containsKey(ios)) {
                int index = getAgentMap().get(ios);
                getAgentMap().put(ios, ++index);
            } else {//when does not contain, set to 1
                getAgentMap().put(ios, 1);
            }
        } else if (user_agent.contains("BlackBerry")) {
            //when user_agent contains, add 1
            if (getAgentMap().containsKey(blackBerry)) {
                int index = getAgentMap().get(blackBerry);
                getAgentMap().put(blackBerry, ++index);
            } else {//when does not contain, set to 1
                getAgentMap().put(blackBerry, 1);
            }
        } else if (user_agent.contains("Android")) {
            //when user_agent contains, add 1
            if (getAgentMap().containsKey(andriod)) {
                int index = getAgentMap().get(andriod);
                getAgentMap().put(andriod, ++index);
            } else {//when does not contain, set to 1
                getAgentMap().put(andriod, 1);
            }
        } else {
            //when user_agent contains, add 1
            if (getAgentMap().containsKey("other")) {
                int index = getAgentMap().get("other");
                getAgentMap().put("other", ++index);
            } else {//when does not contain, set to 1
                getAgentMap().put("other", 1);
            }
        }
    }

    /** 
     * sortMapByValue() is to sort the map by its value
     * Referenced from http://www.programcreek.com/2013/03/java-sort-map-by-value/
     */
    public TreeMap<String, Integer> sortMapByValue(HashMap<String, Integer> map) {
        Comparator<String> comparator = new ValueComparator(map);
        //TreeMap is a map sorted by its keys. 
        //The comparator is used to sort the TreeMap by keys. 
        TreeMap<String, Integer> result = new TreeMap<String, Integer>(comparator);
        result.putAll(map);
        return result;
    }

    /** 
     * sortMapByValue() is to redifine a compare method in comparator
     * Referenced from http://www.programcreek.com/2013/03/java-sort-map-by-value/
     */
    class ValueComparator implements Comparator<String> {

        HashMap<String, Integer> map = new HashMap<String, Integer>();

        public ValueComparator(HashMap<String, Integer> map) {
            this.map.putAll(map);
        }

        @Override
        public int compare(String s1, String s2) {
            if (map.get(s1) < map.get(s2)) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    /**
     * @return the searchItemMap
     */
    public HashMap<String, Integer> getSearchItemMap() {
        return searchItemMap;
    }

    /**
     * @param searchItemMap the searchItemMap to set
     */
    public void setSearchItemMap(HashMap<String, Integer> searchItemMap) {
        this.searchItemMap = searchItemMap;
    }

    /**
     * @return the agentMap
     */
    public HashMap<String, Integer> getAgentMap() {
        return agentMap;
    }

    /**
     * @param agentMap the agentMap to set
     */
    public void setAgentMap(HashMap<String, Integer> agentMap) {
        this.agentMap = agentMap;
    }

    /**
     * @return the courseMap
     */
    public HashMap<String, Integer> getCourseMap() {
        return courseMap;
    }

    /**
     * @param courseMap the courseMap to set
     */
    public void setCourseMap(HashMap<String, Integer> courseMap) {
        this.courseMap = courseMap;
    }

    /**
     * @param max_client_request the max_client_request to set
     */
    public void setMax_client_request(String[] max_client_request) {
        this.max_client_request = max_client_request;
    }

    /**
     * @param latest_date the latest_date to set
     */
    public void setLatest_date(String latest_date) {
        this.latest_date = latest_date;
    }

    /**
     * @param top_user_agent the top_user_agent to set
     */
    public void setTop_user_agent(String top_user_agent) {
        this.top_user_agent = top_user_agent;
    }

    /**
     * @param week the week to set
     */
    public void setWeek(int[] week) {
        this.week = week;
    }

    /**
     * @param latency the latency to set
     */
    public void setLatency(long latency) {
        this.latency = latency;
    }

    /**
     * @param popular_course the popular_course to set
     */
    public void setPopular_course(String[] popular_course) {
        this.popular_course = popular_course;
    }

    /**
     * @param log the log to set
     */
    public void setLog(String log) {
        this.log = log;
    }

    /**
     * @param client_request_array the client_request_array to set
     */
    public void setClient_request_array(ArrayList client_request_array) {
        this.client_request_array = client_request_array;
    }

    /**
     * @param user_agent_array the user_agent_array to set
     */
    public void setUser_agent_array(ArrayList user_agent_array) {
        this.user_agent_array = user_agent_array;
    }

    /**
     * @param api_request_array the api_request_array to set
     */
    public void setApi_request_array(ArrayList api_request_array) {
        this.api_request_array = api_request_array;
    }

    /**
     * @param date_array the date_array to set
     */
    public void setDate_array(ArrayList date_array) {
        this.date_array = date_array;
    }

    /**
     * @param client_requesturl_array the client_requesturl_array to set
     */
    public void setClient_requesturl_array(ArrayList client_requesturl_array) {
        this.client_requesturl_array = client_requesturl_array;
    }

    /**
     * @param course_name_array the course_name_array to set
     */
    public void setCourse_name_array(ArrayList course_name_array) {
        this.course_name_array = course_name_array;
    }

    /**
     * @param course_pic_array the course_pic_array to set
     */
    public void setCourse_pic_array(ArrayList course_pic_array) {
        this.course_pic_array = course_pic_array;
    }

    /**
     * @param api_url_array the api_url_array to set
     */
    public void setApi_url_array(ArrayList api_url_array) {
        this.api_url_array = api_url_array;
    }
}
