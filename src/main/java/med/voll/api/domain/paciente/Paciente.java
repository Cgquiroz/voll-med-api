package med.voll.api.domain.paciente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.direccion.Direccion;

@Table(name="pacientes")
@Entity(name = "Paciente")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String documentoIdentidad;
    private Boolean activo;
    @Embedded
    private Direccion direccion;

    public Paciente(DatosRegistroPaciente datosRegistroPaciente) {
        this.activo=true;
        this.nombre = datosRegistroPaciente.nombre();
        this.email = datosRegistroPaciente.email();
        this.telefono = datosRegistroPaciente.telefono();
        this.documentoIdentidad = datosRegistroPaciente.documentoIdentidad();
        this.direccion = new Direccion(datosRegistroPaciente.direccion());
    }

    public void atualizarInformacionPaciente(DatosActualizacionPaciente datosActualizacionPaciente) {
        if(datosActualizacionPaciente.nombre()!=null){
            this.nombre=datosActualizacionPaciente.nombre();
        }
        if(datosActualizacionPaciente.telefono()!=null){
            this.telefono=datosActualizacionPaciente.telefono();
        }
        if (datosActualizacionPaciente.direccion()!=null){
            this.direccion=direccion.actualizarDatos(datosActualizacionPaciente.direccion());
        }
    }

    public void inactivar() {
        this.activo=false;
    }
}
