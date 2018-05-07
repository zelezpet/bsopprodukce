/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import ml.bma.bsop.backend.data.entity.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author ironman
 * @param <T>
 */

public abstract class CrudService<T extends AbstractEntity> implements Serializable {

	protected abstract JpaRepository<T, Long> getRepository();

	public T save(T entity) {
		return getRepository().save(entity);
	}

	public void delete(long id) {
		getRepository().delete(id);
	}
        
        public void delete(T entity) {
            delete(entity.getId());
        }

	public T load(long id) {
		return getRepository().findOne(id);
	}

	public abstract long countAnyMatching(Optional<String> filter);

	public abstract List<T> findAnyMatching(Optional<String> filter);
        
        public abstract List<T> findAll();

}