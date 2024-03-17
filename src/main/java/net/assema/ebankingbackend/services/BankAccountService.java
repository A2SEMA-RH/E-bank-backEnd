package net.assema.ebankingbackend.services;

import net.assema.ebankingbackend.dtos.*;
import net.assema.ebankingbackend.entities.BankAccount;
import net.assema.ebankingbackend.entities.CurrentAccount;
import net.assema.ebankingbackend.entities.Customer;
import net.assema.ebankingbackend.entities.SavingAccount;
import net.assema.ebankingbackend.exceptions.BalanceNotSufficientException;
import net.assema.ebankingbackend.exceptions.BankAccountNotFoundException;
import net.assema.ebankingbackend.exceptions.CustomerNoFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance , double overDraft, Long customerId) throws CustomerNoFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance , double interestRate, Long customerId) throws CustomerNoFoundException;

    List<CustomerDTO>listCustomer();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;

    void debit(String accountId, double amount ,String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId,double amount ,String description) throws BankAccountNotFoundException;

    void transfer(String accountIdSource, String accountIdDestination,double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccountDTO>bankAccountListe();


    CustomerDTO getCustomer(Long customerId) throws CustomerNoFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);

    List<CustomerDTO>rechercheCustomer(String keyword);
}
