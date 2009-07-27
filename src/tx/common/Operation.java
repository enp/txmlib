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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class Operation {

	private String action;
	private Map<String,String> attributes = new HashMap<String,String>();
	private Map<String,Map<String,String>> devices = new HashMap<String,Map<String,String>>();

	public Operation() {}
	
	public Operation(String action, Map<String, String> attributes, Map<String,Map<String,String>> devices) {
		this.action = action;
		this.attributes = attributes;
		this.devices = devices;
	}	
	
	public String getAction() {
		return action;
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public Map<String,Map<String,String>> getDevices() {
		return devices;
	}
	
}
