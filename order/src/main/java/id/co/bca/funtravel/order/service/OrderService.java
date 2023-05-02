package id.co.bca.funtravel.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.co.bca.funtravel.order.model.dto.OrderDTO;
import id.co.bca.funtravel.order.model.dto.ProductDTO;
import id.co.bca.funtravel.order.model.dto.StatusMessageDTO;
import id.co.bca.funtravel.order.model.entity.Order;
import id.co.bca.funtravel.order.repository.OrderRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class OrderService implements IOrderService{

    @Autowired
    private OrderRepo orderRepo;

    @Override
    public OrderDTO create(OrderDTO orderDTO) {
        ProductDTO productDTO = this.getProductDTO(orderDTO.getIdProduct());
        Order order = new Order();
        order.setIdCustomer(orderDTO.getIdCustomer());
        order.setIdProduct(productDTO.getIdProduct());
        order.setDiscount(productDTO.getDiscount());
        order.setPrice(productDTO.getPrice());
        order.setTotal(calcTotal(productDTO.getDiscount(), productDTO.getPrice()));
        order.setStatus(0);
        order.setCreatedDate(Timestamp.from(Instant.now()));
        order.setUpdatedDate(Timestamp.from(Instant.now()));
        orderRepo.save(order);

        return new OrderDTO(order.getIdOrder(), order.getIdCustomer(), order.getIdProduct(), productDTO.getProductName(), order.getPrice(), order.getDiscount(), order.getTotal(), order.getStatus(), order.getCreatedDate(), order.getUpdatedDate());
    }

    @Override
    public OrderDTO update(OrderDTO orderDTO) {
        Order order = orderRepo.findById(orderDTO.getIdOrder()).get();

        if (order != null ){
            ProductDTO productDTO = this.getProductDTO(orderDTO.getIdProduct());
            order.setUpdatedDate(Timestamp.from(Instant.now()));
            order.setIdProduct(productDTO.getIdProduct());
            order.setDiscount(productDTO.getDiscount());
            order.setPrice(productDTO.getPrice());
            order.setTotal(calcTotal(productDTO.getDiscount(), productDTO.getPrice()));
            orderRepo.save(order);
            return new OrderDTO(order.getIdOrder(), order.getIdCustomer(), order.getIdProduct(), productDTO.getProductName(), order.getPrice(), order.getDiscount(), order.getTotal(), order.getStatus(), order.getCreatedDate(), order.getUpdatedDate());
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
            ProductDTO productDTO = this.getProductDTO(order.getIdProduct());
            orderDTOList.add(new OrderDTO(order.getIdOrder(), order.getIdCustomer(), order.getIdProduct(), productDTO.getProductName(), order.getPrice(), order.getDiscount(), order.getTotal(), order.getStatus(), order.getCreatedDate(), order.getUpdatedDate()));
        }
        return orderDTOList;
    }

    @Override
    public OrderDTO getById(Integer id) {
        Order order = orderRepo.findById(id).get();
        ProductDTO productDTO = this.getProductDTO(order.getIdProduct());

        if(order == null){
            return null;
        }
        else {
            return new OrderDTO(order.getIdOrder(), order.getIdCustomer(), order.getIdProduct(), productDTO.getProductName(), order.getPrice(), order.getDiscount(), order.getTotal(), order.getStatus(), order.getCreatedDate(), order.getUpdatedDate());
        }

    }

    @Override
    public OrderDTO delete(Integer id) {
        Order order = orderRepo.findById(id).get();

        if (order != null){
            ProductDTO productDTO = this.getProductDTO(order.getIdProduct());
            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(order, orderDTO);
            orderDTO.setProductName(productDTO.getProductName());
            orderRepo.delete(order);
            return orderDTO;
        }
        else {
            return null;
        }
    }
    
    
    private Double calcTotal (Double discount, Double price){
        //function untuk menghitung total harga setelah diskon
        return ((100 - discount) / 100) * price;
    }

    private ProductDTO getProductDTO(Integer id){
        //get product data
        RestTemplate restTemplate = new RestTemplate();
        String uri = "http://localhost:8080/api/v1/product/id/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<StatusMessageDTO> productResp = new HttpEntity<StatusMessageDTO>(headers);
        String body = restTemplate.exchange(uri, HttpMethod.GET, productResp, String.class).getBody();

        //change response to object
        ObjectMapper mapper = new ObjectMapper();
        ProductDTO productDTO = new ProductDTO();
        try {
            StatusMessageDTO<ProductDTO> statusMessageDTO = mapper.readValue(body, new TypeReference<StatusMessageDTO<ProductDTO>>() {});
            BeanUtils.copyProperties(statusMessageDTO.getData(), productDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if(productDTO == null){
            return null;
        }
        else{
            return productDTO;
        }
    }
}
