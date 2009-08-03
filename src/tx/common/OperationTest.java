/*
 * Copyright 2009 Eugene Prokopiev <eugene.prokopiev@gmail.com>
 * 
 * This file is part of TXManager (Telephone eXchange Manager).
 *
 * TXManager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TXManager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TXManager. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package tx.common;

import java.io.FileInputStream;
import java.util.Properties;

import com.thoughtworks.xstream.XStream;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class OperationTest {
	
	private String type(OperationManager operationManager) {
		return operationManager.getClass().getSimpleName().replace("OperationManager", "").toLowerCase();
	}
	
	protected void execute(OperationManager operationManager) throws Exception {
		
		Properties params = new Properties();
		params.load(new FileInputStream("conf/"+type(operationManager)+".conf"));
		
		CommandDump commandDump = new TextFileCommandDump("dump/"+type(operationManager)+".txt");		
		OperationDump operationDump = new XmlFileOperationDump("dump/"+type(operationManager)+".xml");
		
		operationManager.connect(params, commandDump, operationDump);
		
		XStream xstream = new XStream();
		xstream.alias("operation", tx.common.Operation.class);
		xstream.useAttributeFor(tx.common.Operation.class, "action");
		
		Operation operation = (Operation)xstream.fromXML(new FileInputStream("exec/"+type(operationManager)+"-operation.xml"));		
		
		operationManager.execute(operation);
		
		operationManager.disconnect();
		
	}
}
