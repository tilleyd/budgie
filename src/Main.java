import gui.MainFrame;

import javax.swing.*;
import database.SqliteDatabase;
import database.DatabaseError;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SqliteDatabase db = new SqliteDatabase("budgie.sqlite");
        try {
            db.connect();
            // TODO database init logic?

            JFrame frame = new JFrame("Budgie");
            frame.setContentPane(new MainFrame(db).getRoot());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent windowEvent) {}

                @Override
                public void windowClosing(WindowEvent windowEvent) {
                    db.disconnect();
                }

                @Override
                public void windowClosed(WindowEvent windowEvent) {}

                @Override
                public void windowIconified(WindowEvent windowEvent) {}

                @Override
                public void windowDeiconified(WindowEvent windowEvent) {}

                @Override
                public void windowActivated(WindowEvent windowEvent) {}

                @Override
                public void windowDeactivated(WindowEvent windowEvent) {}
            });
            frame.setVisible(true);
        } catch (DatabaseError e) {
            System.out.println(e.getMessage());
        }
    }
}
