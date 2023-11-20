package Vue.JTableModel;

import Classes.Article;
import Classes.Facture;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class JTableModelFactureDetaillee extends AbstractTableModel {
    private String[] columnNames = {"Article", "Quantite", "Prix Unitaire"};
    private ArrayList<Article> data;

    public JTableModelFactureDetaillee(ArrayList<Article> data) {
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        Article art = data.get(rowIndex);
        switch (columnIndex) {
            case 0: return art.getIntitule();
            case 1: return art.getQuantite();
            case 2: return String.valueOf(art.getPrix());
            default: return null;
        }
    }

    @Override
    public String getColumnName(int c) { return columnNames[c]; }

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
        if(size > 0) {
            data.clear();
            fireTableRowsDeleted(0, size-1);
        }
    }

    public void updateDataSource(ArrayList<Article> newData) {
        this.data = newData;
        fireTableDataChanged(); //Permet de notifier la JTable que la source de données a été modifiée.
    }
}
