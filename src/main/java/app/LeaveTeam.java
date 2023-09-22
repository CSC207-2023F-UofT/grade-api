package app;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LeaveTeam extends Subpage{
    // three fields: utorid, course, and grade.
    // constructor
    private CardLayout cardLayout;
    private JPanel cards;

    public LeaveTeam(CardLayout cardLayout, JPanel cards) {
        this.cardLayout = cardLayout;
        this.cards = cards;
    }

    public JSONObject leaveTeam() throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject requestBody = new JSONObject();
        RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        Request request = new Request.Builder()
                .url("https://grade-logging-api.chenpan.ca/leaveTeam")
                .method("PUT", body)
                .addHeader("Authorization", System.getenv("API_TOKEN"))
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject responseObj = new JSONObject(response.body().string());
        return responseObj;
    }

    @Override
    public void run() throws IOException, JSONException {
        JSONObject response = this.leaveTeam();
        if(response.getInt("status_code") == 200) {
            // successful.
            JOptionPane.showMessageDialog(this.cards, response.getString("message"));
        } else {
            // error.
            JOptionPane.showMessageDialog(this.cards, response.getString("message"));
        }

        cardLayout.show(cards, "main");


    }
}

