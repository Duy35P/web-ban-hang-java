package com.hutech.demo;

import com.hutech.demo.model.*;
import com.hutech.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;

    @Override
    public void run(String... args) {
        // Tạo role ADMIN nếu chưa có
        RoleEntity adminRole = roleRepository.findByName("ADMIN");
        if (adminRole == null) {
            adminRole = new RoleEntity(null, "ADMIN", "Quản trị viên");
            roleRepository.save(adminRole);
        }

        // Tạo role USER nếu chưa có
        RoleEntity userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            userRole = new RoleEntity(null, "USER", "Người dùng");
            roleRepository.save(userRole);
        }

        // Tạo tài khoản admin nếu chưa có
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@hutech.edu.vn");
            admin.setPhone("0123456789");
            admin.setFullName("Quản trị viên");
            admin.getRoles().add(adminRole);
            userRepository.save(admin);

            System.out.println("=== Đã tạo tài khoản admin mặc định ===");
            System.out.println("Username: admin");
            System.out.println("Password: admin123");
            System.out.println("==========================================");
        }

        // Tạo tài khoản user mẫu
        if (userRepository.findByUsername("user1").isEmpty()) {
            User user1 = new User();
            user1.setUsername("user1");
            user1.setPassword(passwordEncoder.encode("user123"));
            user1.setEmail("user1@email.com");
            user1.setPhone("0987654321");
            user1.setFullName("Nguyễn Văn A");
            user1.setAddress("123 Nguyễn Huệ, Q1, TP.HCM");
            user1.getRoles().add(userRole);
            userRepository.save(user1);
        }

        // Tạo danh mục mẫu
        if (categoryRepository.count() == 0) {
            Category cat1 = new Category(); cat1.setName("Áo"); categoryRepository.save(cat1);
            Category cat2 = new Category(); cat2.setName("Quần"); categoryRepository.save(cat2);
            Category cat3 = new Category(); cat3.setName("Váy"); categoryRepository.save(cat3);
            Category cat4 = new Category(); cat4.setName("Phụ kiện"); categoryRepository.save(cat4);
            Category cat5 = new Category(); cat5.setName("Giày dép"); categoryRepository.save(cat5);

            // Tạo sản phẩm mẫu
            createProduct("Áo thun nam basic", 199000, 149000, "Áo thun nam chất liệu cotton 100%, form regular fit thoải mái", "Nike", cat1, 50, true);
            createProduct("Áo polo nam classic", 349000, 0, "Áo polo nam cổ bẻ sang trọng, chất vải pique cao cấp", "Adidas", cat1, 30, true);
            createProduct("Áo sơ mi nữ công sở", 450000, 380000, "Áo sơ mi nữ dài tay, chất liệu lụa mềm mại", "Uniqlo", cat1, 25, false);
            createProduct("Áo khoác jean unisex", 650000, 520000, "Áo khoác jean phong cách vintage, có thể mặc 4 mùa", "Levi's", cat1, 15, true);
            createProduct("Áo hoodie oversize", 380000, 0, "Áo hoodie oversize form rộng, nỉ bông ấm áp", "Nike", cat1, 40, false);

            createProduct("Quần jean nam slim fit", 550000, 450000, "Quần jean nam dáng slim fit co giãn tốt, wash đậm", "Levi's", cat2, 35, true);
            createProduct("Quần kaki nam", 420000, 0, "Quần kaki nam dáng suông, vải kaki dày dặn", "Adidas", cat2, 20, false);
            createProduct("Quần short nữ", 250000, 199000, "Quần short nữ lưng cao, chất liệu cotton mềm", "Zara", cat2, 45, false);
            createProduct("Quần jogger unisex", 320000, 0, "Quần jogger thể thao phong cách, bo chun cổ chân", "Nike", cat2, 3, true);

            createProduct("Váy công chúa ren", 890000, 690000, "Váy dạ hội phối ren tinh xảo, thiết kế sang trọng", "Zara", cat3, 10, true);
            createProduct("Váy midi xếp ly", 450000, 0, "Váy midi xếp ly thanh lịch, phù hợp đi làm và dạo phố", "Uniqlo", cat3, 2, false);
            createProduct("Đầm maxi boho", 520000, 420000, "Đầm maxi hoa phong cách boho, bay bổng nữ tính", "Zara", cat3, 18, false);

            createProduct("Túi xách nữ da PU", 350000, 280000, "Túi xách nữ da PU cao cấp, nhiều ngăn tiện dụng", "Gucci", cat4, 22, true);
            createProduct("Mũ lưỡi trai", 150000, 0, "Mũ lưỡi trai unisex, vải canvas thoáng khí", "Nike", cat4, 60, false);
            createProduct("Kính mát thời trang", 280000, 220000, "Kính mát chống UV400, gọng kim loại", "RayBan", cat4, 4, false);

            createProduct("Giày thể thao nam", 850000, 680000, "Giày thể thao nam đế êm, thiết kế trẻ trung năng động", "Nike", cat5, 28, true);
            createProduct("Sandal nữ quai chéo", 290000, 0, "Sandal nữ quai chéo, đế bằng thoải mái di chuyển", "Adidas", cat5, 33, false);
            createProduct("Giày cao gót 7cm", 520000, 420000, "Giày cao gót mũi nhọn thanh lịch, gót vuông vững chãi", "Zara", cat5, 12, true);

            System.out.println("=== Đã tạo dữ liệu sản phẩm mẫu ===");
        }

        // Tạo mã giảm giá mẫu
        if (couponRepository.count() == 0) {
            Coupon coupon1 = new Coupon();
            coupon1.setCode("WELCOME10");
            coupon1.setDiscountType("PERCENT");
            coupon1.setDiscountValue(10);
            coupon1.setMinOrderAmount(200000);
            coupon1.setMaxUsage(100);
            coupon1.setActive(true);
            coupon1.setStartDate(LocalDateTime.now());
            coupon1.setEndDate(LocalDateTime.now().plusMonths(3));
            couponRepository.save(coupon1);

            Coupon coupon2 = new Coupon();
            coupon2.setCode("SALE50K");
            coupon2.setDiscountType("FIXED");
            coupon2.setDiscountValue(50000);
            coupon2.setMinOrderAmount(500000);
            coupon2.setMaxUsage(50);
            coupon2.setActive(true);
            coupon2.setStartDate(LocalDateTime.now());
            coupon2.setEndDate(LocalDateTime.now().plusMonths(1));
            couponRepository.save(coupon2);

            Coupon coupon3 = new Coupon();
            coupon3.setCode("HUTECH20");
            coupon3.setDiscountType("PERCENT");
            coupon3.setDiscountValue(20);
            coupon3.setMinOrderAmount(300000);
            coupon3.setMaxUsage(200);
            coupon3.setActive(true);
            coupon3.setStartDate(LocalDateTime.now());
            coupon3.setEndDate(LocalDateTime.now().plusMonths(6));
            couponRepository.save(coupon3);

            System.out.println("=== Đã tạo mã giảm giá mẫu: WELCOME10, SALE50K, HUTECH20 ===");
        }
    }

    private void createProduct(String name, double price, double salePrice,
                                String description, String brand, Category category,
                                int stock, boolean featured) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setSalePrice(salePrice);
        product.setDescription(description);
        product.setBrand(brand);
        product.setCategory(category);
        product.setStockQuantity(stock);
        product.setFeatured(featured);
        product.setActive(true);
        product.setImageUrl("https://via.placeholder.com/400x400?text=" + name.replace(" ", "+"));
        productRepository.save(product);
    }
}
