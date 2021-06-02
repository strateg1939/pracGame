package com.game.snake;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;

public class SwingTableScreen extends JFrame {
    public SwingTableScreen(Main game){
        MenuBar menuBar = new MenuBar();
        PopupMenu menu = new PopupMenu("Options");
        MenuItem menuSave = new MenuItem("Зберегти", new MenuShortcut(KeyEvent.VK_S));
        menu.add(menuSave);
        menuBar.add(menu);
        menuBar.setFont(new Font("sans-serif", Font.BOLD, 12));
        this.setMenuBar(menuBar);
        setSize(850, 300);
        setLocationRelativeTo(null);
        setVisible(true);
        DefaultTableModel tableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String[] columnsHeader = {"Username", "Score", "Difficulty", "Snake size", "Was math game mode", "Other information"};
        tableModel.setColumnIdentifiers(columnsHeader);
        for (Record record : game.records.getRecords()) {
            tableModel.addRow(new Object[]{record.userName, record.score, record.gameDifficulty, record.finalSnakeSize, record.isMathMode, record.gameInformation});
        }
        JScrollPane table = new JScrollPane(new JTable(tableModel));
        getContentPane().add(table, BorderLayout.CENTER);
        getContentPane().repaint();
        getContentPane().revalidate();
    }
}
