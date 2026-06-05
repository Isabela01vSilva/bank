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
@Table(name = "cliente")
@Entity(name = "Cliente")
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long id;

    @Column(name = "nome_completo", nullable = false)
    private String fullName;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate birthDate;

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "telefone", nullable = false, unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_cliente", nullable = false)
    private CustomerStatus customerStatus;

    public void updateInfoCustomer(CustomerRequest data) {
        // Atualiza somente campos não-nulos da requisição
        if (data.fullName() != null) {
            this.fullName = data.fullName();
        }
        if (data.birthDate() != null) {
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