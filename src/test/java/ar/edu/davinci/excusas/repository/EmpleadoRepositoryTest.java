package ar.edu.davinci.excusas.repository;

import ar.edu.davinci.excusas.model.entities.EmpleadoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmpleadoRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private EmpleadoRepository empleadoRepository;
    
    @Test
    public void testFindByLegajo() {
        // Given
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setLegajo(1001);
        empleado.setNombre("Juan Pérez");
        empleado.setEmail("juan.perez@excusas-sa.com");
        entityManager.persistAndFlush(empleado);
        
        // When
        Optional<EmpleadoEntity> found = empleadoRepository.findByLegajo(1001);
        
        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("Juan Pérez");
        assertThat(found.get().getEmail()).isEqualTo("juan@test.com");
    }


    
    @Test
    public void testFindByEmail() {
        // Given
        EmpleadoEntity empleado = new EmpleadoEntity("María García", "maria@test.com", 1002);
        entityManager.persistAndFlush(empleado);
        
        // When
        Optional<EmpleadoEntity> found = empleadoRepository.findByEmail("maria@test.com");
        
        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("María García");
        assertThat(found.get().getLegajo()).isEqualTo(1002);
    }
    
    @Test
    public void testExistsByLegajo() {
        // Given
        EmpleadoEntity empleado = new EmpleadoEntity("Carlos López", "carlos@test.com", 1003);
        entityManager.persistAndFlush(empleado);
        
        // When & Then
        assertThat(empleadoRepository.existsByLegajo(1003)).isTrue();
        assertThat(empleadoRepository.existsByLegajo(9999)).isFalse();
    }
    
    @Test
    public void testExistsByEmail() {
        // Given
        EmpleadoEntity empleado = new EmpleadoEntity("Ana Martínez", "ana@test.com", 1004);
        entityManager.persistAndFlush(empleado);
        
        // When & Then
        assertThat(empleadoRepository.existsByEmail("ana@test.com")).isTrue();
        assertThat(empleadoRepository.existsByEmail("noexiste@test.com")).isFalse();
    }
}
