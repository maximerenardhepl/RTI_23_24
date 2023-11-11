package Modele;

import java.util.EventObject;

public class ThSocketEvent extends EventObject {

    private boolean isSocketInit;
    public boolean isInitSuccessful() { return isSocketInit;}
    public ThSocketEvent(Object source) {
        super(source);
        isSocketInit = false;
    }
    public ThSocketEvent(Object source, boolean isSocketInit) {
        super(source);
        this.isSocketInit = isSocketInit;
    }
}
