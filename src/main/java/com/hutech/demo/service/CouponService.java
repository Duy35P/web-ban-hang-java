package com.hutech.demo.service;

import com.hutech.demo.model.Coupon;
import com.hutech.demo.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {
    private final CouponRepository couponRepository;

    public Coupon createCoupon(Coupon coupon) {
        if (couponRepository.existsByCode(coupon.getCode())) {
            throw new RuntimeException("Mã giảm giá đã tồn tại: " + coupon.getCode());
        }
        return couponRepository.save(coupon);
    }

    public Optional<Coupon> findByCode(String code) {
        return couponRepository.findByCode(code);
    }

    /**
     * Áp dụng mã giảm giá
     * @return số tiền được giảm, hoặc -1 nếu mã không hợp lệ, -2 nếu không đủ điều kiện
     */
    public double applyCoupon(String code, double orderTotal) {
        Optional<Coupon> opt = couponRepository.findByCode(code.toUpperCase().trim());
        if (opt.isEmpty()) return -1;

        Coupon coupon = opt.get();
        if (!coupon.isValid()) return -1;
        if (orderTotal < coupon.getMinOrderAmount()) return -2;

        double discount = coupon.calculateDiscount(orderTotal);

        // Tăng số lần sử dụng
        coupon.setUsedCount(coupon.getUsedCount() + 1);
        couponRepository.save(coupon);

        return discount;
    }

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Optional<Coupon> getCouponById(Long id) {
        return couponRepository.findById(id);
    }

    public void updateCoupon(Coupon coupon) {
        Coupon existing = couponRepository.findById(coupon.getId())
            .orElseThrow(() -> new RuntimeException("Coupon not found: " + coupon.getId()));
        existing.setCode(coupon.getCode());
        existing.setDiscountType(coupon.getDiscountType());
        existing.setDiscountValue(coupon.getDiscountValue());
        existing.setMinOrderAmount(coupon.getMinOrderAmount());
        existing.setMaxUsage(coupon.getMaxUsage());
        existing.setStartDate(coupon.getStartDate());
        existing.setEndDate(coupon.getEndDate());
        existing.setActive(coupon.isActive());
        couponRepository.save(existing);
    }

    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }
}
