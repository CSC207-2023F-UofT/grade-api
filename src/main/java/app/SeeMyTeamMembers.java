package app;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SeeMyTeamMembers extends Subpage {
    // constructor
    private JButton goBackButton;
    private JLabel msg = new JLabel("");
    public SeeMyTeamMembers(CardLayout cardLayout, JPanel cards) throws IOException {
        // The response has an array in its repsponse body called members.
        // We need to print the members array in the card.
        // If error, print the error message.
        setLayout(new GridLayout(  4, 2));

        goBackButton = new JButton("Go Back to Menu");
        add(this.msg);
        add(goBackButton);

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "main");
            }
        });
    }

    public static JSONObject seeMyTeamMembers() throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(String.format("https://grade-logging-api.chenpan.ca/teamMembers"))
                .addHeader("Authorization", System.getenv("API_TOKEN"))
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();

        // response body is a json object.
        JSONObject responseObj = new JSONObject(response.body().string());
        return responseObj;
    }

    @Override
    public void run() throws IOException, JSONException {
        JSONObject response = SeeMyTeamMembers.seeMyTeamMembers();
        if(response.getInt("status_code") == 200) {
            // successful.
            StringBuilder msg = new StringBuilder();
            for(int i = 0; i < response.getJSONArray("members").length(); i++) {
                // Add to string builder with ,. For the last one, not to end with ,.
                if(i != response.getJSONArray("members").length() - 1) {
                    msg.append(response.getJSONArray("members").get(i).toString()).append(", ");
                }
                else {
                    msg.append(response.getJSONArray("members").get(i).toString());
                }
            }
            // modify this.msg
            this.msg.setText(msg.toString());
        }
        else {
            this.msg.setText(response.getString("message"));
        }
    }
}
