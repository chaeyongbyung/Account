package com.example.cyb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private EditText inputMoney;
    private EditText contentMoney;
    private EditText dateMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputMoney = (EditText) findViewById(R.id.input);
        contentMoney = (EditText) findViewById(R.id.content);
        dateMoney = (EditText) findViewById(R.id.date);
    }

    public void insert(View view){
        String Money = inputMoney.getText().toString();
        String Content = contentMoney.getText().toString();
        String Date = dateMoney.getText().toString();

        insertoToDatabase(Money, Content, Date);
    }
    private void insertoToDatabase (String contentMoney, String dateMoney, String inputMoney) {
        class InsertData extends AsyncTask<String, String, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                try {

                    String contentMoney = (String) params[0];
                    String dateMoney = (String) params[1];
                    String inputMoney = (String) params[2];


                    String link = "http://localhost/register.php.rtf";
                    String data = URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(contentMoney, "UTF-8");
                    data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(dateMoney, "UTF-8");
                    data += "&" + URLEncoder.encode("input", "UTF-8") + "=" + URLEncoder.encode(inputMoney, "UTF-8");


                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;


                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(contentMoney, dateMoney, inputMoney);
    }

}
