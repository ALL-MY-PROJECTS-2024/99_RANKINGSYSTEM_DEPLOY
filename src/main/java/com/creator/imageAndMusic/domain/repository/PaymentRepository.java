package com.creator.imageAndMusic.domain.repository;


import com.creator.imageAndMusic.domain.entity.PaymentImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentImage,String> {
}
