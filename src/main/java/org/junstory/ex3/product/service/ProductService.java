package org.junstory.ex3.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junstory.ex3.product.dto.PageRequestDTO;
import org.junstory.ex3.product.dto.ProductDTO;
import org.junstory.ex3.product.dto.ProductListDTO;
import org.junstory.ex3.product.entity.ProductEntity;
import org.junstory.ex3.product.exception.ProductExceptions;
import org.junstory.ex3.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductDTO register(ProductDTO productDTO) {

        try {
            log.info("register............");
            log.info(productDTO);

            ProductEntity productEntity = productDTO.toEntity();

            productRepository.save(productEntity);

            return new ProductDTO(productEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw ProductExceptions.PRODUCT_NOT_REGISTERED.get();
        }//end catch

    }

    @Transactional(readOnly = true)
    public ProductDTO read(Long pno){
        java.util.Optional<ProductEntity> result = productRepository.getProduct(pno);

        ProductEntity productEntity = result.orElseThrow(ProductExceptions.PRODUCT_NOT_FOUND::get);

        return new ProductDTO(productEntity);
    }

    public void remove(Long pno){
        log.info("remove....................");
        log.info(pno);

        java.util.Optional<ProductEntity> result = productRepository.findById(pno);

        ProductEntity productEntity = result.orElseThrow(ProductExceptions.PRODUCT_NOT_FOUND::get);

        try{
            productRepository.delete(productEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw ProductExceptions.PRODUCT_NOT_REMOVED.get();
        }//end catch
    }

    public ProductDTO modify(ProductDTO productDTO) {
        log.info("modify...................");
        log.info(productDTO);

        java.util.Optional<ProductEntity> result = productRepository.findById(productDTO.getPno());

        ProductEntity productEntity = result.orElseThrow(ProductExceptions.PRODUCT_NOT_FOUND::get);

        try{
            //상품 정보 수정
            productEntity.changePrice(productDTO.getPrice());
            productEntity.changeTitle(productDTO.getPname());

            //기존 이미지들 삭제
            productEntity.clearImages();

            //새로운 이미지들 추가
            java.util.List<String> fileNames = productDTO.getImageList();

            if(fileNames != null && !fileNames.isEmpty()){
                fileNames.forEach(productEntity::addImage);
            }

            productRepository.save(productEntity);

            return new ProductDTO(productEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw ProductExceptions.PRODUCT_NOT_MODIFIED.get();
        } //end catch
    }

    public Page<ProductListDTO> getList(PageRequestDTO pageRequestDTO){
        log.info("getList...................");
        log.info(pageRequestDTO);

        try{
            Pageable pageable = pageRequestDTO.getPageable(Sort.by("pno").descending());

            return productRepository.list(pageable);
        } catch (Exception e){
            log.error(e.getMessage());
            throw ProductExceptions.PRODUCT_NOT_FETCHED.get();
        }//end catch
    }
}
