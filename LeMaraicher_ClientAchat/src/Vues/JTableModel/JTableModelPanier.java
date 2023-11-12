package Vues.JTableModel;

import Modele.Article;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class JTableModelPanier extends AbstractTableModel {

    private String[] columnNames = {"Article", "Prix", "Quantité"};

    private ArrayList<Article> data;

    public JTableModelPanier(ArrayList<Article> data) {
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        super.setValueAt(value, row, col);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Article a = data.get(rowIndex);

        switch (columnIndex) {
            case 0 : return a.getIntitule();
            case 1 : return String.valueOf(a.getPrix());
            case 2 : return String.valueOf(a.getQuantite());
            default : return null;
        }
    }

    @Override
    public String getColumnName(int c) {
        return columnNames[c];
    }

    public void addRow(Article art) {
        data.add(art);
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }

    public void refreshRow(int indDebut, int indFin) {
        fireTableRowsUpdated(indDebut, indFin);
    }

    //Supprime l'article a l'indice i de l'ArrayList<Article> (le panier) et actualise l'affichage de la JTable
    public void removeRow(int indice) {
        if(indice >= 0 && indice < data.size()) {
            data.remove(indice);
            fireTableRowsDeleted(indice, indice);
        }
    }

    public void clearTable() {
        int size = data.size();
        data.clear();
        fireTableRowsDeleted(0, size-1);
    }
}
