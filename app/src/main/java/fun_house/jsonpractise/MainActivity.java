package fun_house.jsonpractise;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/*
 * Step 1 : Create a private asynctask subclass of MainActivity called "JsonTask"
 * Step 2 : Create a new class called "PopulationGetter"
 * Step 3 : execute asynctask in mainActivity
 *
 */

public class MainActivity extends Activity {

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listView);

        new JsonTask().execute();

    }

    private class JsonTask extends AsyncTask<Void, Void, List<String>> {

        String url = "https://data.cityofnewyork.us/api/views/xi7c-iiu2/rows.json?accessType=DOWNLOAD";

        @Override
        protected List<String> doInBackground(Void... params) {
            List<String> list = new ArrayList<>();

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                Response response = client.newCall(request).execute();
                String jsonString = response.body().string();

                JSONObject object = new JSONObject(jsonString);
                JSONObject meta = object.getJSONObject("meta");
                JSONObject view = meta.getJSONObject("view");
                JSONArray columns = view.getJSONArray("columns");

                for (int i = 11; i <= 15; i++) {
                    JSONObject population = columns.getJSONObject(i);
                    JSONObject cachedContents = population.getJSONObject("cachedContents");
                    String name = population.getString("name");

                    int sum = cachedContents.getInt("sum");

                    list.add(name + " " + " Number " + sum);

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return list;
        }

        @Override
        protected void onPostExecute(List<String> list) {

            mAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
            mListView.setAdapter(mAdapter);

        }


    }

}
