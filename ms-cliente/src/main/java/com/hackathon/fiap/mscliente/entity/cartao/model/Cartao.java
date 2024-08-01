package com.hackathon.fiap.mscliente.entity.cartao.model;

import com.hackathon.fiap.mscliente.entity.cliente.model.Cliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "tb_cartao")
@Builder
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
