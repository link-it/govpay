/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.orm;

import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.govpay.orm package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
*/

@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.govpay.orm
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link IdEr }
     */
    public IdEr createIdEr() {
        return new IdEr();
    }

    /**
     * Create an instance of {@link IdVersamento }
     */
    public IdVersamento createIdVersamento() {
        return new IdVersamento();
    }

    /**
     * Create an instance of {@link Intermediario }
     */
    public Intermediario createIntermediario() {
        return new Intermediario();
    }

    /**
     * Create an instance of {@link Audit }
     */
    public Audit createAudit() {
        return new Audit();
    }

    /**
     * Create an instance of {@link IdTabellaControparti }
     */
    public IdTabellaControparti createIdTabellaControparti() {
        return new IdTabellaControparti();
    }

    /**
     * Create an instance of {@link Evento }
     */
    public Evento createEvento() {
        return new Evento();
    }

    /**
     * Create an instance of {@link TipoVersamento }
     */
    public TipoVersamento createTipoVersamento() {
        return new TipoVersamento();
    }

    /**
     * Create an instance of {@link Pagamento }
     */
    public Pagamento createPagamento() {
        return new Pagamento();
    }

    /**
     * Create an instance of {@link TipoVersamentoDominio }
     */
    public TipoVersamentoDominio createTipoVersamentoDominio() {
        return new TipoVersamentoDominio();
    }

    /**
     * Create an instance of {@link IdMessaggio }
     */
    public IdMessaggio createIdMessaggio() {
        return new IdMessaggio();
    }

    /**
     * Create an instance of {@link IdMail }
     */
    public IdMail createIdMail() {
        return new IdMail();
    }

    /**
     * Create an instance of {@link IdTributo }
     */
    public IdTributo createIdTributo() {
        return new IdTributo();
    }

    /**
     * Create an instance of {@link IdIbanAccredito }
     */
    public IdIbanAccredito createIdIbanAccredito() {
        return new IdIbanAccredito();
    }

    /**
     * Create an instance of {@link IdSingolaRevoca }
     */
    public IdSingolaRevoca createIdSingolaRevoca() {
        return new IdSingolaRevoca();
    }

    /**
     * Create an instance of {@link Batch }
     */
    public Batch createBatch() {
        return new Batch();
    }

    /**
     * Create an instance of {@link Notifica }
     */
    public Notifica createNotifica() {
        return new Notifica();
    }

    /**
     * Create an instance of {@link IdRilevamento }
     */
    public IdRilevamento createIdRilevamento() {
        return new IdRilevamento();
    }

    /**
     * Create an instance of {@link PagamentoPortaleVersamento }
     */
    public PagamentoPortaleVersamento createPagamentoPortaleVersamento() {
        return new PagamentoPortaleVersamento();
    }

    /**
     * Create an instance of {@link IdTracciato }
     */
    public IdTracciato createIdTracciato() {
        return new IdTracciato();
    }

    /**
     * Create an instance of {@link Uo }
     */
    public Uo createUo() {
        return new Uo();
    }

    /**
     * Create an instance of {@link PagamentoPortale }
     */
    public PagamentoPortale createPagamentoPortale() {
        return new PagamentoPortale();
    }

    /**
     * Create an instance of {@link Stampa }
     */
    public Stampa createStampa() {
        return new Stampa();
    }

    /**
     * Create an instance of {@link IdConfigurazione }
     */
    public IdConfigurazione createIdConfigurazione() {
        return new IdConfigurazione();
    }

    /**
     * Create an instance of {@link IdIuv }
     */
    public IdIuv createIdIuv() {
        return new IdIuv();
    }

    /**
     * Create an instance of {@link IdTipoVersamento }
     */
    public IdTipoVersamento createIdTipoVersamento() {
        return new IdTipoVersamento();
    }

    /**
     * Create an instance of {@link Tracciato }
     */
    public Tracciato createTracciato() {
        return new Tracciato();
    }

    /**
     * Create an instance of {@link IdPagamento }
     */
    public IdPagamento createIdPagamento() {
        return new IdPagamento();
    }

    /**
     * Create an instance of {@link IdBatch }
     */
    public IdBatch createIdBatch() {
        return new IdBatch();
    }

    /**
     * Create an instance of {@link RendicontazionePagamento }
     */
    public RendicontazionePagamento createRendicontazionePagamento() {
        return new RendicontazionePagamento();
    }

    /**
     * Create an instance of {@link IUV }
     */
    public IUV createIUV() {
        return new IUV();
    }

    /**
     * Create an instance of {@link TipoTributo }
     */
    public TipoTributo createTipoTributo() {
        return new TipoTributo();
    }

    /**
     * Create an instance of {@link IdRendicontazione }
     */
    public IdRendicontazione createIdRendicontazione() {
        return new IdRendicontazione();
    }

    /**
     * Create an instance of {@link Applicazione }
     */
    public Applicazione createApplicazione() {
        return new Applicazione();
    }

    /**
     * Create an instance of {@link Operatore }
     */
    public Operatore createOperatore() {
        return new Operatore();
    }

    /**
     * Create an instance of {@link IdAnagrafica }
     */
    public IdAnagrafica createIdAnagrafica() {
        return new IdAnagrafica();
    }

    /**
     * Create an instance of {@link IdRpt }
     */
    public IdRpt createIdRpt() {
        return new IdRpt();
    }

    /**
     * Create an instance of {@link IdUo }
     */
    public IdUo createIdUo() {
        return new IdUo();
    }

    /**
     * Create an instance of {@link Connettore }
     */
    public Connettore createConnettore() {
        return new Connettore();
    }

    /**
     * Create an instance of {@link IdEvento }
     */
    public IdEvento createIdEvento() {
        return new IdEvento();
    }

    /**
     * Create an instance of {@link ACL }
     */
    public ACL createACL() {
        return new ACL();
    }

    /**
     * Create an instance of {@link Tributo }
     */
    public Tributo createTributo() {
        return new Tributo();
    }

    /**
     * Create an instance of {@link RPT }
     */
    public RPT createRPT() {
        return new RPT();
    }

    /**
     * Create an instance of {@link EsitoAvvisatura }
     */
    public EsitoAvvisatura createEsitoAvvisatura() {
        return new EsitoAvvisatura();
    }

    /**
     * Create an instance of {@link Configurazione }
     */
    public Configurazione createConfigurazione() {
        return new Configurazione();
    }

    /**
     * Create an instance of {@link Rendicontazione }
     */
    public Rendicontazione createRendicontazione() {
        return new Rendicontazione();
    }

    /**
     * Create an instance of {@link IdFr }
     */
    public IdFr createIdFr() {
        return new IdFr();
    }

    /**
     * Create an instance of {@link IdContoAccredito }
     */
    public IdContoAccredito createIdContoAccredito() {
        return new IdContoAccredito();
    }

    /**
     * Create an instance of {@link IdApplicazione }
     */
    public IdApplicazione createIdApplicazione() {
        return new IdApplicazione();
    }

    /**
     * Create an instance of {@link IdUtenza }
     */
    public IdUtenza createIdUtenza() {
        return new IdUtenza();
    }

    /**
     * Create an instance of {@link UtenzaTipoVersamento }
     */
    public UtenzaTipoVersamento createUtenzaTipoVersamento() {
        return new UtenzaTipoVersamento();
    }

    /**
     * Create an instance of {@link IdPromemoria }
     */
    public IdPromemoria createIdPromemoria() {
        return new IdPromemoria();
    }

    /**
     * Create an instance of {@link VistaPagamentoPortale }
     */
    public VistaPagamentoPortale createVistaPagamentoPortale() {
        return new VistaPagamentoPortale();
    }

    /**
     * Create an instance of {@link IdIncasso }
     */
    public IdIncasso createIdIncasso() {
        return new IdIncasso();
    }

    /**
     * Create an instance of {@link IdConnettore }
     */
    public IdConnettore createIdConnettore() {
        return new IdConnettore();
    }

    /**
     * Create an instance of {@link IdMediaRilevamento }
     */
    public IdMediaRilevamento createIdMediaRilevamento() {
        return new IdMediaRilevamento();
    }

    /**
     * Create an instance of {@link FR }
     */
    public FR createFR() {
        return new FR();
    }

    /**
     * Create an instance of {@link IdNotifica }
     */
    public IdNotifica createIdNotifica() {
        return new IdNotifica();
    }

    /**
     * Create an instance of {@link IdVistaRiscossione }
     */
    public IdVistaRiscossione createIdVistaRiscossione() {
        return new IdVistaRiscossione();
    }

    /**
     * Create an instance of {@link IdCarrello }
     */
    public IdCarrello createIdCarrello() {
        return new IdCarrello();
    }

    /**
     * Create an instance of {@link IdStampa }
     */
    public IdStampa createIdStampa() {
        return new IdStampa();
    }

    /**
     * Create an instance of {@link IuvSearch }
     */
    public IuvSearch createIuvSearch() {
        return new IuvSearch();
    }

    /**
     * Create an instance of {@link Dominio }
     */
    public Dominio createDominio() {
        return new Dominio();
    }

    /**
     * Create an instance of {@link IdSingoloVersamento }
     */
    public IdSingoloVersamento createIdSingoloVersamento() {
        return new IdSingoloVersamento();
    }

    /**
     * Create an instance of {@link IdAcl }
     */
    public IdAcl createIdAcl() {
        return new IdAcl();
    }

    /**
     * Create an instance of {@link RR }
     */
    public RR createRR() {
        return new RR();
    }

    /**
     * Create an instance of {@link IdTipoVersamentoDominio }
     */
    public IdTipoVersamentoDominio createIdTipoVersamentoDominio() {
        return new IdTipoVersamentoDominio();
    }

    /**
     * Create an instance of {@link IdRr }
     */
    public IdRr createIdRr() {
        return new IdRr();
    }

    /**
     * Create an instance of {@link Operazione }
     */
    public Operazione createOperazione() {
        return new Operazione();
    }

    /**
     * Create an instance of {@link UtenzaDominio }
     */
    public UtenzaDominio createUtenzaDominio() {
        return new UtenzaDominio();
    }

    /**
     * Create an instance of {@link IdTipoTributo }
     */
    public IdTipoTributo createIdTipoTributo() {
        return new IdTipoTributo();
    }

    /**
     * Create an instance of {@link IdStazione }
     */
    public IdStazione createIdStazione() {
        return new IdStazione();
    }

    /**
     * Create an instance of {@link IbanAccredito }
     */
    public IbanAccredito createIbanAccredito() {
        return new IbanAccredito();
    }

    /**
     * Create an instance of {@link IdIntermediario }
     */
    public IdIntermediario createIdIntermediario() {
        return new IdIntermediario();
    }

    /**
     * Create an instance of {@link Stazione }
     */
    public Stazione createStazione() {
        return new Stazione();
    }

    /**
     * Create an instance of {@link IdOperatore }
     */
    public IdOperatore createIdOperatore() {
        return new IdOperatore();
    }

    /**
     * Create an instance of {@link VistaRiscossioni }
     */
    public VistaRiscossioni createVistaRiscossioni() {
        return new VistaRiscossioni();
    }

    /**
     * Create an instance of {@link IdMailTemplate }
     */
    public IdMailTemplate createIdMailTemplate() {
        return new IdMailTemplate();
    }

    /**
     * Create an instance of {@link Incasso }
     */
    public Incasso createIncasso() {
        return new Incasso();
    }

    /**
     * Create an instance of {@link SingoloVersamento }
     */
    public SingoloVersamento createSingoloVersamento() {
        return new SingoloVersamento();
    }

    /**
     * Create an instance of {@link Utenza }
     */
    public Utenza createUtenza() {
        return new Utenza();
    }

    /**
     * Create an instance of {@link Promemoria }
     */
    public Promemoria createPromemoria() {
        return new Promemoria();
    }

    /**
     * Create an instance of {@link IdPagamentoPortale }
     */
    public IdPagamentoPortale createIdPagamentoPortale() {
        return new IdPagamentoPortale();
    }

    /**
     * Create an instance of {@link IdEsitoAvvisatura }
     */
    public IdEsitoAvvisatura createIdEsitoAvvisatura() {
        return new IdEsitoAvvisatura();
    }

    /**
     * Create an instance of {@link Versamento }
     */
    public Versamento createVersamento() {
        return new Versamento();
    }

    /**
     * Create an instance of {@link IdSingolaRendicontazione }
     */
    public IdSingolaRendicontazione createIdSingolaRendicontazione() {
        return new IdSingolaRendicontazione();
    }

    /**
     * Create an instance of {@link VersamentoIncasso }
     */
    public VersamentoIncasso createVersamentoIncasso() {
        return new VersamentoIncasso();
    }

    /**
     * Create an instance of {@link IdDominio }
     */
    public IdDominio createIdDominio() {
        return new IdDominio();
    }

    /**
     * Create an instance of {@link IdSla }
     */
    public IdSla createIdSla() {
        return new IdSla();
    }

    private final static QName _VistaEventiVersamento = new QName("http://www.govpay.it/orm", "VistaEventiVersamento");

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Evento }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.govpay.it/orm", name="VistaEventiVersamento")
    public JAXBElement<Evento> createVistaEventiVersamento() {
        return new JAXBElement<Evento>(_VistaEventiVersamento, Evento.class, null, this.createEvento());
    }
    public JAXBElement<Evento> createVistaEventiVersamento(Evento vistaEventiVersamento) {
        return new JAXBElement<Evento>(_VistaEventiVersamento, Evento.class, null, vistaEventiVersamento);
    }

    private final static QName _VistaEventiPagamento = new QName("http://www.govpay.it/orm", "VistaEventiPagamento");

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Evento }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.govpay.it/orm", name="VistaEventiPagamento")
    public JAXBElement<Evento> createVistaEventiPagamento() {
        return new JAXBElement<Evento>(_VistaEventiPagamento, Evento.class, null, this.createEvento());
    }
    public JAXBElement<Evento> createVistaEventiPagamento(Evento vistaEventiPagamento) {
        return new JAXBElement<Evento>(_VistaEventiPagamento, Evento.class, null, vistaEventiPagamento);
    }

    private final static QName _VistaEventiRpt = new QName("http://www.govpay.it/orm", "VistaEventiRpt");

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Evento }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.govpay.it/orm", name="VistaEventiRpt")
    public JAXBElement<Evento> createVistaEventiRpt() {
        return new JAXBElement<Evento>(_VistaEventiRpt, Evento.class, null, this.createEvento());
    }
    public JAXBElement<Evento> createVistaEventiRpt(Evento vistaEventiRpt) {
        return new JAXBElement<Evento>(_VistaEventiRpt, Evento.class, null, vistaEventiRpt);
    }


 }
