package net.assema.ebankingbackend.web;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.assema.ebankingbackend.dtos.CustomerDTO;
import net.assema.ebankingbackend.entities.Customer;
import net.assema.ebankingbackend.exceptions.CustomerNoFoundException;
import net.assema.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j

public class CustomerRestController {
    private BankAccountService  bankAccountService ;

    @GetMapping("/customers")
    public List<CustomerDTO>customers(){
        return  bankAccountService.listCustomer();

    }

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer( @PathVariable(name = "id") Long customerId) throws CustomerNoFoundException {
        return bankAccountService.getCustomer(customerId);
    }

    @PostMapping("/customers")
    public CustomerDTO savaCustomer(@RequestBody  CustomerDTO customerDTO){
        return  bankAccountService.saveCustomer(customerDTO);

    }

    @PutMapping("/accounts/{customerId}")
    public CustomerDTO update(@PathVariable Long customerId ,@RequestBody CustomerDTO customerDTO){
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);

    }

    @DeleteMapping("/accounts/{id}")
    public  void  deleteCustomer(@PathVariable  Long id){
        bankAccountService.deleteCustomer(id);
    }

}
