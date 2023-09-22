package app;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LogGrade extends Subpage {
    private JButton submitButton, goBackButton;
    private JTextField courseField, gradeField;
    // constructor
    public LogGrade(CardLayout cardLayout, JPanel cards) {
        setLayout(new GridLayout(4, 2));

        submitButton = new JButton("Submit");
        courseField = new JTextField();
        gradeField = new JTextField();
        submitButton = new JButton("Submit");

        // make a go back button.

        goBackButton = new JButton("Go Back to Menu");

        add(new JLabel("Course:"));
        add(courseField);
        add(new JLabel("Grade:"));
        add(gradeField);
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
                String course = courseField.getText();
                String grade = gradeField.getText();

                // For example: Display the retrieved grade in a dialog box
                try {
                    JSONObject response = LogGrade.logGrade(course, grade);
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
        courseField.setText("");
        gradeField.setText("");
    }

    public static JSONObject logGrade(String course, String grade) throws IOException, JSONException {
        // Okhttp3:

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject requestBody = new JSONObject();
        requestBody.put("course", course);
        requestBody.put("grade", grade);
        RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        Request request = new Request.Builder()
                .url("https://grade-logging-api.chenpan.ca/grade")
                .method("POST", body)
                .addHeader("Authorization", "qGHBtvH7myZSKlQ1re3J8oZQztG70HoE")  // System.getenv("API_TOKEN"))
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return new JSONObject(response.body().string());

    }

    @Override
    public void run() throws IOException {

    }
}
