package br.com.sicredi.pautavotacao.domain;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "pautas")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of={"id"})
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao")
    private String descricao;

}
