/*
 * Copyright 2009-2010 Eugene Prokopiev <enp@itx.ru>
 * 
 * This file is part of TXMLib (Telephone eXchange Management Library).
 *
 * TXMLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TXMLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TXMLib. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package ru.itx.txmlib.examples;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import ru.itx.txmlib.common.core.CommandDump;
import ru.itx.txmlib.common.core.Device;
import ru.itx.txmlib.common.core.Operation;
import ru.itx.txmlib.common.core.OperationManager;

import com.thoughtworks.xstream.XStream;

/**
 * @author Eugene Prokopiev <enp@itx.ru>
 *
 */
public class OperationExample {
	
	public static void main(String[] args) throws Exception {
		
		Properties params;
		
		params = new Properties();
		params.load(new FileInputStream("conf/operation.conf"));
		
		String type = params.getProperty("type");
		String operationManagerName = "ru.itx.txmlib.impl."+type.toLowerCase()+"."+type+"OperationManager";
		String action = params.getProperty("action");
		String device = params.getProperty("device");
		
		params = new Properties();
		params.load(new FileInputStream("conf/"+type.toLowerCase()+".conf"));
		
		CommandDump dump = new CommandDump("dump/operation.txt");		

		OperationManager operationManager = 
			(OperationManager)Class.forName(operationManagerName).getConstructor(new Class[] {}).newInstance(new Object[] {});
		operationManager.setName(type.toLowerCase());
		operationManager.setAttributes(params);		
		
		List<Device> devices = new ArrayList<Device>();
		devices.add(new Device(device));
		
		Operation operation = new Operation(operationManager, action, devices, null);
		operation.setDump(dump);
		operation.execute();
		
		new XStream().toXML(operation, new FileOutputStream("dump/operation.xml"));
		
	}
}
