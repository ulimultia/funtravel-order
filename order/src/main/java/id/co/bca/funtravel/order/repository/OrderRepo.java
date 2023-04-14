package id.co.bca.funtravel.order.repository;

import id.co.bca.funtravel.order.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Integer> {
    public List<Order> findByStatus(Integer status);
}
