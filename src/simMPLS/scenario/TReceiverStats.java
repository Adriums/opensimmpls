/* 
 * Copyright (C) Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package simMPLS.scenario;

import simMPLS.protocols.TAbstractPDU;
import org.jfree.chart.*;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.*;
import org.jfree.data.*;

/**
 * Esta lase implementa las estad�sticas para un nodo receptor.
 * @author <B>Manuel Dom�nguez Dorado</B><br><A
 * href="mailto:ingeniero@ManoloDominguez.com">ingeniero@ManoloDominguez.com</A><br><A href="http://www.ManoloDominguez.com" target="_blank">http://www.ManoloDominguez.com</A>
 * @version 1.0
 */
public class TReceiverStats extends TStats {
    
    /**
     * Este m�todo crea una nueva instancia de TEstadisticasReceptor.
     * @since 2.0
     */
    public TReceiverStats() {
    	paquetesEntrantes = new XYSeriesCollection();
    	entrantesIPv4 = new XYSeries(TStats.IPV4);
    	entrantesIPv4_GOS1 = new XYSeries(TStats.IPV4_GOS1);
    	entrantesIPv4_GOS2 = new XYSeries(TStats.IPV4_GOS2);
    	entrantesIPv4_GOS3 = new XYSeries(TStats.IPV4_GOS3);
    	entrantesMPLS = new XYSeries(TStats.MPLS);
    	entrantesMPLS_GOS1 = new XYSeries(TStats.MPLS_GOS1);
    	entrantesMPLS_GOS2 = new XYSeries(TStats.MPLS_GOS2);
    	entrantesMPLS_GOS3 = new XYSeries(TStats.MPLS_GOS3);
    	entrantesGPSRP = new XYSeries(TStats.GPSRP);
        tEIPV4 = 0;
        tEIPV4_GOS1 = 0;
        tEIPV4_GOS2 = 0;
        tEIPV4_GOS3 = 0;
        tEMPLS = 0;
        tEMPLS_GOS1 = 0;
        tEMPLS_GOS2 = 0;
        tEMPLS_GOS3 = 0;
        tEGPSRP = 0;
    }
    
    /**
     * Este m�todo permite obtener los datos necesarios para poder generar la gr�fica
     * 1.
     * @return Datos de la gr�fica 1.
     * @since 2.0
     */    
    public org.jfree.data.AbstractDataset getDataset1() {
        return this.paquetesEntrantes;
    }
    
    /**
     * Este m�todo permite obtener los datos necesarios para poder generar la gr�fica
     * 2.
     * @return Datos de la gr�fica 2.
     * @since 2.0
     */    
    public org.jfree.data.AbstractDataset getDataset2() {
        return null;
    }
    
    /**
     * Este m�todo permite obtener los datos necesarios para poder generar la gr�fica
     * 3.
     * @return Datos de la gr�fica 3.
     * @since 2.0
     */    
    public org.jfree.data.AbstractDataset getDataset3() {
        return null;
    }
    
    /**
     * Este m�todo permite obtener los datos necesarios para poder generar la gr�fica
     * 4.
     * @return Datos de la gr�fica 4.
     * @since 2.0
     */    
    public org.jfree.data.AbstractDataset getDataset4() {
        return null;
    }
    
    /**
     * Este m�todo permite obtener los datos necesarios para poder generar la gr�fica
     * 5.
     * @return Datos de la gr�fica 5.
     * @since 2.0
     */    
    public org.jfree.data.AbstractDataset getDataset5() {
        return null;
    }
    
    /**
     * Este m�todo permite obtener los datos necesarios para poder generar la gr�fica
     * 6.
     * @return Datos de la gr�fica 6.
     * @since 2.0
     */    
    public org.jfree.data.AbstractDataset getDataset6() {
        return null;
    }

