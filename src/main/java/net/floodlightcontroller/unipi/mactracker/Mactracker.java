package net.floodlightcontroller.unipi.mactracker;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.projectfloodlight.openflow.protocol.OFFlowAdd;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFPacketOut;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.protocol.action.OFAction;
import org.projectfloodlight.openflow.protocol.action.OFActionOutput;
import org.projectfloodlight.openflow.types.OFBufferId;
import org.projectfloodlight.openflow.types.OFPort;
import org.projectfloodlight.openflow.util.HexString;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.packet.Ethernet;

public class Mactracker implements IOFMessageListener, IFloodlightModule {

	
	protected IFloodlightProviderService floodlightProvider;
	protected static org.slf4j.Logger log;
	
	@Override
	public void init (FloodlightModuleContext context) throws FloodlightModuleException {
		floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
		log = LoggerFactory.getLogger(Mactracker.class);
	}
	
	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		Collection<Class<? extends IFloodlightService>> l = new ArrayList<Class<? extends IFloodlightService>>();
		l.add(IFloodlightProviderService.class);
	return l;
	}
	
	
	@Override
	public String getName() {
		return Mactracker.class.getSimpleName();
	}

	@Override
	public void startUp(FloodlightModuleContext context) {
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
	}
	
	@Override
	public net.floodlightcontroller.core.IListener.Command receive(IOFSwitch sw,
	OFMessage msg, FloodlightContext cntx) {
		
		/*
		Esempio Hello world
		
		
		Ethernet eth = IFloodlightProviderService.bcStore.get(cntx,
				IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
		
		Long sourceMACHash = Ethernet.toLong(eth.getSourceMACAddress().getBytes());
		System.out.printf("MAC Address: {%s} seen on switch: {%s}\n",
		HexString.toHexString(sourceMACHash),
		sw.getId());
		
		return Command.CONTINUE;
		*/
		
		
		
	
		// Cast to Packet-In
		OFPacketIn pi = (OFPacketIn) msg;
		
		// Create the Packet-Out and set basic fields
		/*
		OFPacketOut.Builder pob = sw.getOFFactory().buildPacketOut();
		pob.setBufferId(pi.getBufferId());
		// Create action -> flood the packet on all the ports
		OFActionOutput.Builder actionBuilder = sw.getOFFactory().actions().buildOutput();
		actionBuilder.setPort(OFPort.FLOOD);
		// Assign the action
		pob.setActions(Collections.singletonList((OFAction) actionBuilder.build()));
		
		// Packet might be buffered in the switch or encapsulated in Packet-In
		// If the packet is encapsulated in Packet-In sent it back
		if (pi.getBufferId() == OFBufferId.NO_BUFFER) {
		// Packet-In buffer-id is none, the packet is encapsulated -> send it back
			byte[] packetData = pi.getData();
			pob.setData(packetData);
		}
		// Send the Packet-Out
		sw.write(pob.build());
		// Interrupt the chain
		return Command.STOP;
		*/
		
		
		
		
		
		OFFlowAdd.Builder fmb = sw.getOFFactory().buildFlowAdd();
		fmb.setBufferId(pi.getBufferId()) // Link the new rule to the received OF PKT IN
		.setHardTimeout(20) // Set hard timeout
		.setIdleTimeout(10) // Set soft timeout
		.setPriority(32768) // Set priority
		.setXid(pi.getXid());
		
		OFActionOutput.Builder actionBuilder = sw.getOFFactory().actions().buildOutput();
		actionBuilder.setPort(OFPort.FLOOD); // Set as action the flood of the packet
		fmb.setActions(Collections.singletonList((OFAction) actionBuilder.build()));
		
//		 Send the Packet-Mod
		sw.write(fmb.build());
		// Interrupt the chain
		return Command.STOP;
	}
	
	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
