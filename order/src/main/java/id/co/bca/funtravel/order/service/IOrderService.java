package id.co.bca.funtravel.order.service;

import id.co.bca.funtravel.order.model.dto.OrderDTO;
import java.util.List;

public interface IOrderService {
    public OrderDTO create(OrderDTO orderDTO);
    public OrderDTO update(OrderDTO orderDTO);
    public List<OrderDTO> getAll();
    public OrderDTO getById(Integer id);
    public OrderDTO delete(Integer id);
}
