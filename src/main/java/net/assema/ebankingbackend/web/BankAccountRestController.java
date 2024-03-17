package net.assema.ebankingbackend.web;

import lombok.AllArgsConstructor;
import net.assema.ebankingbackend.dtos.*;
import net.assema.ebankingbackend.entities.BankAccount;
import net.assema.ebankingbackend.exceptions.BalanceNotSufficientException;
import net.assema.ebankingbackend.exceptions.BankAccountNotFoundException;
import net.assema.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")


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

    @PostMapping("/account/debit")
    public DebitDTO debit(@RequestBody  DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
          this.bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount() , debitDTO.getDescription());
          return  debitDTO ;

    }
    @PostMapping("/account/credit")
    public CreditDTO credit(@RequestBody CreditDTO  creditDTO) throws BankAccountNotFoundException {
        this.bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount() , creditDTO.getDescription());
        return creditDTO  ;

    }

    @PostMapping("/account/transfer")
    public void transfer(@RequestBody TransferRequestDTO  transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.transfer(transferRequestDTO.getAccountSource(), transferRequestDTO.getAccountDestination() , transferRequestDTO.getAmount());

    }


}
