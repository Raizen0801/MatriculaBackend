package pe.edu.upeu.MatriculaBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.MatriculaBackend.entity.Matricula;
import pe.edu.upeu.MatriculaBackend.enums.EstadoMatricula;

import java.util.List;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    boolean existsByEstudianteIdAndPeriodoAndEstado(Long estudianteId, String periodo, EstadoMatricula estado);
    List<Matricula> findByEstudianteIdOrderByFechaDesc(Long estudianteId);

    @Query("SELECT m FROM Matricula m WHERE m.estudiante.id = :estudianteId AND (:periodo IS NULL OR m.periodo = :periodo) ORDER BY m.fechaMatricula DESC")
    List<Matricula> findHistorialByEstudiante(@Param("estudianteId") Long estudianteId, @Param("periodo") String periodo);
}