    /**
     * Este m�todo permite ampliar las estad�sticas a�adiendo las correspondientes para
     * el paquete especificado.
     * @param paquete Paquete que se desea contabilizar.
     * @param entrada INCOMING, OUTGOING o DISCARD, dependiendo de si el paquete entra en el nodo, sale
 de �l o es descartado.
     * @since 2.0
     */    
    public void addStatEntry(TAbstractPDU paquete, int entrada) {
        if (this.statsEnabled) {
            int tipoPaquete = paquete.getSubtype();
            int GoS = 0;
            if (tipoPaquete == TAbstractPDU.GPSRP) {
                if (entrada == TStats.INCOMING) {
                    this.tEGPSRP++;
                }
            } else if (tipoPaquete == TAbstractPDU.MPLS) {
                if (entrada == TStats.INCOMING) {
                    this.tEMPLS++;
                }
            } else if (tipoPaquete == TAbstractPDU.MPLS_GOS) {
                GoS = paquete.getIPv4Header().getOptionsField().getRequestedGoSLevel();
                if (entrada == TStats.INCOMING) {
                    if ((GoS == TAbstractPDU.EXP_LEVEL0_WITHOUT_BACKUP_LSP) || (GoS == TAbstractPDU.EXP_LEVEL0_WITH_BACKUP_LSP)) {
                        this.tEMPLS++;
                    } else if ((GoS == TAbstractPDU.EXP_LEVEL1_WITHOUT_BACKUP_LSP) || (GoS == TAbstractPDU.EXP_LEVEL1_WITH_BACKUP_LSP)) {
                        this.tEMPLS_GOS1++;
                    } else if ((GoS == TAbstractPDU.EXP_LEVEL2_WITHOUT_BACKUP_LSP) || (GoS == TAbstractPDU.EXP_LEVEL2_WITH_BACKUP_LSP)) {
                        this.tEMPLS_GOS2++;
                    } else if ((GoS == TAbstractPDU.EXP_LEVEL3_WITHOUT_BACKUP_LSP) || (GoS == TAbstractPDU.EXP_LEVEL3_WITH_BACKUP_LSP)) {
                        this.tEMPLS_GOS3++;
                    }
                }
            } else if (tipoPaquete == TAbstractPDU.IPV4) {
                if (entrada == TStats.INCOMING) {
                    this.tEIPV4++;
                }
            } else if (tipoPaquete == TAbstractPDU.IPV4_GOS) {
                GoS = paquete.getIPv4Header().getOptionsField().getRequestedGoSLevel();
                if (entrada == TStats.INCOMING) {
                    if ((GoS == TAbstractPDU.EXP_LEVEL0_WITHOUT_BACKUP_LSP) || (GoS == TAbstractPDU.EXP_LEVEL0_WITH_BACKUP_LSP)) {
                        this.tEIPV4++;
                    } else if ((GoS == TAbstractPDU.EXP_LEVEL1_WITHOUT_BACKUP_LSP) || (GoS == TAbstractPDU.EXP_LEVEL1_WITH_BACKUP_LSP)) {
                        this.tEIPV4_GOS1++;
                    } else if ((GoS == TAbstractPDU.EXP_LEVEL2_WITHOUT_BACKUP_LSP) || (GoS == TAbstractPDU.EXP_LEVEL2_WITH_BACKUP_LSP)) {
                        this.tEIPV4_GOS2++;
                    } else if ((GoS == TAbstractPDU.EXP_LEVEL3_WITHOUT_BACKUP_LSP) || (GoS == TAbstractPDU.EXP_LEVEL3_WITH_BACKUP_LSP)) {
                        this.tEIPV4_GOS3++;
                    }
                }
            }
        }
    }
    
    /**
     * Este m�todo permite obtener el n�mero de graficas del nodo receptor.
     * @return N�mero de gr�ficas del nodo receptor.
     * @since 2.0
     */    
    public int numberOfAvailableDatasets() {
        return 1;
    }
    
