package Modele;

public class VESPAP {

    private static VESPAP instance;

    private VESPAP() {

    }

    public static VESPAP getInstance() {
        if (instance == null) {
            instance = new VESPAP();
        }
        return instance;
    }

    public void Login() {

    }

    public void Logout() {

    }

    public void GetFactures() {

    }

    public void PayFacture() {

    }
}
