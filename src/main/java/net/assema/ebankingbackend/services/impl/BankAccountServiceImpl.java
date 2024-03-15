package net.assema.ebankingbackend.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.assema.ebankingbackend.dtos.*;
import net.assema.ebankingbackend.entities.*;
import net.assema.ebankingbackend.enums.OperationType;
import net.assema.ebankingbackend.exceptions.BalanceNotSufficientException;
import net.assema.ebankingbackend.exceptions.BankAccountNotFoundException;
import net.assema.ebankingbackend.exceptions.CustomerNoFoundException;
import net.assema.ebankingbackend.mappers.BankAccountMappersImpl;
import net.assema.ebankingbackend.repositories.AccountOperationRepository;
import net.assema.ebankingbackend.repositories.BankAccountRepository;
import net.assema.ebankingbackend.repositories.CustomerRepository;
import net.assema.ebankingbackend.services.BankAccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j


public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMappersImpl dtoMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("saving new customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savaCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savaCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNoFoundException {
        Customer customer= customerRepository.findById(customerId).orElse(null);
        if (customer==null){
            throw new CustomerNoFoundException("Customer not found");
        }
        CurrentAccount currentAccount = new CurrentAccount() ;
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedBankAccount =bankAccountRepository.save(currentAccount);
        return dtoMapper.fromSCurrentBankAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNoFoundException {
        Customer customer= customerRepository.findById(customerId).orElse(null);
        if (customer==null){
            throw new CustomerNoFoundException("Customer not found");
        }
        SavingAccount savingAccount = new SavingAccount() ;
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccount savedBankAccount =bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingBankAccount(savedBankAccount);
    }

    @Override
    public List<CustomerDTO> listCustomer() {
        List<Customer> customers =customerRepository.findAll();
        List<CustomerDTO> customerDTOS  = customers.stream()
                .map(customer ->dtoMapper.fromCustomer(customer))
                .collect(Collectors.toList());



        /*
        c'est beaucoups plus volumineux en termes de code mais c'est ce qui  se passe derriere pour  comprendre
        List<Customer> customers =customerRepository.findAll();
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for (Customer customer : customers){
            CustomerDTO customerDTO = dtoMapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);
        }
        *
         */
        return  customerDTOS ;

    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount= bankAccountRepository.findById(accountId)
                .orElseThrow( ()->new BankAccountNotFoundException("Bank is not found") );

        if (bankAccount instanceof SavingAccount){
            SavingAccount savingAccount =(SavingAccount) bankAccount ;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        }else {
            CurrentAccount currentAccount =(CurrentAccount) bankAccount ;
            return dtoMapper.fromSCurrentBankAccount(currentAccount);
        }

    }


    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount= bankAccountRepository.findById(accountId)
                .orElseThrow( ()->new BankAccountNotFoundException("Bank is not found") );
        if(bankAccount.getBalance()<amount){
            throw new BalanceNotSufficientException(" Balance not sufficient");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount= bankAccountRepository.findById(accountId)
                .orElseThrow( ()->new BankAccountNotFoundException("Bank is not found") );
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource,amount ,"transfer to "+accountIdDestination);
        credit(accountIdDestination ,amount , "transfer from "+accountIdSource);

    }

    @Override
    public List<BankAccountDTO> bankAccountListe() {
                List<BankAccount> bankAccounts = bankAccountRepository.findAll();
                List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount-> {
                    if (bankAccount instanceof SavingAccount) {
                        SavingAccount savingAccount = (SavingAccount) bankAccount;
                        return  dtoMapper.fromSavingBankAccount(savingAccount);

                    } else {
                        CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                        return  dtoMapper.fromSCurrentBankAccount(currentAccount);
                    }
                }).collect(Collectors.toList());
          return  bankAccountDTOS ;
    }
    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNoFoundException {
        Customer customer =customerRepository.findById(customerId)
                .orElseThrow(()->new CustomerNoFoundException("Customer not found"));

        return dtoMapper.fromCustomer(customer);
    }


    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("saving new customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savaCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savaCustomer);
    }

    @Override
    public  void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }


    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations =accountOperationRepository.findByBankAccountId(accountId);
        List<AccountOperationDTO> accountOperationDTOS =accountOperations.stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        return accountOperationDTOS ;


    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount==null) throw new BankAccountNotFoundException("Account not Found");
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS= accountOperations.getContent().stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());


        return accountHistoryDTO;
    }

}
