package com.google.preet;

import android.app.Activity;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.util.AsynchUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


@DesignerComponent(
  category = ComponentCategory.EXTENSION,
  description = "A Non-Visible component that helps you to send emails from your App.",
  showOnPalette = true,
  nonVisible = true,
  iconName = "https://img.icons8.com/color/16/000000/gmail-new.png",
  versionName = "1.0",
  version = 1
)
@SimpleObject(external = true)
public class GoogleGmail extends AndroidNonvisibleComponent {

  private String deploymentKey;
  private final Activity activity;

  public GoogleGmail(ComponentContainer componentContainer) {
    super(componentContainer.$form());
    this.activity = componentContainer.$context();
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "Deployment Key")
  @SimpleProperty(description = "Set your App Script's deployment key.")
  public void DeploymentKey(String key) {
    this.deploymentKey = key;
  }

  @SimpleProperty
  public String DeploymentKey() {
    return this.deploymentKey;
  }

  @SimpleEvent(description = "Event raised when mail sent.")
  public void MailSend(final String status, final String message) {
    EventDispatcher.dispatchEvent(this, "MailSend", status, message);
  }

  @SimpleEvent(description = "Event raised when error occurred.")
  public void ErrorOccurred(final String errorMessage) {
    EventDispatcher.dispatchEvent(this, "ErrorOccurred", errorMessage);
  }

  @SimpleFunction(description = "Send mail.")
  public void SendMail(final String mailId, final String subject, final String body) {
    AsynchUtil.runAsynchronously(new Runnable() {
      @Override
      public void run() {
        final String requestURL = "https://script.google.com/macros/s/" + GoogleGmail.this.deploymentKey + "/exec?" + "mail_Id=" + mailId + "&body=" + body + "&sub=" + subject;
        try {
          final URL url = new URL(requestURL);
          HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
          httpURLConnection.setRequestMethod("POST");
          httpURLConnection.setDoOutput(true);
          BufferedReader bufferedReader = null;
          if (httpURLConnection.getResponseCode() == 200) {
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
              output.append(line).append("\n");
            }
            bufferedReader.close();
            final String outputContent = output.toString();
            final JSONObject jsonObject = new JSONObject(outputContent);
            activity.runOnUiThread(new Runnable() {
              @Override
              public void run() {
                try {
                  MailSend(jsonObject.getString("status"), jsonObject.getString("msg"));
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            });
          } else {
            InputStreamReader errorStreamReader = new InputStreamReader(httpURLConnection.getErrorStream());
            bufferedReader = new BufferedReader(errorStreamReader);
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
              output.append(line).append("\n");
            }
            bufferedReader.close();
            final String outputContent = output.toString();
            activity.runOnUiThread(new Runnable() {
              @Override
              public void run() {
                ErrorOccurred(outputContent);
              }
            });
          }
        } catch (MalformedURLException e) {
          e.printStackTrace();
        } catch (IOException | JSONException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
