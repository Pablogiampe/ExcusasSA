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
        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setLegajo(9001);
        empleado.setNombre("Juan Pérez Test");
        empleado.setEmail("juan.perez.test@excusas-sa.com");
        entityManager.persistAndFlush(empleado);

        Optional<EmpleadoEntity> found = empleadoRepository.findByLegajo(9001);

        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("Juan Pérez Test");
        assertThat(found.get().getEmail()).isEqualTo("juan.perez.test@excusas-sa.com");
    }



    @Test
    public void testFindByEmail() {
        EmpleadoEntity empleado = new EmpleadoEntity("María García Test", "maria.test@excusas-sa.com", 9002);
        entityManager.persistAndFlush(empleado);

        Optional<EmpleadoEntity> found = empleadoRepository.findByEmail("maria.test@excusas-sa.com");

        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("María García Test");
        assertThat(found.get().getLegajo()).isEqualTo(9002);
    }

    @Test
    public void testExistsByLegajo() {
        EmpleadoEntity empleado = new EmpleadoEntity("Carlos López Test", "carlos.test@excusas-sa.com", 9003);
        entityManager.persistAndFlush(empleado);

        assertThat(empleadoRepository.existsByLegajo(9003)).isTrue();
        assertThat(empleadoRepository.existsByLegajo(9999)).isFalse();
    }

    @Test
    public void testExistsByEmail() {
        EmpleadoEntity empleado = new EmpleadoEntity("Ana Martínez Test", "ana.test@excusas-sa.com", 9004);
        entityManager.persistAndFlush(empleado);

        assertThat(empleadoRepository.existsByEmail("ana.test@excusas-sa.com")).isTrue();
        assertThat(empleadoRepository.existsByEmail("noexiste@test.com")).isFalse();
    }
}
