package net.assema.ebankingbackend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String name ;
    private String email;
    @OneToMany(mappedBy = "customer")
//    pour ignorer le chargement des comptes du client
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<BankAccount> bankAccounts;
}
