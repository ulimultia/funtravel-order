package id.co.bca.funtravel.order.service;

import id.co.bca.funtravel.order.model.dto.OrderDTO;
import id.co.bca.funtravel.order.model.entity.Order;
import id.co.bca.funtravel.order.repository.OrderRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements IOrderService{

    @Autowired
    private OrderRepo orderRepo;

    @Override
    public OrderDTO create(OrderDTO orderDTO) {
        orderDTO.setCreatedDate(Timestamp.from(Instant.now()));
        orderDTO.setUpdatedDate(Timestamp.from(Instant.now()));
        //set total dari hasil perhitungan discount (%) dengan price
        orderDTO.setTotal(calcTotal(orderDTO.getDiscount(), orderDTO.getPrice()));
        
        Order order = new Order();

        BeanUtils.copyProperties(orderDTO, order);

        return new OrderDTO(order.getIdOrder(), order.getIdCustomer(), orderDTO.getIdProduct(), orderDTO.getProductName(), orderDTO.getPrice(), orderDTO.getDiscount(), order.getTotal(), order.getStatus(), order.getCreatedDate(), order.getUpdatedDate());
    }

    @Override
    public OrderDTO update(OrderDTO orderDTO) {
        Optional<Order> order = orderRepo.findById(orderDTO.getIdOrder());

        if (order != null ){
            orderDTO.setUpdatedDate(Timestamp.from(Instant.now()));
            BeanUtils.copyProperties(orderDTO, order);
            orderRepo.save(order.get());
            return new OrderDTO(order.get().getIdOrder(), order.get().getIdCustomer(), orderDTO.getIdProduct(), orderDTO.getProductName(), orderDTO.getPrice(), orderDTO.getDiscount(), order.get().getTotal(), order.get().getStatus(), order.get().getCreatedDate(), order.get().getUpdatedDate());
        }
        else {
            return null;
        }
    }

    @Override
    public List<OrderDTO> getAll() {
        List<Order> orders = orderRepo.findAll();
        List<OrderDTO> orderDTOList = new ArrayList<>();
        for (Order order : orders) {
            orderDTOList.add(new OrderDTO(order.getIdOrder(), order.getIdCustomer(), order.getIdProduct(), null, order.getPrice(), order.getDiscount(), order.getTotal(), order.getStatus(), order.getCreatedDate(), order.getUpdatedDate()));
        }
        return orderDTOList;
    }

    @Override
    public OrderDTO getById(Integer id) {
        Optional<Order> order = orderRepo.findById(id);
        if(order == null){
            return null;
        }
        else {
            return new OrderDTO(order.get().getIdOrder(), order.get().getIdCustomer(), order.get().getIdProduct(), "productName", order.get().getPrice(), order.get().getDiscount(), order.get().getTotal(), order.get().getStatus(), order.get().getCreatedDate(), order.get().getUpdatedDate());
        }

    }

    @Override
    public OrderDTO delete(Integer id) {
        Optional<Order> order = orderRepo.findById(id);

        if (order != null){
            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(order.get(), orderDTO);
            orderRepo.delete(order.get());
            return orderDTO;
        }
        else {
            return null;
        }
    }
    
    
    private Double calcTotal (Double discount, Double price){
        return (discount / 100) * price;
    }
}
