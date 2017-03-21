package sjing.cmu.edu.project4android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import android.util.Base64;

/**
 * Created by Jingshiqing on 11/8/16.
 */

public class GetCourse {
    MainActivity ma = null;

    public void search(String searchTerm, MainActivity ma) {
        this.ma = ma;
        new AsyncCourseSearch().execute(searchTerm);
    }

    public class AsyncCourseSearch extends AsyncTask<String, Void, String[]> {
        //HashMap<String, Bitmap> hashMap = new HashMap<>();
        String[] result = new String[3];
        protected String[] doInBackground(String... urls) {
            try {
                return searchCourse(urls[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return null; // so compiler does not complain
            }

        }

        protected void onPostExecute(String[] result ) {
            ma.pictureReady(result);
        }

        private String[] searchCourse(String searchTerm) throws JSONException, IOException{
            //initiate a jsonarry named courses
            JSONArray courses = null;
            System.out.println("fjdksjklsa");
            //http://10.0.0.212:8080/Project4Task2/
            JSONObject jsonObject = getRemoteURL("https://sjingproject4task2.herokuapp.com/task2?searchWord="+searchTerm);
            //get json array
            courses = jsonObject.getJSONArray("elements");
            //get name
            String name = courses.getJSONObject(0).getString("name");
            //get picture url
            String pictureURL = courses.getJSONObject(0).getString("photoUrl");
            //get discription
            String description="";
            //if discription's length > 200, cut it
            if(courses.getJSONObject(0).getString("description").length()>200) {
                description = courses.getJSONObject(0).getString("description").substring(0, 200)+"...";
            }else {
                description = courses.getJSONObject(0).getString("description");
            }
            try {
                return getRemoteImage(pictureURL,name,description);
            } catch (Exception e) {
                e.printStackTrace();
                return null;// so compiler does not complain
            }
        }

        /**
         * getRemoteURL() is to generate json object based on url
         */
        private JSONObject getRemoteURL(String url) throws IOException{
            StringBuilder sb = new StringBuilder();
            JSONObject jsonObject = null;
            InputStream stream = null;
            URL curl = new URL(url);
            URLConnection connection = curl.openConnection();
            String inputLine="";
            try {
                //create a httpConnection
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                //set request method to GET
                httpConnection.setRequestMethod("GET");

                httpConnection.connect();
                //if successfully connected
                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(stream));
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
                stream.close();
                br.close();
                //construct jsonObject based on the StringBuilder
                jsonObject = new JSONObject(sb.toString());
            } catch (Exception e) {
                System.out.print("Yikes, hit the error: " + e);
                return null;
            }
            return jsonObject;
        }

        /**
         * Given a URL referring to an image, return a bitmap of that image
         */
        private String[] getRemoteImage(String picture, String courseName, String description) {
            try {
                //url is the pircture url
                URL url = new URL(picture);
                final URLConnection conn = url.openConnection();
                conn.connect();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                //bm is the bitmap created by BufferedInputStream bis
                Bitmap bm = BitmapFactory.decodeStream(bis);
                bis.close();
                //put coursename into result
                result[0] = courseName;
                //put bitmap into result
                result[1] = BitMapToString(bm);
                //put description into result
                result[2] = description;
            } catch (IOException e) {
                e.printStackTrace();
            }
                return result;
        }

        /**
         * @param bitmap the bitmap
         * referenced from http://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
         */
        public String BitMapToString(Bitmap bitmap) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        }
    }
}
