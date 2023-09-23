package app.gui;

import api.MongoGradeDB;
import app.Config;
import entity.Grade;
import use_case.*;

import javax.swing.*;
import java.awt.*;
import java.lang.String;

public class GUI {
    public static void main(String[] args) {

        // Initial setup for the program.
        // The config hides the details of which implementation of GradeDB
        // we are using in the program. If we were to use a different implementation
        // of GradeDB, this config is what we would change.
        Config config = new Config();

        GetGradeUseCase getGradeUseCase = config.getGradeUseCase();
        LogGradeUseCase logGradeUseCase = config.logGradeUseCase();
        FormTeamUseCase formTeamUseCase = config.formTeamUseCase();
        JoinTeamUseCase joinTeamUseCase = config.joinTeamUseCase();
        LeaveTeamUseCase leaveTeamUseCase = config.leaveTeamUseCase();
        GetAverageGradeUseCase getAverageGradeUseCase = config.getAverageGradeUseCase();

        // this is the code that runs to set up our GUI
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Grade GUI App");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(850, 300);

            CardLayout cardLayout = new CardLayout();
            JPanel cardPanel = new JPanel(cardLayout);

            JPanel defaultCard = createDefaultCard();
            JPanel getGradeCard = createGetGradeCard(frame, getGradeUseCase);
            JPanel logGradeCard = createLogGradeCard(frame, logGradeUseCase);
            JPanel formTeamCard = createFormTeamCard(frame, formTeamUseCase);
            JPanel joinTeamCard = createJoinTeamCard(frame, joinTeamUseCase);
            JPanel manageTeamCard = createManageTeamCard(frame, leaveTeamUseCase, getAverageGradeUseCase);

            cardPanel.add(defaultCard, "DefaultCard");
            cardPanel.add(getGradeCard, "GetGradeCard");
            cardPanel.add(logGradeCard, "LogGradeCard");
            cardPanel.add(formTeamCard, "FormTeamCard");
            cardPanel.add(joinTeamCard, "JoinTeamCard");
            cardPanel.add(manageTeamCard, "ManageTeamCard");

            JButton getGradeButton = new JButton("Get Grade");
            getGradeButton.addActionListener(e -> cardLayout.show(cardPanel, "GetGradeCard"));

            JButton logGradeButton = new JButton("Log Grade");
            logGradeButton.addActionListener(e -> cardLayout.show(cardPanel, "LogGradeCard"));

            JButton formTeamButton = new JButton("Form a team");
            formTeamButton.addActionListener(e -> cardLayout.show(cardPanel, "FormTeamCard"));

            JButton joinTeamButton = new JButton("Join a team");
            joinTeamButton.addActionListener(e -> cardLayout.show(cardPanel, "JoinTeamCard"));

            JButton manageTeamButton = new JButton("My Team");
            manageTeamButton.addActionListener(e -> cardLayout.show(cardPanel, "ManageTeamCard"));

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(getGradeButton);
            buttonPanel.add(logGradeButton);
            buttonPanel.add(formTeamButton);
            buttonPanel.add(joinTeamButton);
            buttonPanel.add(manageTeamButton);

            frame.getContentPane().add(cardPanel, BorderLayout.CENTER);
            frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);

        });
    }

    // utility methods that take care of setting up each JPanel to be displayed
    // in our GUI
    private static JPanel createDefaultCard() {
        JPanel defaultCard = new JPanel();
        defaultCard.setLayout(new BorderLayout());

        JLabel infoLabel = new JLabel("Welcome to the Grade App (GUI Version)!\n" +
                "\t Using API TOKEN: " + MongoGradeDB.getApiToken());

        defaultCard.add(infoLabel, BorderLayout.CENTER);

        return defaultCard;
    }

    private static JPanel createGetGradeCard(JFrame jFrame, GetGradeUseCase getGradeUseCase) {
        JPanel getGradeCard = new JPanel();
        getGradeCard.setLayout(new GridLayout(3, 2));

        JTextField utoridField = new JTextField(20);
        JTextField courseField = new JTextField(20);
        JButton getButton = new JButton("Get");

        JLabel resultLabel = new JLabel();

        getButton.addActionListener(e -> {
            String utorid = utoridField.getText();
            String course = courseField.getText();

            try{
                Grade grade = getGradeUseCase.getGrade(utorid, course);
                JOptionPane.showMessageDialog(jFrame, String.format("Grade: %d", grade.getGrade()));
            }
            catch (Exception ex){
                JOptionPane.showMessageDialog(jFrame, ex.getMessage());
            }
        });

        getGradeCard.add(new JLabel("UTORid:"));
        getGradeCard.add(utoridField);
        getGradeCard.add(new JLabel("Course:"));
        getGradeCard.add(courseField);
        getGradeCard.add(getButton);
        getGradeCard.add(resultLabel);

        return getGradeCard;
    }

    private static JPanel createLogGradeCard(JFrame jFrame, LogGradeUseCase logGradeUseCase) {
        JPanel logGradeCard = new JPanel();
        logGradeCard.setLayout(new GridLayout(4, 2));
        JTextField courseField = new JTextField(20);
        JTextField gradeField = new JTextField(20);
        JButton logButton = new JButton("Log");
        JLabel resultLabel = new JLabel();

        logButton.addActionListener(e -> {
            String course = courseField.getText();
            String gradeStr = gradeField.getText();
            int grade = Integer.parseInt(gradeStr);

            try {
                logGradeUseCase.logGrade(course, grade);
                JOptionPane.showMessageDialog(jFrame, "Grade Added successfully.");
                courseField.setText("");
                gradeField.setText("");
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(jFrame, ex.getMessage());
            }

        });
        logGradeCard.add(new JLabel("Course:"));
        logGradeCard.add(courseField);
        logGradeCard.add(new JLabel("Grade:"));
        logGradeCard.add(gradeField);
        logGradeCard.add(logButton);
        logGradeCard.add(resultLabel);


        return logGradeCard;
    }

    private static JPanel createFormTeamCard(JFrame jFrame, FormTeamUseCase formTeamUseCase) {
        JPanel theCard = new JPanel();
        theCard.setLayout(new GridLayout(4, 2));
        JTextField nameField = new JTextField(20);
        JButton submitButton = new JButton("Submit");
        JLabel resultLabel = new JLabel();

        submitButton.addActionListener(e -> {
            String name = nameField.getText();

            try {
                formTeamUseCase.formTeam(name);
                JOptionPane.showMessageDialog(jFrame, "Team formed!");
                nameField.setText("");
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(jFrame, ex.getMessage());
            }

        });
        theCard.add(new JLabel("Name (please choose a unique team name):"));
        theCard.add(nameField);
        theCard.add(submitButton);
        theCard.add(resultLabel);


        return theCard;
    }

    private static JPanel createJoinTeamCard(JFrame jFrame, JoinTeamUseCase joinTeamUseCase) {
        JPanel theCard = new JPanel();
        theCard.setLayout(new GridLayout(4, 2));
        JTextField nameField = new JTextField(20);
        JButton submitButton = new JButton("Submit");
        JLabel resultLabel = new JLabel();

        submitButton.addActionListener(e -> {
            String name = nameField.getText();

            try {
                joinTeamUseCase.joinTeam(name);
                JOptionPane.showMessageDialog(jFrame, "Joined successfully");
                nameField.setText("");
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(jFrame, ex.getMessage());
            }
        });
        theCard.add(new JLabel("The team name:"));
        theCard.add(nameField);
        theCard.add(submitButton);
        theCard.add(resultLabel);


        return theCard;
    }


    private static JPanel createManageTeamCard(JFrame jFrame, LeaveTeamUseCase leaveTeamUseCase,
                                               GetAverageGradeUseCase getAverageGradeUseCase) {
        JPanel theCard = new JPanel();
        theCard.setLayout(new GridLayout(4, 2));
        JTextField courseField = new JTextField(20);
        // make a separate line.
        JButton getAverageButton = new JButton("Get Average Grade");
        JButton leaveTeamButton = new JButton("Leave Team");
        JLabel resultLabel = new JLabel();

        getAverageButton.addActionListener(e -> {
            String course = courseField.getText();

            try {
                float avg = getAverageGradeUseCase.getAverageGrade(course);
                JOptionPane.showMessageDialog(jFrame, "Average Grade: " + avg);
                courseField.setText("");
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(jFrame, ex.getMessage());
            }
        });


        leaveTeamButton.addActionListener(e -> {
            try {
                leaveTeamUseCase.leaveTeam();
                JOptionPane.showMessageDialog(jFrame, "Left team successfully.");
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(jFrame, ex.getMessage());
            }
        });
        theCard.add(new JLabel("The Course you want to calculate the team average for:"));
        theCard.add(courseField);
        theCard.add(getAverageButton);
        theCard.add(leaveTeamButton);
        theCard.add(resultLabel);


        return theCard;
    }
}