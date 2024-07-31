package com.hackathon.fiap.mscliente.entity.cartao.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tb_cartao")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cartao {

  @Id
  private String numeroCartao;

}
