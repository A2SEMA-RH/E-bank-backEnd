package net.assema.ebankingbackend.repositories;

import net.assema.ebankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer>findByNameContains(String keyword);

//  on peut le faire de cette maniere aussi


    @Query("SELECT c FROM Customer c WHERE c.name LIKE:kw")
    List<Customer>searchCustomerByName(@Param("kw") String keyword);
}
