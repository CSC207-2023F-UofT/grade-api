package app;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class HandleNewMemberRequest extends Subpage {
    private JButton goBackButton;

    private JButton acceptButton, rejectButton;
    private CardLayout cardLayout;
    private JPanel cards;

    private JLabel requestListLabel = new JLabel("");

    private JTextField studentField;
    // constructor
    public HandleNewMemberRequest(CardLayout cardLayout, JPanel cards) {
        this.cardLayout = cardLayout;
        this.cards = cards;

        setLayout(new GridLayout(4, 2));

        acceptButton = new JButton("Accept");
        rejectButton = new JButton("Reject");
        goBackButton = new JButton("Go Back to Menu");
        studentField = new JTextField();

        add(new JLabel("Request List:"));
        add(this.requestListLabel);
        add(new JLabel("Who to accept/reject:"));
        add(studentField);
        add(acceptButton);
        add(rejectButton);
        add(goBackButton);

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "main");
            }
        });

        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the grade retrieval logic here
                String utorid = studentField.getText();
                // For example: Display the retrieved grade in a dialog box
                try {
                    JSONObject response = HandleNewMemberRequest.handleNewMemberRequest(utorid, 1);
                    JOptionPane.showMessageDialog(cards, response.getString("message"));
                }
                catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }

                // Switch back to the main panel
                try {
                    run();
                } catch (IOException | JSONException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the grade retrieval logic here
                String utorid = studentField.getText();
                // For example: Display the retrieved grade in a dialog box
                try {
                    JSONObject response = HandleNewMemberRequest.handleNewMemberRequest(utorid, 0);
                    JOptionPane.showMessageDialog(cards, response.getString("message"));
                }
                catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }

                // Switch back to the main panel
                try {
                    run();
                } catch (IOException | JSONException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public JSONObject getRequestList() throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(String.format("https://grade-logging-api.chenpan.ca/teamRequestList"))
                .addHeader("Authorization", System.getenv("API_TOKEN"))
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();

        // response body is a json object.
        System.out.println(response);
        JSONObject responseObj = new JSONObject(response.body().string());
        return responseObj;
    }

    public static JSONObject handleNewMemberRequest(String newMemberUtorid, int decision) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject requestBody = new JSONObject();
        requestBody.put("newMemberUtorid", newMemberUtorid);
        requestBody.put("decision", decision);
        RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        Request request = new Request.Builder()
                .url("https://grade-logging-api.chenpan.ca/handleNewMemberRequest")
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
        System.out.println("HandleNewMemberRequest.run");
        JSONObject requetListResponse = this.getRequestList();
        if(requetListResponse.getInt("status_code") == 200) {
            // successful.
            StringBuilder msg = new StringBuilder();
            for(int i = 0; i < requetListResponse.getJSONArray("requestList").length(); i++) {
                // Add to string builder with. For the last one, not to end with ,.
                if(i != requetListResponse.getJSONArray("requestList").length() - 1) {
                    msg.append(requetListResponse.getJSONArray("requestList").get(i).toString()).append(", ");
                }
                else {
                    msg.append(requetListResponse.getJSONArray("requestList").get(i).toString());
                }
            }
            this.requestListLabel.setText(msg.toString());
        }
        else {
            // error.
            JOptionPane.showMessageDialog(this.cards, requetListResponse.getString("message"));
            // go back to main.
            this.cardLayout.show(this.cards, "main");
        }
    }

    private void clear() {
        studentField.setText("");
    }

}
