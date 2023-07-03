package ru.tinkoff.storePrime.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.tinkoff.storePrime.models.Category;
import ru.tinkoff.storePrime.models.Product;

import java.util.Collection;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.seller.id = :sellerId")
    List<Product> findAllBySellerId(@Param("sellerId") Long sellerId);


    @Query("select p from Product  p where p.seller.id = :sellerId and  p.price <= :price and :categories member of p.categories")
    List<Product> findBySellerAndPriceAndCategory(@Param("sellerId") Long sellerId, @Param("price") Double price, @Param("categories") Collection<Category> categories);

    @Query("select p from Product  p where p.seller.id = :sellerId and p.price <= :price")
    List<Product> findBySellerAndPrice(Long sellerId, Double price);

    @Query("select p from Product  p where p.seller.id = :sellerId and  p.price <= :price and :categories member of p.categories")
    Page<Product> findPageBySellerAndPriceAndCategory(Pageable pageable, @Param("sellerId") Long sellerId, @Param("price") Double price, @Param("categories") Collection<Category> categories);

    @Query("select p from Product  p where p.seller.id = :sellerId and p.price <= :price")
    Page<Product> findPageBySellerAndPrice(PageRequest pageRequest, @Param("sellerId") Long sellerId, @Param("price") Double price);

    @Query("select p from Product  p where p.price <= :price")
    List<Product> findByPrice(Double price);

    @Query("select p from Product  p where p.price <= :price and :categories member of p.categories")
    List<Product> findByPriceAndCategory(Double price, Collection<Category> categories);

    @Query("select p from Product  p  where :categories member of p.categories")
    List<Product> findByCategory(Collection<Category> categories);

    @Query("select p from Product  p where p.price <= :price and :categories member of p.categories")
    Page<Product> findPageByPriceAndCategory(PageRequest pageRequest, Double price, Collection<Category> categories);

    Page<Product> findPageByPrice(PageRequest pageRequest, Double price);

    @Query("select p from Product  p  where :categories member of p.categories")
    Page<Product> findPageByCategory(Collection<Category> categories);

    @Query("select p from Product p")
    Page<Product> findPage(PageRequest pageRequest);
}
