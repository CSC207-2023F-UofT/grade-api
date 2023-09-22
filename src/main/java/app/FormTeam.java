package app;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class FormTeam extends Subpage {
    private JButton submitButton, goBackButton;
    private JTextField nameField;
    private CardLayout cardLayout;
    private JPanel cards;
    // constructor
    public FormTeam(CardLayout cardLayout, JPanel cards) throws IOException {
        this.cardLayout = cardLayout;
        this.cards = cards;
        setLayout(new GridLayout(4, 2));
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
                    JSONObject response = FormTeam.formTeam(name);
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

    private void clear() {
        nameField.setText("");
    }

    @Override
    public void run() throws IOException, JSONException {
        JSONObject seeMyTeamMemberResponse = SeeMyTeamMembers.seeMyTeamMembers();
        //Reuse this api to check if the user has already been in a form.
        // Note that if not in a team already, a 404 error will be received.

        if (seeMyTeamMemberResponse.getInt("status_code") == 200) {
            JOptionPane.showMessageDialog(this.cards, "You have already been in a team.");
            this.cardLayout.show(this.cards, "main");
        }
    }


    public static JSONObject formTeam(String name) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", name);

        RequestBody body = RequestBody.Companion.create(requestBody.toString(),
                mediaType);
        //RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        Request request = new Request.Builder()
                .url("https://grade-logging-api.chenpan.ca/team")
                .method("POST", body)
                .addHeader("Authorization", System.getenv("API_TOKEN"))
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return new JSONObject(response.body().string());
    }
}

