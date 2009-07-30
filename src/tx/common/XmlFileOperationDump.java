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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class XmlFileOperationDump implements OperationDump {

	private String fileName;
	private FileOutputStream fos;
	private XStream xstream;
	
	public XmlFileOperationDump(String fileName) throws FileNotFoundException {
		this.fileName = fileName;
		this.fos = new FileOutputStream(fileName);
		this.xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.alias("operation", tx.common.Operation.class);
		xstream.alias("command", tx.common.Command.class);
		xstream.alias("result", tx.common.StreamCommandResult.class);
		xstream.omitField(tx.common.TextFileCommandDump.class, "fos");
		xstream.useAttributeFor(tx.common.Operation.class, "action");
	}
	
	public String getFileName() {
		return fileName;
	}

	@Override
	public void dump(Operation operation) {
		//xstream.toXML(operation, fos);
		xstream.marshal(operation, new PrettyPrintWriter(new OutputStreamWriter(fos)){
			public void addAttribute(String key, String value) {
				if (!key.equals("class")) super.addAttribute(key, value);
			}
		});
	}

}