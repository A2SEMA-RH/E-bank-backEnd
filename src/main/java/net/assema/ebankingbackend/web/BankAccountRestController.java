package net.assema.ebankingbackend.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import net.assema.ebankingbackend.dtos.AccountHistoryDTO;
import net.assema.ebankingbackend.dtos.AccountOperationDTO;
import net.assema.ebankingbackend.dtos.BankAccountDTO;
import net.assema.ebankingbackend.entities.BankAccount;
import net.assema.ebankingbackend.exceptions.BankAccountNotFoundException;
import net.assema.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor


public class BankAccountRestController {
    private BankAccountService bankAccountService;


    @GetMapping("accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable  String accountId) throws BankAccountNotFoundException {
        return  bankAccountService.getBankAccount(accountId);

    }

    @GetMapping("/accounts")
    public List<BankAccountDTO>listAccount(){
        return bankAccountService.bankAccountListe();
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getHistory(@PathVariable  String accountId ,
                                        @RequestParam(name = "page", defaultValue = "0") int page ,
                                        @RequestParam(name = "size", defaultValue = "5")int size ) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId, page , size);
    }


}
