package net.assema.ebankingbackend.dtos;

import jakarta.persistence.*;
import lombok.Data;
import net.assema.ebankingbackend.enums.AccountStatus;

import java.util.Date;
import java.util.List;

@Data
public  class SavingBankAccountDTO extends  BankAccountDTO {
    private String id ;
    private double balance ;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double  interestRate;

}
