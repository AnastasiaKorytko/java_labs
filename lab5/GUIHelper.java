//package org.example;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//
//public class GUIHelper {
//
//    public static JPanel createLabeledField(String label, JComponent component) {
//        JPanel panel = new JPanel(new BorderLayout(5, 5));
//        panel.add(new JLabel(label + ":"), BorderLayout.WEST);
//        panel.add(component, BorderLayout.CENTER);
//        return panel;
//    }
//
//    public static JPanel createButtonPanel(JButton... buttons) {
//        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
//        for (JButton button : buttons) {
//            panel.add(button);
//        }
//        return panel;
//    }
//
//    public static void showErrorDialog(Component parent, String message) {
//        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
//    }
//
//    public static void showInfoDialog(Component parent, String message) {
//        JOptionPane.showMessageDialog(parent, message, "Information", JOptionPane.INFORMATION_MESSAGE);
//    }
//
//    public static boolean showConfirmDialog(Component parent, String message) {
//        return JOptionPane.showConfirmDialog(parent, message, "Confirm",
//                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
//    }
//
//    public static JTextArea createLogArea() {
//        JTextArea logArea = new JTextArea(10, 50);
//        logArea.setEditable(false);
//        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
//        logArea.setBackground(new Color(240, 240, 240));
//        logArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
//        return logArea;
//    }
//
//    public static JTable createStyledTable(DefaultTableModel model) {
//        JTable table = new JTable(model);
//        table.setRowHeight(25);
//        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
//        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
//        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        table.setGridColor(Color.LIGHT_GRAY);
//        return table;
//    }
//}
