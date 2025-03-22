package com.practice.oauth2_prac.product;

import com.practice.oauth2_prac.product.entity.Product;
import com.practice.oauth2_prac.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            productRepository.save(Product.builder().name("몬스터 에너지 오리지널").price(2000).build());
            productRepository.save(Product.builder().name("몬스터 울트라 화이트").price(2200).build());
            productRepository.save(Product.builder().name("몬스터 파이프라인 펀치").price(2500).build());
            productRepository.save(Product.builder().name("몬스터 망고 로코").price(2500).build());
            System.out.println("✅ 샘플 Product 데이터가 삽입되었습니다.");
        }
    }
}