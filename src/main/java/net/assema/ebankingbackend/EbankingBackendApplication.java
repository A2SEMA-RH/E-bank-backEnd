package net.assema.ebankingbackend;

import net.assema.ebankingbackend.dtos.BankAccountDTO;
import net.assema.ebankingbackend.dtos.CurrentBankAccountDTO;
import net.assema.ebankingbackend.dtos.CustomerDTO;
import net.assema.ebankingbackend.dtos.SavingBankAccountDTO;
import net.assema.ebankingbackend.entities.*;
import net.assema.ebankingbackend.enums.AccountStatus;
import net.assema.ebankingbackend.enums.OperationType;
import net.assema.ebankingbackend.exceptions.BalanceNotSufficientException;
import net.assema.ebankingbackend.exceptions.BankAccountNotFoundException;
import net.assema.ebankingbackend.exceptions.CustomerNoFoundException;
import net.assema.ebankingbackend.repositories.AccountOperationRepository;
import net.assema.ebankingbackend.repositories.BankAccountRepository;
import net.assema.ebankingbackend.repositories.CustomerRepository;
import net.assema.ebankingbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            Stream.of("Hassane", "imane", "mohamed").forEach(name->{
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setName(name);
                customerDTO.setEmail(name+"@gmail.com");
                bankAccountService.saveCustomer(customerDTO);
            });
            bankAccountService.listCustomer().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*9000 , 9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*12000, 5.5 , customer.getId());


                } catch (CustomerNoFoundException e) {
                    e.printStackTrace();
                }
            });
            List<BankAccountDTO> bankAccountDTOS = bankAccountService.bankAccountListe();
            for (BankAccountDTO bankAccountDTO : bankAccountDTOS){
                for (int i =0 ; i <10 ; i++){
                    String accountId;
                    if (bankAccountDTO instanceof SavingBankAccountDTO){
                        accountId = ((SavingBankAccountDTO) bankAccountDTO).getId();
                    }else {
                        accountId = ((CurrentBankAccountDTO)bankAccountDTO).getId();

                    }
                    bankAccountService.credit(accountId , 12200, "credit");
                    bankAccountService.debit(accountId , 1000+Math.random()*9000,"debit");

                }


            }


        };
    }
    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository , BankAccountRepository bankAccountRepository, AccountOperationRepository accountOperationRepository){
        return args -> {
            Stream.of("Hassane", "Yassine", "Aicha").forEach(name->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
                    });

            customerRepository.findAll().forEach(customer -> {
                CurrentAccount currentAccount= new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount= new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);
            });

            bankAccountRepository.findAll().forEach(acc-> {
                for (int i = 0; i < 10; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random() * 12000);
                    accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }

                BankAccount bankAccount = bankAccountRepository.findById("5afcbd25-96b3-45df-acd1-8344d6267a5c").orElse(null);
                System.out.println("**************************************************************");
                if (bankAccount != null) {
                System.out.println(bankAccount.getId());
                System.out.println(bankAccount.getBalance());
                System.out.println(bankAccount.getStatus());
                System.out.println(bankAccount.getCreatedAt());
                System.out.println(bankAccount.getCustomer().getName());
                System.out.println(bankAccount.getClass().getSimpleName());
                if (bankAccount instanceof CurrentAccount) {
                    System.out.println("je suis un compte courant ");
                    System.out.println("Over draft=>" + ((CurrentAccount) bankAccount).getOverDraft());
                } else if (bankAccount instanceof SavingAccount) {
                    System.out.println("Rate=>" + ((SavingAccount) bankAccount).getInterestRate());

                }
                bankAccount.getAccountOperations().forEach(op -> {
                    System.out.println("========================================");
                    System.out.println(op.getType());
                    System.out.println(op.getOperationDate());
                    System.out.println(op.getAmount());

                });
            }








            });

        };

    }

}


