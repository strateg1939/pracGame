package com.game.snake;

import jdk.javadoc.internal.doclets.formats.html.markup.Table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class SwingTableScreen extends JFrame {
    public SwingTableScreen(Main game){

        setSize(850, 300);
        setLocationRelativeTo(null);
        setVisible(true);
        final DefaultTableModel tableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if(columnIndex == 4) return Boolean.class;
                if(columnIndex == 1) return Integer.class;
                return super.getColumnClass(columnIndex);
            }
        };
        String[] columnsHeader = {"Username", "Score", "Difficulty", "Snake size", "Was math game mode played", "Other information"};
        tableModel.setColumnIdentifiers(columnsHeader);
        for (Record record : game.records.getRecords()) {
            tableModel.addRow(new Object[]{record.userName, record.score, record.gameDifficulty, record.finalSnakeSize, record.isMathMode, record.gameInformation});
        }
        MenuBar menuBar = new MenuBar();
        PopupMenu menu = new PopupMenu("Options");
        MenuItem menuScore = new MenuItem("Sort by highest score", new MenuShortcut(KeyEvent.VK_S));
        MenuItem menuPlayer = new MenuItem("Sort by player name", new MenuShortcut(KeyEvent.VK_P));
        menu.add(menuScore);
        menu.add(menuPlayer);
//        menuBar.add(menu);
        menuBar.setFont(new Font("sans-serif", Font.BOLD, 12));
        this.setMenuBar(menuBar);
        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        JScrollPane tablePane = new JScrollPane(table);
        getContentPane().add(tablePane, BorderLayout.CENTER);
        Button button = new Button("Show results only for selected player");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        getContentPane().add(button, BorderLayout.SOUTH);
        getContentPane().repaint();
        getContentPane().revalidate();

    }
}
