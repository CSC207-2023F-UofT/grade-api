package app;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame{
    private JPanel cards;
    private CardLayout cardLayout;


    public Main() throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        initUI();
    }

    private void initUI() {
        setTitle("Grade Application");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        cards = new JPanel(new CardLayout());
        cardLayout = (CardLayout) cards.getLayout();

        JPanel mainPanel = new JPanel();
        cards.add(mainPanel, "main");
        // Define subpages.
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JButton getGradeButton = new JButton("Get a Grade");
        JButton logGradeButton = new JButton("Log a Grade");
        JButton deleteGradeButton = new JButton("Delete a Grade");
        JButton seeMyTeamMembersButton = new JButton("See my team members");
        JButton formTeamButton = new JButton("Form a team");
        JButton handleNewMemberRequestButton = new JButton("Handle new member request");
        JButton requestToJoinTeamButton = new JButton("Join a team");
        JButton leaveTeamButton = new JButton("Leave my team");

        // Add a text label: Grade Management
        mainPanel.add(new JLabel("Grade Management"));
        mainPanel.add(getGradeButton);
        mainPanel.add(logGradeButton);
        mainPanel.add(deleteGradeButton);
        // Add a text label: Team Management
        mainPanel.add(new JLabel("Team Management"));
        mainPanel.add(seeMyTeamMembersButton);
        mainPanel.add(formTeamButton);
        mainPanel.add(handleNewMemberRequestButton);
        mainPanel.add(requestToJoinTeamButton);
        mainPanel.add(leaveTeamButton);


        // Adding navigation buttons.
        JButton[] buttons = {getGradeButton, logGradeButton, deleteGradeButton,
                seeMyTeamMembersButton, formTeamButton, requestToJoinTeamButton,
                leaveTeamButton, handleNewMemberRequestButton};
        Class<?>[] classes = {GetGrade.class, LogGrade.class, DeleteGrade.class,
                SeeMyTeamMembers.class, FormTeam.class, RequestToJoinTeam.class,
                LeaveTeam.class, HandleNewMemberRequest.class};
        String[] pageNames = {"getGradePage", "logGradePage", "deleteGradePage",
                "seeMyTeamMembersPage", "formTeamPage", "requestToJoinTeamPage",
                "leaveTeamPage", "handleNewMemberRequestPage"};
        Subpage[] cardComponents = new Subpage[buttons.length];
        for(int i = 0; i < buttons.length; i++) {
            int finalI = i;
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cards, pageNames[finalI]);
                    try {
                        cardComponents[finalI].run();
                    } catch (IOException | JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            try
            {
                Subpage cardComponent = (Subpage) classes[i].getDeclaredConstructor(CardLayout.class,
                        JPanel.class).newInstance(cardLayout, cards);
                cardComponents[i] = cardComponent;
                cards.add(cardComponent, pageNames[finalI]);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        add(cards);
        pack();
        setLocationRelativeTo(null); // place in center of screen
    }


    public static void main(String[] args) {
        // The program starts here.
        SwingUtilities.invokeLater(() -> {
            try {
                new Main().setVisible(true);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

}