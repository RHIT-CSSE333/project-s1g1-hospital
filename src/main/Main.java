package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Locale;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class Main extends JFrame{

    private static JFrame frame;
//    private JPanel panel;
//    private JPanel textPanel;
//    private static Connection connection = null;
    static final int frameWidth = 1600;
    static final int frameHeight = 800;

    static final int frameLocX = 50;
    static final int frameLocY = 50;

//COMMENT COMMMENT COMMENT fadsdsfdsafdsafdsaafhdhdsag dsfadsfds 

    private void runApp() {
        frame = new JFrame();
        frame.setSize(frameWidth, frameHeight);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        ScreenViewer screenviewer = new ScreenViewer(frame);
    }

    public static void main(String[] args) throws IOException {
        Main mainApp = new Main();
        mainApp.runApp();
    }

}