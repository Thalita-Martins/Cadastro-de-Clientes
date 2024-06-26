package com.thalitamartins.cadastroclientes.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "contato")
public class Contato {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String SEQ_GENERATOR = "contato_id_seq_gen";
    private static final String SEQ_NAME = "contato_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_GENERATOR)
    @SequenceGenerator(name = SEQ_GENERATOR, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;

    private String nome;

    private String telefone;

    private String email;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("contato")
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    private Boolean ativo;

}
