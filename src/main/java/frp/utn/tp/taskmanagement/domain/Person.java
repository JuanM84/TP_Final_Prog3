package frp.utn.tp.taskmanagement.domain;

import java.util.LinkedList;
import java.util.List;

import frp.utn.tp.base.domain.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="person")
public class Person extends AbstractEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotBlank
    private String dni;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @OneToMany(mappedBy = "person")
    private List<Task> tasks = new LinkedList<>();
    
    public Long getId(){
        return id;
    }

    public String getDni(){
        return dni;
    }

    public String getNombre(){
        return nombre;
    } 

    public String getApellido(){
        return apellido;
    }

    public void setDni(String dni){
        this.dni = dni;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public void setApellido(String apellido){
        this.apellido = apellido;
    }

    public List<Task> getTasks(){
        return tasks;
    }

    public void setTask(List<Task> tasks){
        this.tasks = tasks;
    }

    @Override
    public String toString(){
        return nombre + ' ' + apellido;
    }
}
