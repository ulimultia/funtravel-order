package id.co.bca.funtravel.order.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Integer idOrder;
    private Integer idCustomer;
    private Integer idProduct;
    private String productName;
    private Double price;
    private Double discount;
    private Double total;
    private Integer status;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}
