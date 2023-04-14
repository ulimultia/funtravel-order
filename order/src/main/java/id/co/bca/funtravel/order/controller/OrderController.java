package id.co.bca.funtravel.order.controller;

import id.co.bca.funtravel.order.model.dto.OrderDTO;
import id.co.bca.funtravel.order.model.dto.StatusMessageDTO;
import id.co.bca.funtravel.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {
    @Autowired
    private IOrderService iOrderService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrder(){
        try {
            StatusMessageDTO<List<OrderDTO>> response = new StatusMessageDTO<>();
            List<OrderDTO> orderDTOList = iOrderService.getAll();
            if (orderDTOList.size() == 0) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Data kosong!");
                response.setData(orderDTOList);
                return ResponseEntity.badRequest().body(response);
            }
            else {
                response.setStatus(HttpStatus.OK.value());
                response.setMessage("Berhasil!");
                response.setData(orderDTOList);
                return ResponseEntity.ok().body(response);
            }
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getByIdOrder(@PathVariable Integer id) {
        try {
            StatusMessageDTO<OrderDTO> response = new StatusMessageDTO<>();
            OrderDTO orderDTO = iOrderService.getById(id);
            if(orderDTO == null){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Data tidak ditemukan!");
                response.setData(orderDTO);
                return ResponseEntity.badRequest().body(response);
            }
            else {
                response.setStatus(HttpStatus.OK.value());
                response.setMessage("Data ditemukan!");
                response.setData(orderDTO);
                return ResponseEntity.ok().body(response);
            }
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@RequestBody OrderDTO orderDTO){
        try {
            StatusMessageDTO<OrderDTO> response = new StatusMessageDTO<>();
            OrderDTO newOrderDTO = iOrderService.create(orderDTO);
            response.setStatus(HttpStatus.CREATED.value());
            response.setMessage("Data berhasil diinputkan!");
            response.setData(newOrderDTO);
            return ResponseEntity.ok().body(response);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editOrder(@RequestBody OrderDTO orderDTO) {
        try {
            StatusMessageDTO<OrderDTO> response = new StatusMessageDTO<>();
            OrderDTO newOrderDTO = iOrderService.update(orderDTO);
            if(newOrderDTO == null){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Gagal update data!");
                response.setData(orderDTO);
                return ResponseEntity.badRequest().body(response);
            }
            else{
                response.setStatus(HttpStatus.OK.value());
                response.setMessage("Berhasil update data!");
                response.setData(newOrderDTO);
                return ResponseEntity.ok().body(response);
            }
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        try {
            StatusMessageDTO<OrderDTO> response = new StatusMessageDTO<>();
            OrderDTO orderDTO = iOrderService.delete(id);
            if (orderDTO == null){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Gagal hapus data!");
                response.setData(orderDTO);
                return ResponseEntity.badRequest().body(response);
            }
            else {
                response.setStatus(HttpStatus.OK.value());
                response.setMessage("Berhasil hapus data!");
                response.setData(orderDTO);
                return ResponseEntity.ok().body(response);
            }
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }
}
