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
    private Integer idProduct;
    private Float price;
    private Integer status;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}
