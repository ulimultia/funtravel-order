package id.co.bca.funtravel.order.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.co.bca.funtravel.order.model.dto.OrderDTO;
import id.co.bca.funtravel.order.model.dto.ProductDTO;
import id.co.bca.funtravel.order.model.dto.StatusMessageDTO;
import id.co.bca.funtravel.order.service.IOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {
    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private RestTemplate restTemplate;

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
                try {
                    //get product
                    String uri = "http://localhost:8080/api/v1/product/id/" + orderDTO.getIdProduct();
                    System.out.println("uri: \n" +uri);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                    HttpEntity <StatusMessageDTO> productResp = new HttpEntity<StatusMessageDTO>(headers);
                    StatusMessageDTO<ProductDTO> tempDTO = restTemplate.exchange(uri, HttpMethod.GET, productResp, StatusMessageDTO.class).getBody();
                    ProductDTO productDTO = new ProductDTO();
                    BeanUtils.copyProperties(tempDTO.getData(), productDTO);
                    System.out.println(productDTO);

                    if (!productResp.hasBody()){
                        response.setStatus(HttpStatus.BAD_REQUEST.value());
                        response.setMessage("Data tidak valid!");
                        response.setData(orderDTO);
                        return ResponseEntity.badRequest().body(response);
                    }
                    else {
                        response.setStatus(HttpStatus.OK.value());
                        response.setMessage("Data ditemukan!");
                        response.setData(orderDTO);
                        return ResponseEntity.ok().body(response);
                    }
                } catch (Exception e){
                    return ResponseEntity.badRequest().body(e);
                }
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

            //get product
            RestTemplate restTemplate = new RestTemplate();
            String uri = "http://localhost:8080/api/v1/product/id/" + orderDTO.getIdProduct();

            StatusMessageDTO<ProductDTO> productResp = restTemplate.getForObject(uri, StatusMessageDTO.class);
            ProductDTO productDTO = productResp.getData();

            //set product to OrderDTO
            orderDTO.setProductName(productDTO.getProductName());
            orderDTO.setPrice(productDTO.getPrice());
            orderDTO.setDiscount(productDTO.getDiscount());

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
