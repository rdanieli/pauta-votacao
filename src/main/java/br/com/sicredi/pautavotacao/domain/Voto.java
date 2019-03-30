package br.com.sicredi.pautavotacao.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "votos")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of={"id"})
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="opcao")
    private OpcaoVoto opcaoVoto;

    @Column(name="data")
    private LocalDateTime data;

    @Column(name="id_eleitor")
    private Long eleitorId;

    @ManyToOne
    @JoinColumn(name = "id_sessao_votacao")
    private SessaoVotacao sessaoVotacao;
}
