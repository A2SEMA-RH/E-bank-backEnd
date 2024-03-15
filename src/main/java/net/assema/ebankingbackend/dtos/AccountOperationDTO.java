package net.assema.ebankingbackend.dtos;

import lombok.Data;


import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id ;
    private Date operationDate ;
    private double amount ;
    private String description;
}
