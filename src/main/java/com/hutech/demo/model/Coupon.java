package com.hutech.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Mã giảm giá không được để trống")
    @Column(unique = true)
    private String code;

    @NotBlank(message = "Loại giảm giá không được để trống")
    private String discountType; // PERCENT or FIXED

    @Min(value = 0, message = "Giá trị giảm không được âm")
    private double discountValue;

    @Min(value = 0, message = "Giá trị đơn tối thiểu không được âm")
    private double minOrderAmount;

    private int maxUsage;

    private int usedCount;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean active = true;

    public boolean isValid() {
        if (!active) return false;
        if (maxUsage > 0 && usedCount >= maxUsage) return false;
        LocalDateTime now = LocalDateTime.now();
        if (startDate != null && now.isBefore(startDate)) return false;
        if (endDate != null && now.isAfter(endDate)) return false;
        return true;
    }

    public double calculateDiscount(double orderTotal) {
        if (!isValid()) return 0;
        if (orderTotal < minOrderAmount) return 0;
        if ("PERCENT".equals(discountType)) {
            return Math.round(orderTotal * discountValue / 100);
        } else {
            return Math.min(discountValue, orderTotal);
        }
    }
}
