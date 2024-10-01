package org.junstory.ex3.product.repository.search;

import org.junstory.ex3.product.dto.ProductDTO;
import org.junstory.ex3.product.dto.ProductListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductSearch {
    Page<ProductListDTO> list(Pageable pageable);

    Page<ProductDTO> listWithAllImages(Pageable pageable);

    Page<ProductDTO> listFetchAllImages(Pageable pageable);
}
