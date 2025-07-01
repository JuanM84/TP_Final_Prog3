package frp.utn.tp.base.ui.view;

import frp.utn.tp.base.ui.component.ViewToolbar;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

/**
 * This view shows up when a user navigates to the root ('/') of the application.
 */
@Route
@PermitAll // When security is enabled, allow all authenticated users
public final class MainView extends Main {

    MainView() {
        addClassName(LumoUtility.Padding.MEDIUM);
        add(new ViewToolbar("Trabajo Practico"));
        
        add(new H3("Programación 3"));
        
        add(new H5("Docente:"));
        add(new Div("Lic. Ernesto Zapata Icart"));
        add(new Div("Ing. Mariano Carpio"));
        add(new Div("Lic. Rodolfo Schönals-Fisher"));

        add(new H5("Alumnos:"));
        add(new Div("Gonzalez, Juan Manuel"));
        add(new Div("Hepp, Pablo"));
    }

    /**
     * Navigates to the main view.
     */
    public static void showMainView() {
        UI.getCurrent().navigate(MainView.class);
    }
}
