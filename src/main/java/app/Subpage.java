package app;

import org.json.JSONException;

import javax.swing.*;
import java.io.IOException;

abstract public class Subpage extends JPanel {
    // The function that gets called when a subpage is entered.
    abstract public void run() throws IOException, JSONException;
}
