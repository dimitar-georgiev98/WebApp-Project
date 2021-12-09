package uni.fmi.masters.webapplication.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.fmi.masters.webapplication.entities.UserEntity;
import uni.fmi.masters.webapplication.entities.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer>{

	List<ProductEntity> findByUser(UserEntity user);
	
}
