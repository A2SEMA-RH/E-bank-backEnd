package net.assema.ebankingbackend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("SA")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SavingAccount extends BankAccount{
    private double interestRate ;
}
