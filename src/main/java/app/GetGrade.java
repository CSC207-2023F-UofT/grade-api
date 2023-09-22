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

public class GetGrade extends Subpage{
    // two fields: utorid, and course.
    private JButton submitButton, goBackButton;
    private JTextField studentField, courseField;
    // constructor
    public GetGrade(CardLayout cardLayout, JPanel cards) {
        setLayout(new GridLayout(4, 2));

        submitButton = new JButton("Submit");
        studentField = new JTextField();
        courseField = new JTextField();

        goBackButton = new JButton("Go Back to Menu");

        add(new JLabel("Student:"));
        add(studentField);
        add(new JLabel("Course:"));
        add(courseField);
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
                String utorid = studentField.getText();
                String course = courseField.getText();

                // For example: Display the retrieved grade in a dialog box
                try {
                    JSONObject response = GetGrade.getGrade(utorid, course);
                    if (response.getInt("status_code") == 200) {
                        // successful.
                        JSONObject grade = response.getJSONObject("grade");
                        JOptionPane.showMessageDialog(cards, String.format("Grade: %s", grade.getInt("grade")));
                    }
                    else {
                        JOptionPane.showMessageDialog(cards, response.getString("message"));
                    }

                }
                catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                catch (JSONException ex) {
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
        studentField.setText("");
    }


    public static JSONObject getGrade(String utorid, String course) throws IOException, JSONException {
        // Okhttp3:

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(String.format("https://grade-logging-api.chenpan.ca/grade?course=%s&utorid=%s", course, utorid))
                .addHeader("Authorization", System.getenv("API_TOKEN"))
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();

        // response body is a json object.
        JSONObject responseObj = new JSONObject(response.body().string());
        return responseObj;
    }

    @Override
    public void run() throws IOException {

    }
}
