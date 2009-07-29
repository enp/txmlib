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
package tx.dx;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import tx.common.CommandDump;
import tx.common.Operation;
import tx.common.OperationDump;
import tx.common.OperationManager;
import tx.common.TextFileCommandDump;
import tx.common.XmlFileOperationDump;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class DxOperationTest {

	public static void main(String[] args) throws Exception {
		
		OperationManager operationManager = new DxOperationManager();
		
		Properties params = new Properties();
		params.load(new FileInputStream("conf/dx.conf"));
		
		CommandDump commandDump = new TextFileCommandDump("dump/dx.txt");
		
		OperationDump operationDump = new XmlFileOperationDump("dump/dx.xml");
		
		Map<String,Map<String,String>> devices = new HashMap<String,Map<String,String>>();
		devices.put("2631000", null);
		
		operationManager.connect(params, commandDump, operationDump);	
		
		operationManager.execute(new Operation("linetest", devices, null));
		
		operationManager.disconnect();
		
	}

}
