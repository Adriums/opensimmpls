/* 
 * Copyright 2015 (C) Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com.
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
package simMPLS.hardware.dmgp;

import java.util.Iterator;
import java.util.TreeSet;
import simMPLS.protocols.TMPLSPDU;
import simMPLS.utils.TRotaryIDGenerator;
import simMPLS.utils.TMonitor;

/**
 * This class implements a table where received requests for retrnasmission will
 * be stored while they wait to be managed.
 *
 * @author Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com
 * @version 2.0
 */
public class TGPSRPRequestsMatrix {

    /**
     * This is the class constructor. It creates a new instance of
     * TGPSRPRequestsMatrix.
     *
     * @author Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com
     * @since 2.0
     */
    public TGPSRPRequestsMatrix() {
        this.entries = new TreeSet();
        this.idGenerator = new TRotaryIDGenerator();
        this.monitor = new TMonitor();
    }

    /**
     * This method reset all attributes od the class to its original values, as
     * when created by the constructor.
     *
     * @author Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com
     * @since 2.0
     */
    public void reset() {
        this.entries = null;
        this.idGenerator = null;
        this.monitor = null;
        this.entries = new TreeSet();
        this.idGenerator = new TRotaryIDGenerator();
        this.monitor = new TMonitor();
    }

    /**
     * This method update the outgoing port of for all the matching entries. It
     * will be changed for a new outgoing port.
     *
     * @author Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com
     * @param currentOutgoingPort The port that is goint to be replaced.
     * @param newOutgoingPort The new outgoing port for matching entries.
     * @since 2.0
     */
    public void updateOutgoingPort(int currentOutgoingPort, int newOutgoingPort) {
        this.monitor.lock();
        Iterator iterator = this.entries.iterator();
        TGPSRPRequestEntry gpsrpRequestEntry = null;
        while (iterator.hasNext()) {
            gpsrpRequestEntry = (TGPSRPRequestEntry) iterator.next();
            if (gpsrpRequestEntry.getOutgoingPort() == currentOutgoingPort) {
                gpsrpRequestEntry.setOutgoingPort(newOutgoingPort);
            }
        }
        this.monitor.unLock();
    }

    /**
     * This method removes from the table all entries that have the outgoing
     * port equal than the one specified as an argument.
     *
     * @author Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com
     * @param oldOutgoingPort Port that must match the outgoing port of entries to be
     * removed.
     * @since 2.0
     */
    public void removeEntriesMatchingOutgoingPort(int oldOutgoingPort) {
        this.monitor.lock();
        Iterator iterator = this.entries.iterator();
        TGPSRPRequestEntry gpsrpRequestEntry = null;
        while (iterator.hasNext()) {
            gpsrpRequestEntry = (TGPSRPRequestEntry) iterator.next();
            if (gpsrpRequestEntry.getOutgoingPort() == oldOutgoingPort) {
                iterator.remove();
            }
        }
        this.monitor.unLock();
    }

    /**
     * This method insert a new entry in the table.
     *
     * @author Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com
     * @param mplsPacket Packet for wich the retransmission is going to be
     * requested.
     * @param incomingPort Incoming port of the packet. It will be the outgoing port
     * for the retransmission request.
     * @return The new created an inserted entry. Otherwise, NULL.
     * @since 2.0
     */
    public TGPSRPRequestEntry addEntry(TMPLSPDU mplsPacket, int incomingPort) {
        this.monitor.lock();
        TGPSRPRequestEntry gpsrpRequestEntry = new TGPSRPRequestEntry(this.idGenerator.getNextID());
        gpsrpRequestEntry.setOutgoingPort(incomingPort);
        gpsrpRequestEntry.setFlowID(mplsPacket.getIPv4Header().getOriginIPv4Address().hashCode());
        gpsrpRequestEntry.setPacketID(mplsPacket.getIPv4Header().getGoSGlobalUniqueIdentifier());
        int numberOfCrossedNodes = mplsPacket.getIPv4Header().getOptionsField().getNumberOfCrossedActiveNodes();
        int i = 0;
        String nextIPv4 = "";
        for (i = 0; i < numberOfCrossedNodes; i++) {
            nextIPv4 = mplsPacket.getIPv4Header().getOptionsField().getCrossedActiveNode(i);
            if (nextIPv4 != null) {
                gpsrpRequestEntry.setCrossedNodeIP(nextIPv4);
            }
        }
        entries.add(gpsrpRequestEntry);
        this.monitor.unLock();
        return gpsrpRequestEntry;
    }

    /**
     * This method removes a entry from the table.
     *
     * @author Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com
     * @param flowID Flow the entry refers to.
     * @param packetID Packet the table refers to.
     * @since 2.0
     */
    public void removeEntry(int flowID, int packetID) {
        this.monitor.lock();
        Iterator iterator = this.entries.iterator();
        TGPSRPRequestEntry gpsrpRequestEntry = null;
        while (iterator.hasNext()) {
            gpsrpRequestEntry = (TGPSRPRequestEntry) iterator.next();
            if (gpsrpRequestEntry.getFlowID() == flowID) {
                if (gpsrpRequestEntry.getPacketID() == packetID) {
                    iterator.remove();
                }
            }
        }
        this.monitor.unLock();
    }

