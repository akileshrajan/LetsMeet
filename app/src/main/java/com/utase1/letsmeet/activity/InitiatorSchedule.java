package com.utase1.letsmeet.activity;

/**
 * Created by akilesh on 10/16/2015.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.utase1.letsmeet.app.AppConfig;
import com.utase1.letsmeet.helper.SQLiteHandler;
import com.utase1.letsmeet.R;
import com.utase1.letsmeet.Model.scheduleModel;
import com.utase1.letsmeet.helper.CustomScheduleAdapter;





/**
 * Created by hp1 on 21-01-2015.
 */
public class InitiatorSchedule extends Fragment {

    private AsyncDataClass asyncRequestObject;
    private List<scheduleModel> schList = new ArrayList<scheduleModel>();
    private ListView vSchedule;
    private CustomScheduleAdapter adapter;
    private SQLiteHandler db;
    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_initiator_schedule,container,false);

        // SqLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();
        email = user.get("email");
        asyncRequestObject = new AsyncDataClass();
        asyncRequestObject.execute(AppConfig.URL_GETMEETING, email);

        ListView lstView = (ListView) getActivity().findViewById(R.id.lstSchedule);


        return v;

    }


    private class AsyncDataClass extends AsyncTask<String, Void, String> {

        @Override

        protected String doInBackground(String... params) {



            String jsonResult = "";

            try {

                Map<String,String> nameValuePairs = new HashMap<String,String>();

                nameValuePairs.put("email", params[1]);

                URL url = new URL(AppConfig.URL_GETMEETING);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(getQuery(nameValuePairs));
                writer.flush();
                writer.close();
                con.connect();

                InputStreamReader in = new InputStreamReader(con.getInputStream());
                jsonResult = inputStreamToString(in).toString();
                //System.out.print(jsonResult);


            }catch (IOException io){
                Log.e("IOexcep", "exp", io);
            }catch (Exception e) {
                e.printStackTrace();
                Log.e("MYAPP", "exception", e);
            }

            return jsonResult;

        }

        private String getQuery(Map<String,String> nameValuePairs) throws UnsupportedEncodingException
        {
            StringBuilder result = new StringBuilder( );
            boolean first = true;
            for(Map.Entry<String,String> pair : nameValuePairs.entrySet()){
                if(first)
                    first =  false;
                else result.append("&");
                result.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(),"UTF-8"));
            }
            return  result.toString();
        }


        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            //Toast.makeText(Login.this,result, Toast.LENGTH_LONG).show();

            System.out.println("Initiator Sch Resulted Value: " + result);

            if(result.equals("") || result == null){

                Toast.makeText(getActivity(), "Server connection failed", Toast.LENGTH_LONG).show();

                return;

            }


            int success = returnSuccess(result);

            if(success == 0){

                Toast.makeText(getActivity(), "Post cannot be posted", Toast.LENGTH_LONG).show();

                return;

            }

            if(success == 1){
                List<scheduleModel> lst = returnParsedJsonObject(result);
                vSchedule = (ListView) getActivity().findViewById(R.id.lstSchedule);
                CustomScheduleAdapter cta = new CustomScheduleAdapter(getActivity(), R.layout.myschedule_rows,lst);
                vSchedule.setAdapter(cta);
                cta.notifyDataSetChanged();



                //MyCustomBaseAdapter adpt = new MyCustomBaseAdapter(getApplicationContext(),R.layout.events_view,lst);
                //mListView.setAdapter(adpt);


                //Intent intent = new Intent(getBaseContext(), GameEvents.class);
                //startActivity(intent);
                //asyncRequestObject.cancel(true);

            }


        }

        private StringBuilder inputStreamToString(InputStreamReader is) {

            String rLine = "";

            StringBuilder answer = new StringBuilder();

            BufferedReader br = new BufferedReader(is);

            try {

                while ((rLine = br.readLine()) != null) {

                    answer.append(rLine);

                }

            } catch (IOException e) {

                // TODO Auto-generated catch block

                e.printStackTrace();

            }

            return answer;

        }

    }



    private List<scheduleModel> returnParsedJsonObject(String result){

        JSONObject resultObject = null;
        JSONArray data = null;
        List<scheduleModel> schModelList = new ArrayList<>();



        try {

            resultObject = new JSONObject(result);
            data = resultObject.getJSONArray("results");



            for(int i = 0;i< data.length();i++){
                scheduleModel sch = new scheduleModel();
                JSONObject obj = data.getJSONObject(i);
                sch.setMeetId(obj.getString("unique_id"));
                sch.setMeetname(obj.getString("meeting_name"));
                sch.setLocation(obj.getString("meeting_location"));
                sch.setDate(obj.getString("meeting_date"));
                sch.setParticipants(obj.getString("meeting_participants"));
                sch.setTimefrom(obj.getString("meeting_timefrom"));
                sch.setTimeto(obj.getString("meeting_timeto"));
                schModelList.add(sch);

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }

        return schModelList;

    }

    private int returnSuccess(String result){
        JSONObject successObject = null;

        int returnedResult = 0;

        try {

            successObject = new JSONObject(result);

            returnedResult = successObject.getInt("success");

        } catch (JSONException e) {

            e.printStackTrace();

        }

        return returnedResult;

    }

    public static class flag{
        public static boolean FIRST_START = true;
    }
}