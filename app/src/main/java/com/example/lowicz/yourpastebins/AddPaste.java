package com.example.lowicz.yourpastebins;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


/**
 * Created by lowicz on 03.05.15.
 */


public class AddPaste extends Activity {

    EditText PasteText;
    Database database = new Database(this);

    private boolean checkResponse(String response) {
        String regex = ".*http.*";
        return response.matches(regex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_paste);
        FacebookSdk.sdkInitialize(getApplicationContext());
        PasteText = (EditText) findViewById(R.id.paste_text);


    }

    public void sendPaste(View v) {
        InsertData task = new InsertData();
        task.execute("http://pastebin.com/api/api_post.php");
    }

    private class InsertData extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog = new ProgressDialog(AddPaste.this);
        String text = "";

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Sending Paste...");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            InputStream inputStream;
            for(String url : params) {
                try {
                    ArrayList<NameValuePair> pairs = new ArrayList<>();
                    pairs.add(new BasicNameValuePair("api_dev_key", getString(R.string.api_dev_key)));
                    pairs.add(new BasicNameValuePair("api_option", "paste"));
                    pairs.add(new BasicNameValuePair("api_paste_code", PasteText.getText().toString()));

                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(String.valueOf(url));
                    post.setEntity(new UrlEncodedFormEntity(pairs));

                    HttpResponse response = client.execute(post);
                    inputStream = response.getEntity().getContent();
                } catch (ClientProtocolException e) {
                    Toast.makeText(AddPaste.this, e.toString(), Toast.LENGTH_LONG).show();
                    return false;
                } catch (IOException e){
                    Toast.makeText(AddPaste.this, e.toString(), Toast.LENGTH_LONG).show();
                    return false;
                }

                BufferedReader reader;

                try {
                    reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    text += reader.readLine();
                    inputStream.close();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (checkResponse(text)) {
                Paste qvalues = new Paste();

                qvalues.setUrl(text);
                qvalues.setPaste_text(PasteText.getText().toString());

                qvalues.setPasteID(database.insertPaste(qvalues));
            } else {
                text = "ERROR: " + text;
                return false;
            }


            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result){

                AlertDialog.Builder adialog = new AlertDialog.Builder(AddPaste.this);
                adialog.setTitle("Paste added");
                adialog.setMessage(text);
                adialog.setCancelable(false);

                adialog.setPositiveButton("Copy URL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        clipboard.setText(text);

                        Intent intent = new Intent(AddPaste.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(AddPaste.this, "Paste added.", Toast.LENGTH_LONG).show();
                    }
                });

                adialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AddPaste.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(AddPaste.this, "Paste added.", Toast.LENGTH_LONG).show();
                    }
                });
                final ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(text))
                        .build();

                adialog.setNeutralButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShareDialog sDialog = new ShareDialog(AddPaste.this);
                        sDialog.show(content);

                        Intent intent = new Intent(AddPaste.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(AddPaste.this, "Paste added.", Toast.LENGTH_LONG).show();
                    }
                });
                adialog.show();
            }
            else {
                Toast.makeText(AddPaste.this, text, Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }
}