    /**
     * This method obtains a specific entry from the table.
     *
     * @author Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com
     * @param flowID Flow identifier the entry refers to.
     * @param packetID Packet identifier the entry refers to.
     * @return Entry matching the specified arguments. Otherwise, NULL.
     * @since 2.0
     */
    public TGPSRPRequestEntry getEntry(int flowID, int packetID) {
        this.monitor.lock();
        Iterator iterator = this.entries.iterator();
        TGPSRPRequestEntry gpsrpRequestEntry = null;
        while (iterator.hasNext()) {
            gpsrpRequestEntry = (TGPSRPRequestEntry) iterator.next();
            if (gpsrpRequestEntry.getFlowID() == flowID) {
                if (gpsrpRequestEntry.getPacketID() == packetID) {
                    this.monitor.unLock();
                    return gpsrpRequestEntry;
                }
            }
        }
        this.monitor.unLock();
        return null;
    }

    /**
     * This method updates the table. It removes all entries for which no
     * retransmission attemps are available and their timeouts have expired.
     *
     * @author Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com
     * @since 2.0
     */
    public void updateEntries() {
        this.monitor.lock();
        Iterator iterator = this.entries.iterator();
        TGPSRPRequestEntry gpsrpRequestEntry = null;
        while (iterator.hasNext()) {
            gpsrpRequestEntry = (TGPSRPRequestEntry) iterator.next();
            if (gpsrpRequestEntry.isPurgeable()) {
                iterator.remove();
            } else {
                gpsrpRequestEntry.resetTimeout();
            }
        }
        this.monitor.unLock();
    }

    /**
     * This method drecreases the timeout for all entries of the table.
     *
     * @author Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com
     * @param nanoseconds Number of nanoseconds to be decreased from all entries timeouts.
     * @since 2.0
     */
    public void decreaseTimeout(int nanoseconds) {
        this.monitor.lock();
        Iterator iterator = this.entries.iterator();
        TGPSRPRequestEntry gpsrpRequestEntry = null;
        while (iterator.hasNext()) {
            gpsrpRequestEntry = (TGPSRPRequestEntry) iterator.next();
            gpsrpRequestEntry.decreaseTimeout(nanoseconds);
        }
        this.monitor.unLock();
    }

    /**
     * This method obtains the outgoing port of a specific entry.
     *
     * @author Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com
     * @param flowID Flow identifier the entry refers to.
     * @param packetID Packet identifier the entry refers to.
     * @return Outgoing port of the entry maching the specified arguments.
     * @since 2.0
     */
    public int getOutgoingPort(int flowID, int packetID) {
        this.monitor.lock();
        Iterator iterator = this.entries.iterator();
        TGPSRPRequestEntry gpsrpRequestEntry = null;
        while (iterator.hasNext()) {
            gpsrpRequestEntry = (TGPSRPRequestEntry) iterator.next();
            if (gpsrpRequestEntry.getFlowID() == flowID) {
                if (gpsrpRequestEntry.getPacketID() == packetID) {
                    this.monitor.unLock();
                    return gpsrpRequestEntry.getOutgoingPort();
                }
            }
        }
        this.monitor.unLock();
        return -1;
    }

    /**
     * Thism method obtains the IP address of the following node that should be
     * requested for a packet retransmission.
     *
     * @author Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com
     * @param flowID Flow identifier of the desired entry.
     * @param packetID Packet identifier of the desired entry.
     * @return IP address of the following node to be requested for a packet
     * retransmission. Otherwise, NULL.
     * @since 2.0
     */
    public String getActiveNodeIP(int flowID, int packetID) {
        this.monitor.lock();
        Iterator iterator = this.entries.iterator();
        TGPSRPRequestEntry gpsrpRequestEntry = null;
        while (iterator.hasNext()) {
            gpsrpRequestEntry = (TGPSRPRequestEntry) iterator.next();
            if (gpsrpRequestEntry.getFlowID() == flowID) {
                if (gpsrpRequestEntry.getPacketID() == packetID) {
                    this.monitor.unLock();
                    return gpsrpRequestEntry.getCrossedNodeIPv4();
                }
            }
        }
        this.monitor.unLock();
        return null;
    }

    /**
     * This method obtains the interator of all entries of the table.
     *
     * @author Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com
     * @return Iterator of all entries in the table.
     * @since 2.0
     */
    public Iterator getEntriesIterator() {
        return entries.iterator();
    }

    /**
     * This method allow accesing the sync monitor of this table.
     *
     * @author Manuel Domínguez Dorado - ingeniero@ManoloDominguez.com
     * @return Sync monitor of the table.
     * @since 2.0
     */
    public TMonitor getMonitor() {
        return this.monitor;
    }

    /**
     * Timer used to know when a retransmission request should be retried.
     *
     * @since 2.0
     */
    public static final int TIMEOUT = 50000;
    /**
     * Number of times the rentransmission request should be retried.
     *
     * @since 2.0
     */
    public static final int ATTEMPTS = 8;

    private TreeSet entries;
    private TRotaryIDGenerator idGenerator;
    private TMonitor monitor;
}
