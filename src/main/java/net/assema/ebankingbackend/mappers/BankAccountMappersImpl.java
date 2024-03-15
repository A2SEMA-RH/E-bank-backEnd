package net.assema.ebankingbackend.mappers;

import net.assema.ebankingbackend.dtos.AccountOperationDTO;
import net.assema.ebankingbackend.dtos.CurrentBankAccountDTO;
import net.assema.ebankingbackend.dtos.CustomerDTO;
import net.assema.ebankingbackend.dtos.SavingBankAccountDTO;
import net.assema.ebankingbackend.entities.AccountOperation;
import net.assema.ebankingbackend.entities.CurrentAccount;
import net.assema.ebankingbackend.entities.Customer;
import net.assema.ebankingbackend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.swing.plaf.PanelUI;
import java.util.List;

@Service
public class BankAccountMappersImpl {
    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO  customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount  savingAccount){
        SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingAccount , savingBankAccountDTO);
        savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingBankAccountDTO ;

    }

    public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO  savingBankAccountDTO){
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDTO  , savingAccount);
        savingAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
        return savingAccount;

    }

    public CurrentBankAccountDTO fromSCurrentBankAccount(CurrentAccount  currentAccount){
        CurrentBankAccountDTO  currentBankAccountDTO = new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentAccount, currentBankAccountDTO);
        currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        currentBankAccountDTO.setType(currentAccount.getClass().getSimpleName());
        return currentBankAccountDTO;

    }

    public CurrentAccount fromCurrentSavingBankAccountDTO(CurrentBankAccountDTO  currentBankAccountDTO){
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO , currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
        return currentAccount;

    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation){
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation, accountOperationDTO);
        return accountOperationDTO;
    }

}