    /**
     * Este m�todo reinicia los atributos de la clase, dejando las instancia como si
     * acabase de ser creada por el constructor.
     * @since 2.0
     */    
    public void reset() {
    	paquetesEntrantes = new XYSeriesCollection();
    	entrantesIPv4 = new XYSeries(TStats.IPV4);
    	entrantesIPv4_GOS1 = new XYSeries(TStats.IPV4_GOS1);
    	entrantesIPv4_GOS2 = new XYSeries(TStats.IPV4_GOS2);
    	entrantesIPv4_GOS3 = new XYSeries(TStats.IPV4_GOS3);
    	entrantesMPLS = new XYSeries(TStats.MPLS);
    	entrantesMPLS_GOS1 = new XYSeries(TStats.MPLS_GOS1);
    	entrantesMPLS_GOS2 = new XYSeries(TStats.MPLS_GOS2);
    	entrantesMPLS_GOS3 = new XYSeries(TStats.MPLS_GOS3);
    	entrantesGPSRP = new XYSeries(TStats.GPSRP);
        tEIPV4 = 0;
        tEIPV4_GOS1 = 0;
        tEIPV4_GOS2 = 0;
        tEIPV4_GOS3 = 0;
        tEMPLS = 0;
        tEMPLS_GOS1 = 0;
        tEMPLS_GOS2 = 0;
        tEMPLS_GOS3 = 0;
        tEGPSRP = 0;
    }
    
    /**
     * Este m�todo permite ampliar las estad�sticas con los ultimos datos especificados
     * desde la ultima vez que se llam� a este m�todo.
     * @param instante Instante de tiempo al que se atribuir�n los ultimos datos recolectados.
     * @since 2.0
     */    
    public void consolidateData(long instante) {
        if (this.statsEnabled) {
            if (tEIPV4 > 0) {
                if (entrantesIPv4.getItemCount() == 0) {
                    this.entrantesIPv4.add(instante-1, 0 );
                    this.entrantesIPv4.add(instante, tEIPV4);
                    this.paquetesEntrantes.addSeries(entrantesIPv4);
                } else {
                    this.entrantesIPv4.add(instante, tEIPV4);
                }
            }
            
            if (tEIPV4_GOS1 > 0) {
                if (entrantesIPv4_GOS1.getItemCount() == 0) {
                    this.entrantesIPv4_GOS1.add(instante-1, 0 );
                    this.entrantesIPv4_GOS1.add(instante, tEIPV4_GOS1);
                    this.paquetesEntrantes.addSeries(entrantesIPv4_GOS1);
                } else {
                    this.entrantesIPv4_GOS1.add(instante, tEIPV4_GOS1);
                }
            }
            
            if (tEIPV4_GOS2 > 0) {
                if (entrantesIPv4_GOS2.getItemCount() == 0) {
                    this.entrantesIPv4_GOS2.add(instante-1, 0 );
                    this.entrantesIPv4_GOS2.add(instante, tEIPV4_GOS2);
                    this.paquetesEntrantes.addSeries(entrantesIPv4_GOS2);
                } else {
                    this.entrantesIPv4_GOS2.add(instante, tEIPV4_GOS2);
                }
            }
            
            if (tEIPV4_GOS3 > 0) {
                if (entrantesIPv4_GOS3.getItemCount() == 0) {
                    this.entrantesIPv4_GOS3.add(instante-1, 0 );
                    this.entrantesIPv4_GOS3.add(instante, tEIPV4_GOS3);
                    this.paquetesEntrantes.addSeries(entrantesIPv4_GOS3);
                } else {
                    this.entrantesIPv4_GOS3.add(instante, tEIPV4_GOS3);
                }
            }
            
            if (tEMPLS > 0) {
                if (entrantesMPLS.getItemCount() == 0) {
                    this.entrantesMPLS.add(instante-1, 0 );
                    this.entrantesMPLS.add(instante, tEMPLS);
                    this.paquetesEntrantes.addSeries(entrantesMPLS);
                } else {
                    this.entrantesMPLS.add(instante, tEMPLS);
                }
            }
            
            if (tEMPLS_GOS1 > 0) {
                if (entrantesMPLS_GOS1.getItemCount() == 0) {
                    this.entrantesMPLS_GOS1.add(instante-1, 0 );
                    this.entrantesMPLS_GOS1.add(instante, tEMPLS_GOS1);
                    this.paquetesEntrantes.addSeries(entrantesMPLS_GOS1);
                } else {
                    this.entrantesMPLS_GOS1.add(instante, tEMPLS_GOS1);
                }
            }
            
            if (tEMPLS_GOS2 > 0) {
                if (entrantesMPLS_GOS2.getItemCount() == 0) {
                    this.entrantesMPLS_GOS2.add(instante-1, 0 );
                    this.entrantesMPLS_GOS2.add(instante, tEMPLS_GOS2);
                    this.paquetesEntrantes.addSeries(entrantesMPLS_GOS2);
                } else {
                    this.entrantesMPLS_GOS2.add(instante, tEMPLS_GOS2);
                }
            }
            
            if (tEMPLS_GOS3 > 0) {
                if (entrantesMPLS_GOS3.getItemCount() == 0) {
                    this.entrantesMPLS_GOS3.add(instante-1, 0 );
                    this.entrantesMPLS_GOS3.add(instante, tEMPLS_GOS3);
                    this.paquetesEntrantes.addSeries(entrantesMPLS_GOS3);
                } else {
                    this.entrantesMPLS_GOS3.add(instante, tEMPLS_GOS3);
                }
            }
            
            if (tEGPSRP > 0) {
                if (entrantesGPSRP.getItemCount() == 0) {
                    this.entrantesGPSRP.add(instante-1, 0 );
                    this.entrantesGPSRP.add(instante, tEGPSRP);
                    this.paquetesEntrantes.addSeries(entrantesGPSRP);
                } else {
                    this.entrantesGPSRP.add(instante, tEGPSRP);
                }
            }
        }
    }    
    
