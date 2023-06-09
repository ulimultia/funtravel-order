package id.co.bca.funtravel.order.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_table")
public class Order {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer idOrder;

    @Column
    private Integer idCustomer;

    @Column
    private Integer idProduct;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Double discount;

    @Column
    private Double total;

    @Column
    private Integer status;

    @Basic
    private Timestamp createdDate;

    @Basic
    private Timestamp updatedDate;
}
