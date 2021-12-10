package uni.fmi.masters.webapplication.repositories;



import org.springframework.data.jpa.repository.JpaRepository;

import uni.fmi.masters.webapplication.entities.DiscountEntity;

public interface DiscountRepository extends JpaRepository<DiscountEntity, Integer>{

}
