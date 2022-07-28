package it.govpay.gde.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter	
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
public class EventoIndexEntity extends EventoBaseEntity { 
	
}
