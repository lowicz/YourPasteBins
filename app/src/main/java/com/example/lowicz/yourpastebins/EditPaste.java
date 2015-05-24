package com.example.lowicz.yourpastebins;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
 * Created by s391382 on 2015-05-09.
 */
public class EditPaste extends Activity {
    EditText PasteText;
    EditText PasteUrl;
    String pasteID;
    Database database = new Database(this);
    Paste paste;

    private void closeMe() {
        Intent intent = new Intent(EditPaste.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean checkResponse(String response) {
        String regex = ".*http.*";
        return response.matches(regex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_paste);

        PasteText = (EditText) findViewById(R.id.paste_text2);
        PasteUrl = (EditText) findViewById(R.id.pastebin_url);

        Intent intent = getIntent();
        paste = (Paste) intent.getSerializableExtra("message");

        PasteText.setText(paste.getPaste_text());
        PasteUrl.setText(paste.getUrl());
        pasteID = paste.getPasteID();

        ImageButton UpdatePaste = (ImageButton) findViewById(R.id.send_paste_button2);
        UpdatePaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (paste.getPaste_text().equals(PasteText.getText().toString())) {
                    Toast.makeText(EditPaste.this, "You must change your paste...", Toast.LENGTH_SHORT).show();
                } else {
                    paste.setPaste_text(PasteText.getText().toString());
                    InsertData task = new InsertData();
                    task.execute("http://pastebin.com/api/api_post.php");
                }
            }
        });

        ImageButton DeletePaste = (ImageButton) findViewById(R.id.delete_button);
        DeletePaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.deletePaste(paste.getPasteID());
                Toast.makeText(EditPaste.this, "Paste deleted.", Toast.LENGTH_LONG).show();
                closeMe();
            }
        });

        Button CopyButton = (Button) findViewById(R.id.copy_button);
        CopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText(PasteUrl.getText());

                Toast.makeText(EditPaste.this, "Copied.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class InsertData extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog = new ProgressDialog(EditPaste.this);
        String text = "";

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Updating Paste...");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            InputStream inputStream;
            for (String url : params) {
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
                    Toast.makeText(EditPaste.this, e.toString(), Toast.LENGTH_LONG).show();
                    return false;
                } catch (IOException e) {
                    Toast.makeText(EditPaste.this, e.toString(), Toast.LENGTH_LONG).show();
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
                paste.setUrl(text);
                database.updatePaste(paste);
            } else {
                text = "ERROR: " + text;
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {

                AlertDialog.Builder adialog = new AlertDialog.Builder(EditPaste.this);
                adialog.setTitle("Paste updated");
                adialog.setMessage(text);
                adialog.setCancelable(false);

                adialog.setPositiveButton("Copy URL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        clipboard.setText(text);

                        closeMe();
                        Toast.makeText(EditPaste.this, "Paste updated.", Toast.LENGTH_LONG).show();
                    }
                });

                adialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        closeMe();
                        Toast.makeText(EditPaste.this, "Paste updated.", Toast.LENGTH_LONG).show();
                    }
                });
                adialog.show();


            } else {
                Toast.makeText(EditPaste.this, text, Toast.LENGTH_LONG).show();
                PasteText.setText(paste.getPaste_text());
            }
            dialog.dismiss();
        }
    }
}

