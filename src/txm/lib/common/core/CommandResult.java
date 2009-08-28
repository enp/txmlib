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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class CommandResult {
	
	@SuppressWarnings("unused")
	private long id;
	
	protected String text;	
	protected List<Attribute> attributes = new ArrayList<Attribute>();

	public CommandResult(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
	public void addAttribute(String key, String value) {
		attributes.add(new Attribute(key, value));
	}
	
	public List<Attribute> getAttributes() {
		return Collections.unmodifiableList(attributes);
	}
	
	public String getAttribute(String key) {
		for (Attribute attribute : attributes)
			if (attribute.getName().equals(key))
				return attribute.getValue();
		return null;
	}

}
