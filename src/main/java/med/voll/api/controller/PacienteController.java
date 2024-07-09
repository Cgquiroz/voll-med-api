package med.voll.api.controller;


import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaPaciente> registrarPaciente(@RequestBody @Valid DatosRegistroPaciente datosRegistroPaciente,
                                                                    UriComponentsBuilder uriComponentsBuilder){
      Paciente paciente = pacienteRepository.save(new Paciente(datosRegistroPaciente));
      DatosRespuestaPaciente datosRespuestaPaciente = new DatosRespuestaPaciente(paciente.getId(), paciente.getNombre(),
              paciente.getEmail(), paciente.getTelefono(), paciente.getDocumentoIdentidad(),
              new DatosDireccion(paciente.getDireccion().getCalle(),paciente.getDireccion().getDistrito(),
                      paciente.getDireccion().getCiudad(),paciente.getDireccion().getNumero(),
                      paciente.getDireccion().getComplemento()));
        URI url = uriComponentsBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaPaciente);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoPaciente>> listadoPacientes(@PageableDefault(size = 2) Pageable paginacion) {
        return ResponseEntity.ok(pacienteRepository.findByActivoTrue(paginacion).map(DatosListadoPaciente::new));
        //return pacienteRepository.findAll(paginacion).map(DatosListadoPaciente::new);
    }

    @PutMapping
    @Transactional
    public ResponseEntity actualizarPaciente(@RequestBody @Valid DatosActualizacionPaciente datosActualizacionPaciente) {
        Paciente paciente = pacienteRepository.getReferenceById(datosActualizacionPaciente.id());
        paciente.atualizarInformacionPaciente(datosActualizacionPaciente);
        return ResponseEntity.ok(new DatosRespuestaPaciente(paciente.getId(), paciente.getNombre(),
                paciente.getEmail(), paciente.getTelefono(), paciente.getDocumentoIdentidad(),
                new DatosDireccion(paciente.getDireccion().getCalle(),paciente.getDireccion().getDistrito(),
                        paciente.getDireccion().getCiudad(),paciente.getDireccion().getNumero(),
                        paciente.getDireccion().getComplemento())));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void remover(@PathVariable Long id) {
        var paciente = pacienteRepository.getReferenceById(id);
        paciente.inactivar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaPaciente> retornaDatosPaciente(@PathVariable Long id){
        Paciente paciente=pacienteRepository.getReferenceById(id);
        var datosPaciente = new DatosRespuestaPaciente(paciente.getId(), paciente.getNombre(), paciente.getEmail(),
                paciente.getTelefono(),paciente.getDocumentoIdentidad(),
                new DatosDireccion(paciente.getDireccion().getCalle(),paciente.getDireccion().getDistrito(),
                        paciente.getDireccion().getCiudad(),paciente.getDireccion().getNumero(),
                        paciente.getDireccion().getComplemento()));
        return ResponseEntity.ok(datosPaciente);
    }
}
