package br.com.sicredi.pautavotacao.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sessoes_votacao")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of={"id"})
public class SessaoVotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="data_abertura")
    private LocalDateTime dataAbertura;

    @Column(name="data_encerramento")
    private LocalDateTime dataFechamento;

    @ManyToOne
    @JoinColumn(name = "id_pauta")
    private Pauta pauta;
}
