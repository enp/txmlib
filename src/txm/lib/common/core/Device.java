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
public class Device {
	
	@SuppressWarnings("unused")
	private long id;
	
	private String name;
	private List<Attribute> attributes;

	public Device(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void addAttribute(String name, String value) {
		if (attributes == null)
			attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute(name, value));
	}
	
	public List<Attribute> getAttributes() {
		return Collections.unmodifiableList(attributes);
	}

}
