package com.emostn.publictransit2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.emostn.publictransit2.db_helper.Helper;
import com.emostn.publictransit2.model.jSoupTrolley;
import com.emostn.publictransit2.util.ConnectionDetector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//com.emostn.publictransit2.MainActivity
public class MainActivity extends Activity implements OnItemSelectedListener {
    // URL Address
    private String eggedScheduleUrl;
    private String chance99Url;
    private String geocommerceUrl;

    // data
    private ArrayList<jSoupTrolley> jSoupTrolleys;

    private ArrayList<String> trolleyNames;

    private Elements td;
    private Helper db = new Helper(this);
    // UI variables
    private Spinner lineNameSelector;


    private TextView resultTV;
    private ProgressDialog mProgressDialog;

    // Connection detector class
    ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        jSoupTrolleys = new ArrayList<>();
        trolleyNames = new ArrayList<>();

        //init resources
        eggedScheduleUrl = getResources().getString(R.string.egged_schedules);
        chance99Url = getString(R.string.chance99Url);
        geocommerceUrl = getString(R.string.geocommerceUrl);

        // Initialize UI variables
        resultTV = (TextView) findViewById(R.id.output);

        lineNameSelector = (Spinner) findViewById(R.id.lineNumbersSpinner);
        lineNameSelector.setVisibility(View.GONE);
        Log.i("TEST", "UI variables initialized");
        String test = "";


        if (db.getTrolleyRecord().isEmpty()) {
            downloadData();
        } else {
            jSoupTrolleys = db.getTrolleyRecord();
            for (jSoupTrolley jSoupTrolley : jSoupTrolleys) {
                trolleyNames.add(jSoupTrolley.getName());
            }
            addItemsOnSpinner(trolleyNames);
            lineNameSelector.setVisibility(View.VISIBLE);
        }
        resultTV.setText(test);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_download was selected
            case R.id.action_download:
                downloadData();
                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings selected", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }

        return true;
    }

    private void downloadData() {

        cd = new ConnectionDetector(getApplicationContext());
        PingTask ping = new PingTask();
        if (!ping.getStatus().equals(AsyncTask.Status.PENDING)
                || !ping.getStatus().equals(AsyncTask.Status.RUNNING)) {
            ping.execute();
        }

    }

    //ping
    private class PingTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String[] urls) {

            if (cd.isConnectingToInternet()) {
                try {
                    HttpURLConnection connection = (HttpURLConnection)
                            (new URL("http://clients3.google.com/generate_204")
                                    .openConnection());
                    connection.setRequestProperty("User-Agent", "Android");
                    connection.setRequestProperty("Connection", "close");
                    connection.setConnectTimeout(1500);
                    connection.connect();
                    return (connection.getResponseCode() == 204 &&
                            connection.getContentLength() == 0);
                } catch (IOException e) {
                    Log.e("net", "Error checking internet connection", e);
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                FetchEggedDataTask task = new FetchEggedDataTask();
                if (!task.getStatus().equals(AsyncTask.Status.PENDING)
                        || !task.getStatus().equals(AsyncTask.Status.RUNNING)) {

                    task.execute(eggedScheduleUrl);
                }
            } else {
                showAlertDialog(MainActivity.this, getResources().getString(R.string.connection_status_title),
                        getResources().getString(R.string.no_connection), false);

            }
        }

    }

    // download data task
    private class FetchEggedDataTask extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Fetching Data");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                db.dropTrolleys();
                jSoupTrolleys.clear();
                trolleyNames.clear();
                Document eggedJSoupDoc;
                Elements links;
                eggedJSoupDoc = Jsoup.connect(urls[0]).get();
                links = eggedJSoupDoc.select("a[href*=/trolley/]");
                for (Element element : links) {
                    jSoupTrolley tempJSoupTrolley = new jSoupTrolley();
                    tempJSoupTrolley.setUrl(element.attr("abs:href"));
                    Document tempDock = Jsoup.connect(element.attr("abs:href")).get();

                    tempJSoupTrolley.setHtml(tempDock.html());
                    tempJSoupTrolley.setName(element.ownText());
                    //trolleyNames.add(element.ownText());
                    //trolleys.add(tempTrolley);
                    String insertedTrolley = "";
                    insertedTrolley = db.insertTrolleyRecord(tempJSoupTrolley);
                    publishProgress(tempJSoupTrolley.getName() + " : " + insertedTrolley);
                }
                return true;
            } catch (IOException e) {
                mProgressDialog.dismiss();
                return false;
            }
            // return false;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setMessage(progress[0].toString());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressDialog.dismiss();
//			for (Trolley tmp : trolleys) {
//				String row = db.insertTrolleyRecord(tmp);
//				resultTV.setText(resultTV.getText() + "\n" + row);
//			}

            jSoupTrolleys = db.getTrolleyRecord();
            trolleyNames = db.getTrolleyNames();
            if (!trolleyNames.isEmpty()) {
                addItemsOnSpinner(trolleyNames);
                lineNameSelector.setVisibility(View.VISIBLE);
            } else {
                //resultTV.setText(db.getTrolleyNames().toString());
            }


            //Toast.makeText(getApplicationContext(), "Data downloaded", Toast.LENGTH_LONG).show();

        }
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner(ArrayList<String> items) {

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        lineNameSelector.setAdapter(dataAdapter);
        lineNameSelector.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        String lineNumber = parentView.getItemAtPosition(position).toString();
        lineNameSelector.setSelection(parentView.getSelectedItemPosition());
        Toast.makeText(parentView.getContext(),
                "OnItemSelectedListener : " + lineNumber,
                Toast.LENGTH_LONG).show();
        td = Jsoup.parse(jSoupTrolleys.get(parentView.getSelectedItemPosition()).getHtml()).select("td.main_link");
        resultTV.setText(td.text());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parentView) {
        // your code here
    }


    /**
     * Function to display simple Alert Dialog
     *
     * @param context - application context
     * @param title   - alert dialog title
     * @param message - alert message
     * @param status  - success/failure (used to set icon)
     */
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setIcon((status) ? R.drawable.success : R.drawable.fail)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
