package org.junstory.ex3.product.repository;

import org.junstory.ex3.product.dto.ProductDTO;
import org.junstory.ex3.product.entity.ProductEntity;
import org.junstory.ex3.product.repository.search.ProductSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductSearch {

//    @EntityGraph(attributePaths = {"images"}, type = EntityGraph.EntityGraphType.FETCH)
//    @Query("select p from ProductEntity p where p.pno = :pno")
//    Optional<ProductEntity> getProduct(@Param("pno") Long pno);

    @Query("select p from ProductEntity p join fetch p.images pi where p.pno = :pno")
    Optional<ProductEntity> getProduct(@Param("pno") Long pno);

    @Query("select p from ProductEntity p join fetch p.images pi where p.pno = :pno")
    Optional<ProductDTO> getProductDTO(@Param("pno")Long pno);

    @Query("select p from ProductEntity p join fetch p.images pi")
    Page<ProductDTO> listQuery(Pageable pageable);
}
