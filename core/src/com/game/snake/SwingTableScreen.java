package com.game.snake;

import jdk.javadoc.internal.doclets.formats.html.markup.Table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class SwingTableScreen extends JFrame {
    private final Main game;
    public SwingTableScreen(final Main game){
        this.game = game;
        setSize(850, 300);
        setLocationRelativeTo(null);
        setVisible(true);


        MenuBar menuBar = new MenuBar();
        PopupMenu menu = new PopupMenu("Options");
        MenuItem menuScore = new MenuItem("Sort by highest score", new MenuShortcut(KeyEvent.VK_S));
        MenuItem menuPlayer = new MenuItem("Sort by player name", new MenuShortcut(KeyEvent.VK_P));
        menu.add(menuScore);
        menu.add(menuPlayer);
//        menuBar.add(menu);
        menuBar.setFont(new Font("sans-serif", Font.BOLD, 12));
        this.setMenuBar(menuBar);
        final JTable generalTable = getTableModel();
        JScrollPane jScrollPane = new JScrollPane(generalTable);
        getContentPane().add(jScrollPane, BorderLayout.CENTER);
        Button button = new Button("Show results for only selected player");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameScreen.buttonS.play();
                if(generalTable.getSelectedColumn() != 0) return;
                String username = (String) generalTable.getValueAt(generalTable.getSelectedRow(), 0);
                final JTable userSpecialTable = getTableModel(username);
                getContentPane().removeAll();
                JScrollPane jScrollPane = new JScrollPane(userSpecialTable);
                getContentPane().add(jScrollPane, BorderLayout.CENTER);
                Button button = new Button("Show results for all");
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        GameScreen.buttonS.play();
                        SwingTableScreen.this.dispose();
                        new SwingTableScreen(game);
                    }
                });
                getContentPane().add(button, BorderLayout.SOUTH);
                getContentPane().repaint();
                getContentPane().revalidate();

            }
        });
        getContentPane().add(button, BorderLayout.SOUTH);
        getContentPane().repaint();
        getContentPane().revalidate();

    }
    private JTable getTableModel(String userName){
        final DefaultTableModel tableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if(columnIndex == 4) return Boolean.class;
                if(columnIndex == 1 || columnIndex == 3) return Integer.class;
                return super.getColumnClass(columnIndex);
            }
        };
        String[] columnsHeader = {"Username", "Score", "Difficulty", "Snake size", "Math game mode", "Other information"};
        tableModel.setColumnIdentifiers(columnsHeader);
        for (Record record : game.records.getRecords()) {
            if(userName == null || record.userName.equals(userName)) {
                tableModel.addRow(new Object[]{record.userName, record.score, record.gameDifficulty, record.finalSnakeSize, record.isMathMode, record.gameInformation});
            }
        }
        final JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        return table;
    }
    private JTable getTableModel(){
        return getTableModel(null);
    }
}
