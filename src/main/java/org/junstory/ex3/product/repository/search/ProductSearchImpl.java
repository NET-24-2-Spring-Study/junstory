package org.junstory.ex3.product.repository.search;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.junstory.ex3.product.dto.ProductDTO;
import org.junstory.ex3.product.dto.ProductListDTO;
import org.junstory.ex3.product.entity.ProductEntity;
import org.junstory.ex3.product.entity.QProductEntity;
import org.junstory.ex3.product.entity.QProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() {
        super(ProductEntity.class);
    }

    @Override
    public Page<ProductListDTO> list(Pageable pageable) {
        QProductEntity productEntity = QProductEntity.productEntity;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<ProductEntity> query = from(productEntity);
        query.leftJoin(productEntity.images, productImage);

        //where product Image.idx = 0
        query.where(productImage.idx.eq(0));

        //Long pno, String pname, int price, String writer, String productImage
        JPQLQuery<ProductListDTO> dtojpqlQuery = query.select(Projections.bean(
                ProductListDTO.class,
                productEntity.pno,
                productEntity.pname,
                productEntity.price,
                productEntity.writer,
                productImage.fileName.as("productImage")
        ));

        this.getQuerydsl().applyPagination(pageable,dtojpqlQuery);

        java.util.List<ProductListDTO> dtoList = dtojpqlQuery.fetch();

        long count = dtojpqlQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }

    @Override
    public Page<ProductDTO> listWithAllImages(Pageable pageable) {
        QProductEntity productEntity = QProductEntity.productEntity;

        JPQLQuery<ProductEntity> query = from(productEntity);

        this.getQuerydsl().applyPagination(pageable,query);

        List<ProductEntity> entityList = query.fetch();

        long count = query.fetchCount();

//        for (ProductEntity entity : entityList){
//            System.out.println(entity);
//            System.out.println(entity.getImages());
//            System.out.println("-------------------------------------");
//        }

        List<ProductDTO> dtoList = entityList.stream().map(ProductDTO::new).toList();

        return new PageImpl<>(dtoList, pageable, count);
    }

    @Override
    public Page<ProductDTO> listFetchAllImages(Pageable pageable) {

        QProductEntity productEntity = QProductEntity.productEntity;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<ProductEntity> query = from(productEntity);
        query.leftJoin(productEntity.images, productImage).fetchJoin();

        this.getQuerydsl().applyPagination(pageable,query);
        List<ProductEntity> entityList = query.fetch();

        List<ProductDTO> dtoList = entityList.stream().map(ProductDTO::new).toList();

        long count = query.fetchCount();

//        for (ProductEntity entity : entityList) {
//            System.out.println(entity);
//            System.out.println(entity.getImages());
//            System.out.println("----------------------------------------");
//        }

        return new PageImpl<>(dtoList, pageable, count);
    }
}
