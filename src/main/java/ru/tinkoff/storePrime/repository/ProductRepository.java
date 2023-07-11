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
import java.util.Optional;
import java.util.OptionalInt;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        select p from Product p 
        where p.seller.id = :sellerId
        """)
    List<Product> findAllBySellerId(@Param("sellerId") Long sellerId);

    @Query("""
        select p from Product p 
        where p.seller.id = :sellerId 
        and :minPrice <= p.price 
        and p.price <= :maxPrice 
        and :categories member of p.categories
        """)
    List<Product> findBySellerAndPriceAndCategory(
            @Param("sellerId") Long sellerId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("categories") Collection<Category> categories
    );

    @Query("""
        select p from Product p 
        where p.seller.id = :sellerId 
        and :minPrice <= p.price 
        and p.price <= :maxPrice
        """)
    List<Product> findBySellerAndPrice(
            Long sellerId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );


    @Query("""
        select p from Product p 
        where p.seller.id = :sellerId 
        and :minPrice <= p.price 
        and p.price <= :maxPrice 
        and :categories member of p.categories
        """)
    Page<Product> findPageBySellerAndPriceAndCategory(
            Pageable pageable,
            @Param("sellerId") Long sellerId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("categories") Collection<Category> categories
    );

    @Query("""
        select p from Product p 
        where p.seller.id = :sellerId 
        and :minPrice <= p.price 
        and p.price <= :maxPrice
        """)
    Page<Product> findPageBySellerAndPrice(
            PageRequest pageRequest,
            @Param("sellerId") Long sellerId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );

    @Query("""
        select p from Product p 
        where :minPrice <= p.price 
        and p.price <= :maxPrice
        """)
    List<Product> findByPrice(
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );

    @Query("""
        select p from Product p 
        where :minPrice <= p.price 
        and p.price <= :maxPrice 
        and :categories member of p.categories
        """)
    List<Product> findByPriceAndCategory(
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Collection<Category> categories
    );

    @Query("""
        select p from Product p 
        where :categories member of p.categories
        """)
    List<Product> findByCategory(
            Collection<Category> categories
    );

    @Query("""
        select p from Product p 
        where :minPrice <= p.price 
        and p.price <= :maxPrice 
        and :categories member of p.categories
        """)
    Page<Product> findPageByPriceAndCategory(
            PageRequest pageRequest,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Collection<Category> categories
    );

    @Query("""
        select p from Product p 
        where :minPrice <= p.price 
        and p.price <= :maxPrice
        """)
    Page<Product> findPageByPrice(
            PageRequest pageRequest,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );

    @Query("""
        select p from Product p 
        where :categories member of p.categories
        """)
    Page<Product> findPageByCategory(
            PageRequest pageRequest,
            @Param("categories") Collection<Category> categories
    );


    @Query("""
        select p from Product p
        """)
    Page<Product> findPage(PageRequest pageRequest);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM product WHERE product.title ILIKE :content"
    )
    List<Product> findAllByContent(
            @Param("content") String content
    );

    @Query(value = "SELECT * FROM product ORDER BY random() LIMIT 1", nativeQuery = true)
    Optional<Product> findRandomProduct();

    @Query(value = "SELECT * FROM product ORDER BY random() LIMIT :amount", nativeQuery = true)
    List<Product> findRandomProducts(@Param("amount") int amount);

    @Query(nativeQuery = true, value = """
        select * from product inner join product_category on product.id = product_category.product_id where product.title ilike :content and product_category.category_id = :categoryId
        """)
    List<Product> findAllByContentAndCategory(@Param("content") String content, @Param("categoryId") Long categoryId);
}