    /**
     * Este m�todo permite obtener el t�tulo de la gr�fica 1.
     * @return T�tulo de la gr�fica 1.
     * @since 2.0
     */    
    public String getTitleOfDataset1() {
        return TStats.INCOMING_PACKETS;
    }
    
    /**
     * Este m�todo permite obtener el t�tulo de la gr�fica 2.
     * @return T�tulo de la gr�fica 2.
     * @since 2.0
     */    
    public String getTitleOfDataset2() {
        return null;
    }
    
    /**
     * Este m�todo permite obtener el t�tulo de la gr�fica 3.
     * @return T�tulo de la gr�fica 3.
     * @since 2.0
     */    
    public String getTitleOfDataset3() {
        return null;
    }
    
    /**
     * Este m�todo permite obtener el t�tulo de la gr�fica 4.
     * @return T�tulo de la gr�fica 4.
     * @since 2.0
     */    
    public String getTitleOfDataset4() {
        return null;
    }
    
    /**
     * Este m�todo permite obtener el t�tulo de la gr�fica 5.
     * @return T�tulo de la gr�fica 5.
     * @since 2.0
     */    
    public String getTitleOfDataset5() {
        return null;
    }
    
    /**
     * Este m�todo permite obtener el t�tulo de la gr�fica 6.
     * @return T�tulo de la gr�fica 6.
     * @since 2.0
     */    
    public String getTitleOfDataset6() {
        return null;
    }
    
    private int tEIPV4;
    private int tEIPV4_GOS1;
    private int tEIPV4_GOS2;
    private int tEIPV4_GOS3;
    private int tEMPLS;
    private int tEMPLS_GOS1;
    private int tEMPLS_GOS2;
    private int tEMPLS_GOS3;
    private int tEGPSRP;
    private XYSeriesCollection paquetesEntrantes;
    private XYSeries entrantesIPv4;
    private XYSeries entrantesIPv4_GOS1;
    private XYSeries entrantesIPv4_GOS2;
    private XYSeries entrantesIPv4_GOS3;
    private XYSeries entrantesMPLS;
    private XYSeries entrantesMPLS_GOS1;
    private XYSeries entrantesMPLS_GOS2;
    private XYSeries entrantesMPLS_GOS3;
    private XYSeries entrantesGPSRP;
}
