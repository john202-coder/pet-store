package pet.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pet.store.entity.Customer;

@Repository
public interface CustomerDao extends JpaRepository<Customer, Long> {
}
