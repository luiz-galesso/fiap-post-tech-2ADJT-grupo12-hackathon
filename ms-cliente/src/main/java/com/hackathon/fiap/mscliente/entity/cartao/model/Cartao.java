package com.hackathon.fiap.mscliente.entity.cartao.model;

import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tb_cartao")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Cartao {

    @Id
    private String numeroCartao;

    private Double limite;

    private String dataValidade;

    private Integer cvv;

    @ManyToOne
    private Cliente cliente;

}
