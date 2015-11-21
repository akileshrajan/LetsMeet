package com.utase1.letsmeet.activity;

/**
 * Created by akilesh on 10/16/2015.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

import com.utase1.letsmeet.Model.AcceptedEventModel;
import com.utase1.letsmeet.Model.AcceptedModel;
import com.utase1.letsmeet.app.AppConfig;
import com.utase1.letsmeet.helper.CustomAcceptedAdapter;
import com.utase1.letsmeet.helper.CustomAcceptedEventAdapter;
import com.utase1.letsmeet.helper.SQLiteHandler;
import com.utase1.letsmeet.R;
import com.utase1.letsmeet.Model.scheduleModel;
import com.utase1.letsmeet.helper.CustomScheduleAdapter;


public class AcceptedTab extends Fragment {

    private AsyncDataClass asyncRequestObject;
    private List<scheduleModel> schList = new ArrayList<scheduleModel>();
    private ListView meetAccepted;
    private ListView eventAccepted;
    private CustomAcceptedAdapter adapter;
    private SQLiteHandler db;
    private String email;
    private String uid;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_accepted_tab,container,false);

        // SqLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();
        email = user.get("email");
        uid = user.get("uid");

        asyncRequestObject = new AsyncDataClass();
        asyncRequestObject.execute(AppConfig.URL_GETACCEPTED, uid);



        return v;

    }
    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = MeasureSpec.makeMeasureSpec(mListView.getWidth(), MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }


    private class AsyncDataClass extends AsyncTask<String, Void, String> {

        @Override

        protected String doInBackground(String... params) {



            String jsonResult = "";

            try {

                Map<String,String> nameValuePairs = new HashMap<String,String>();

                nameValuePairs.put("uid", params[1]);

                URL url = new URL(AppConfig.URL_GETACCEPTED);
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

            System.out.println("Accepted Tab Resulted Value: " + result);

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
                List<AcceptedModel> lst = returnParsedJsonObject(result);
                meetAccepted = (ListView) getActivity().findViewById(R.id.lstMeetAccepted);
                CustomAcceptedAdapter cta = new CustomAcceptedAdapter(getActivity(), R.layout.accepted_rows,lst);
                meetAccepted.setAdapter(cta);
                ListAdapter meetAccept = meetAccepted.getAdapter();
                int adamA_len = meetAccept.getCount();
                if (adamA_len > 0){
                    TextView tv_meet = (TextView) getActivity().findViewById(R.id.meet_acceptedempty);
                    //tv_meet.setVisibility(View.INVISIBLE);
                    tv_meet.setAlpha(0.0f);
                }

                List<AcceptedEventModel> lstEvent = returnParsedJsonObjectEvent(result);
                eventAccepted = (ListView) getActivity().findViewById(R.id.lstEventAccepted);
                CustomAcceptedEventAdapter ctaEvent = new CustomAcceptedEventAdapter(getActivity(), R.layout.accepted_rows,lstEvent);
                eventAccepted.setAdapter(ctaEvent);
                ListAdapter eventAccept = eventAccepted.getAdapter();
                int adaeA_len = eventAccept.getCount();
                if (adaeA_len > 0){
                    TextView tv_event = (TextView) getActivity().findViewById(R.id.event_acceptedempty);
                    //tv_event.setVisibility(View.INVISIBLE);
                    tv_event.setAlpha(0.0f);
                }

                cta.notifyDataSetChanged();
                ctaEvent.notifyDataSetChanged();

                ListUtils.setDynamicHeight(meetAccepted);
                ListUtils.setDynamicHeight(eventAccepted);

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



    private List<AcceptedModel> returnParsedJsonObject(String result){

        JSONObject resultObject = null;
        JSONArray data = null;
        List<AcceptedModel> schModelList = new ArrayList<>();



        try {

            resultObject = new JSONObject(result);
            data = resultObject.getJSONArray("results");



            for(int i = 0;i< data.length();i++){
                AcceptedModel sch = new AcceptedModel();
                JSONObject obj = data.getJSONObject(i);

                sch.setMeetname(obj.getString("meeting_name"));
                sch.setLocation(obj.getString("meeting_location"));
                sch.setDate(obj.getString("meeting_date"));
                sch.setParticipants(obj.getString("meeting_participants"));
                sch.setTimefrom(obj.getString("from_time"));
                sch.setTimeto(obj.getString("to_time"));
                schModelList.add(sch);

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }

        return schModelList;

    }

    private List<AcceptedEventModel> returnParsedJsonObjectEvent(String result){

        JSONObject resultObject = null;
        JSONArray data = null;
        List<AcceptedEventModel> schModelList = new ArrayList<>();



        try {

            resultObject = new JSONObject(result);
            data = resultObject.getJSONArray("results_event");



            for(int i = 0;i< data.length();i++){
                AcceptedEventModel sch = new AcceptedEventModel();
                JSONObject obj = data.getJSONObject(i);

                sch.setEventname(obj.getString("event_name"));
                sch.setLocation(obj.getString("event_location"));
                sch.setDate(obj.getString("event_date"));
                sch.setParticipants(obj.getString("event_participants"));
                sch.setTime(obj.getString("event_time"));

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