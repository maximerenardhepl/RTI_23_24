package Vue.JTableModel;

import Classes.Data.Facture;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class JTableModelFactures extends AbstractTableModel {

    private String[] columnNames = {"Date", "Montant", "Payé"};
    private ArrayList<Facture> data;

    public JTableModelFactures(ArrayList<Facture> data) { this.data = data; }

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
        Facture f = data.get(rowIndex);
        switch(columnIndex) {
            case 0: return f.getDate();
            case 1: return String.valueOf(f.getMontant());
            case 2: {
                if(f.isPaye()) {
                    return "Oui";
                }
                else return "Non";
            }
            default: return null;
        }
    }

    @Override
    public String getColumnName(int c) {
        return columnNames[c];
    }

    public void addRow(Facture f) {
        data.add(f);
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
        if(size > 0) {
            data.clear();
            fireTableRowsDeleted(0, size-1);
        }
    }

    public void updateDataSource(ArrayList<Facture> newData) {
        this.data = newData;
        fireTableDataChanged(); //Permet de notifier la JTable que la source de données a été modifiée.
    }
}
