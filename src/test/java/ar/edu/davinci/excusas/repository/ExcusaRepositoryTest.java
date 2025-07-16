package ar.edu.davinci.excusas.repository;

import ar.edu.davinci.excusas.model.entities.EmpleadoEntity;
import ar.edu.davinci.excusas.model.entities.ExcusaEntity;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ExcusaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ExcusaRepository excusaRepository;

    @Test
    public void testFindByEmpleadoLegajo() {
        EmpleadoEntity empleado = new EmpleadoEntity("Juan Pérez Test", "juan.test@excusas-sa.com", 9001);
        entityManager.persistAndFlush(empleado);

        ExcusaEntity excusa = new ExcusaEntity(MotivoExcusa.QUEDARSE_DORMIDO, "ExcusaTrivial", empleado);
        entityManager.persistAndFlush(excusa);

        List<ExcusaEntity> excusas = excusaRepository.findByEmpleadoLegajo(9001);

        assertThat(excusas).hasSize(1);
        assertThat(excusas.get(0).getMotivo()).isEqualTo(MotivoExcusa.QUEDARSE_DORMIDO);
        assertThat(excusas.get(0).getEmpleado().getLegajo()).isEqualTo(9001);
    }




}
