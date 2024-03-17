package net.assema.ebankingbackend.dtos;

import lombok.Data;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
@Data

public class CreditDTO {
    private String accountId ;
    private  double amount;

    private  String description ;

}
