/*
 * Copyright 2009 Eugene Prokopiev <eugene.prokopiev@gmail.com>
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
package txm.lib.examples;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import txm.lib.common.core.CommandDump;
import txm.lib.common.core.Operation;
import txm.lib.common.core.OperationManager;

import com.thoughtworks.xstream.XStream;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class OperationExample {
	
	public static void main(String[] args) throws Exception {
		
		Properties params;
		
		params = new Properties();
		params.load(new FileInputStream("conf/operation.conf"));
		
		String type = params.getProperty("type");
		String operationManagerName = "txm.lib.impl."+type.toLowerCase()+"."+type+"OperationManager";		
		String action = params.getProperty("action");
		String device = params.getProperty("device");
		
		OperationManager operationManager = 
			(OperationManager)Class.forName(operationManagerName).getConstructor(new Class[] {}).newInstance(new Object[] {});
		
		params = new Properties();
		params.load(new FileInputStream("conf/"+type.toLowerCase()+".conf"));
		
		CommandDump commandDump = new CommandDump("dump/operation.txt");
		
		operationManager.connect(params, commandDump);	
		
		List<String> devices = new ArrayList<String>();
		devices.add(device);
		Operation operation = new Operation(action, devices, null);
		
		operationManager.execute(operation);
		
		operationManager.disconnect();
		
		new XStream().toXML(operation, new FileOutputStream("dump/operation.xml"));
		
	}
}
