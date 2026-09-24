package pe.edu.upeu.MatriculaBackend.service.generic;

import java.util.List;

public interface CrudService<RQ, RS, ID> {
    RS create(RQ request);
    RS update(ID id, RQ request);
    RS read(ID id);
    void delete(ID id);
    List<RS> readAll();
}