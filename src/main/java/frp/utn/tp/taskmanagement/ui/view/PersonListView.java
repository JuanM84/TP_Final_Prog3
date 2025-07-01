package frp.utn.tp.taskmanagement.ui.view;

import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import frp.utn.tp.base.ui.component.ViewToolbar;
import frp.utn.tp.taskmanagement.domain.Person;
import frp.utn.tp.taskmanagement.service.PersonService;
import frp.utn.tp.taskmanagement.service.TaskService;
import jakarta.annotation.security.PermitAll;

@Route("person-list")
@PageTitle("Lista de Personas")
@Menu(order = 0, icon = "vaadin:users", title = "Personas")
@PermitAll
public class PersonListView extends Main {

    private Binder<Person> binder = new BeanValidationBinder<>(Person.class);
    private TextField lastName = new TextField("Apellido");
    private TextField firstName = new TextField("Nombre");
    private TextField dni = new TextField("DNI");
    private Button edit = new Button("Nuevo", e -> {
        habilitarCampos();
    });
    private Button save = new Button("Guardar", e -> {
        createPerson();
    });
    private Grid<Person> personGrid;
    private PersonService personService;
    private TaskService taskService;

    public PersonListView(PersonService personService, TaskService taskService) {
        
        this.personService = personService;
        this.taskService = taskService;
    
        binder.bindInstanceFields(this);

        add(new ViewToolbar("Administrador de Personas"));

        VerticalLayout content = new VerticalLayout();

        HorizontalLayout formPersona = new HorizontalLayout();
        formPersona.setSpacing(true);
        formPersona.getStyle().set("gap", "5px");
        lastName.setReadOnly(true);
        firstName.setReadOnly(true);
        dni.setReadOnly(true);

        formPersona.add(firstName, lastName, dni);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.getStyle().set("gap", "5px");
        edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        buttons.add(edit, save);

        personGrid = new Grid<>();
        personGrid.setItems(query -> personService.list(toSpringPageRequest(query)).stream());
        personGrid.addColumn(Person::getId).setHeader("ID");
        personGrid.addColumn(Person::getNombre).setHeader("Nombre");
        personGrid.addColumn(Person::getApellido).setHeader("Apellido");
        personGrid.addColumn(Person::getDni).setHeader("DNI");
        personGrid.addComponentColumn(person -> {
            Button delete = new Button(new Icon(VaadinIcon.TRASH), event -> {
                Dialog confirmDialog = new Dialog();
                confirmDialog.setHeaderTitle(person.toString());
                confirmDialog.add("¿Estas seguro que deseas eliminar esta persona?");

                Button deleteButton = new Button("Borrar", (e) -> {
                    taskService.deleteByPerson(person);
                    deletePerson(person.getId());
                    confirmDialog.close();
                });
                deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                deleteButton.getStyle().set("margin-right", "auto");
                confirmDialog.getFooter().add(deleteButton);

                Button cancelButton = new Button("Cancelar", (e) -> confirmDialog.close());
                cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                confirmDialog.getFooter().add(cancelButton);

                confirmDialog.open();

            });
            delete.addThemeVariants(ButtonVariant.LUMO_ICON);
            delete.setAriaLabel("Borrar");
            delete.setTooltipText("Eliminar");
            return delete;
        }).setHeader("Acciones");

        setSizeFull();
        addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Padding.MEDIUM, LumoUtility.Gap.SMALL);

        content.add(formPersona, buttons, personGrid);
        add(content);

    }

    // Crear una Persona
    private void createPerson() {
        personService.createPerson(firstName.getValue(), lastName.getValue(), dni.getValue());
        personGrid.getDataProvider().refreshAll();
        firstName.clear();
        lastName.clear();
        dni.clear();
        Notification.show("Persona Agregada", 3000, Notification.Position.BOTTOM_START)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    // Eliminar una Persona
    private void deletePerson(Long id) {
        personService.deletePerson(id);
        personGrid.getDataProvider().refreshAll();
        Notification.show("Persona Eliminada", 3000, Notification.Position.BOTTOM_START)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    // Habilitar los campos para Ingresar un nueva persona
    private void habilitarCampos() {
        lastName.setReadOnly(false);
        firstName.setReadOnly(false);
        dni.setReadOnly(false);
    }

}
