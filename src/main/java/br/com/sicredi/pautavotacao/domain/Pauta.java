package br.com.sicredi.pautavotacao.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "pautas")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Pauta extends PanacheEntity {

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "titulo")
    private String titulo;

    @OneToOne(mappedBy = "pauta")
    private SessaoVotacao sessaoVotacao;
}
