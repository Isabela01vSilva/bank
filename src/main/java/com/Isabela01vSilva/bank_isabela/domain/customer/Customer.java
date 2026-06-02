package com.Isabela01vSilva.bank_isabela.domain.customer;

import com.Isabela01vSilva.bank_isabela.controller.request.customer.CustomerRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Table(name = "clientes")
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCliente")
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    public void updateInfoCustomer(CustomerRequest data) {
        if (data.fullName() != null) {
            this.fullName = data.fullName();
        }
        if(data.birthDate() != null) {
            this.birthDate = data.birthDate();
        }
        if (data.cpf() != null) {
            this.cpf = data.cpf();
        }
        if (data.email() != null) {
            this.email = data.email();
        }
        if (data.phoneNumber() != null) {
            this.phoneNumber = data.phoneNumber();
        }
    }
}