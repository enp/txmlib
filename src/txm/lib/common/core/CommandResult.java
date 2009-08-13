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
package txm.lib.common.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class CommandResult {
	
	protected String text;
	
	protected Map<String,String> attributes;
	
	public CommandResult() {}

	public CommandResult(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
	public void addAttribute(String key, String value) {
		if (attributes == null)
			attributes = new HashMap<String,String>();
		attributes.put(key, value);
	}
	
	public Map<String,String> getAttributes() {
		return (attributes == null)?null:Collections.unmodifiableMap(attributes);
	}
	
	public String getAttribute(String key) {
		return attributes.get(key);
	}

}
