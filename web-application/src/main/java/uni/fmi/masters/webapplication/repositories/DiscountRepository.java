package uni.fmi.masters.webapplication.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uni.fmi.masters.webapplication.entities.DiscountEntity;
import uni.fmi.masters.webapplication.entities.ProductEntity;

public interface DiscountRepository extends JpaRepository<DiscountEntity, Integer>{
	
	//List<DiscountEntity> findByProduct(ProductEntity product);

}
