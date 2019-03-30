package br.com.sicredi.pautavotacao.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "votos")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Voto extends PanacheEntity {

    @Column(name="opcao")
    private OpcaoVoto opcaoVoto;

    @Column(name="data")
    private LocalDateTime dataHora;

    @Column(name="id_eleitor")
    private Long idEleitor;

    @ManyToOne
    @JoinColumn(name = "id_sessao_votacao")
    private SessaoVotacao sessaoVotacao;
}
