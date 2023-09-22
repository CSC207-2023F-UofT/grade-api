package app;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class RequestToJoinTeam extends Subpage {
    private JButton submitButton, goBackButton;

    private JLabel allTeamsLabel = new JLabel("");
    private JTextField nameField;
    private CardLayout cardLayout;
    private JPanel cards;

    public RequestToJoinTeam(CardLayout cardLayout, JPanel cards) {
        this.cardLayout = cardLayout;
        this.cards = cards;
        this.setLayout(new GridLayout(3, 1));

        add(new JLabel("These are all the teams:"));
        add(this.allTeamsLabel);
        nameField = new JTextField();
        submitButton = new JButton("Submit");
        goBackButton = new JButton("Go Back to Menu");
        add(new JLabel("Team Name:"));
        add(nameField);
        add(submitButton);
        add(goBackButton);
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "main");
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the grade retrieval logic here
                String name = nameField.getText();

                // For example: Display the retrieved grade in a dialog box
                try {
                    JSONObject response = RequestToJoinTeam.requestToJoinTeam(name);
                    JOptionPane.showMessageDialog(cards, response.getString("message"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }

                // Switch back to the main panel
                cardLayout.show(cards, "main");
                clear();
            }
        });
    }

    public JSONObject getAllTeams() throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(String.format("https://grade-logging-api.chenpan.ca/teams"))
                .addHeader("Authorization", System.getenv("API_TOKEN"))
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();

        // response body is a json object.
        JSONObject responseObj = new JSONObject(response.body().string());
        return responseObj;
    }

    public static JSONObject requestToJoinTeam(String name) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", name);
        RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        Request request = new Request.Builder()
                .url("https://grade-logging-api.chenpan.ca/requestJoinTeam")
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
        JSONObject seeMyTeamMemberResponse = SeeMyTeamMembers.seeMyTeamMembers(); //Reuse this api to check if the user has already been in a form. Note that if not in a team already, 404 error will be received.

        if (seeMyTeamMemberResponse.getInt("status_code") == 200) {
            JOptionPane.showMessageDialog(this.cards, "You have already been in a team.");
            this.cardLayout.show(this.cards, "main");
        }
        else {
            // get all vailable teams.
            JSONObject getAllTeamsResponse = getAllTeams();
            if (getAllTeamsResponse.getInt("status_code") == 200) {
                // set allTeamsLabel.
                StringBuilder msg = new StringBuilder();
                for(int i = 0; i < getAllTeamsResponse.getJSONArray("teams").length(); i++) {
                    // Add to string builder with ,. For the last one, not to end with ,.
                    if(i != getAllTeamsResponse.getJSONArray("teams").length() - 1) {
                        msg.append(getAllTeamsResponse.getJSONArray("teams").get(i).toString()).append(", ");
                    }
                    else {
                        msg.append(getAllTeamsResponse.getJSONArray("teams").get(i).toString());
                    }
                }
                // modify this.msg
                this.allTeamsLabel.setText(msg.toString());
            }
            else {
                // Something is wrong, failed loading team names.
                JOptionPane.showMessageDialog(this.cards, "You have already been in a team.");
                this.cardLayout.show(this.cards, "main");
            }
        }
    }
    private void clear() {
        nameField.setText("");
    }

}
