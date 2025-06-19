package ui;

import javax.swing.*;
import java.awt.Component;

public final class Dialogs {
    private Dialogs() {}

    public static void warn(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public static void error(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void info(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
