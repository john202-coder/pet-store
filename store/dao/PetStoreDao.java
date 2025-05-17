package pet.store.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pet.store.entity.PetStore;

public interface PetStoreDao extends JpaRepository<PetStore, Long> {

    @Query("SELECT p FROM PetStore p LEFT JOIN FETCH p.customers LEFT JOIN FETCH p.employees WHERE p.petStoreId = :petStoreId")
    Optional<PetStore> findByIdWithRelations(@Param("petStoreId") Long petStoreId);
}
