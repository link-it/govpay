package it.govpay.gde.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter	
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(
		name = "eventi", 
		indexes = {
				@Index(name="idx_evt_data", columnList = "data"),
				@Index(name="idx_evt_fk_vrs", columnList = "cod_applicazione,cod_versamento_ente"),
				@Index(name="idx_evt_id_sessione", columnList = "id_sessione"),
				@Index(name="idx_evt_iuv", columnList = "iuv"),
				@Index(name="idx_evt_fk_fr", columnList = "id_fr")
		}
)
public class EventoEntity extends EventoBaseEntity {
	
	@Lob
	@Type(type="org.hibernate.type.BinaryType")
	@Column(name = "parametri_richiesta")
	private byte[] parametriRichiesta;
	
	@Lob
	@Type(type="org.hibernate.type.BinaryType")
	@Column(name = "parametri_risposta")
	private byte[] parametriRisposta;
	
}
