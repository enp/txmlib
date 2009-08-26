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

import java.util.Set;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class MatchError extends Error {

	private static final long serialVersionUID = -8382392967318903080L;
	
	private Set<ExpectedEntry> expected;
	private String actual;
	
	public MatchError() {}

	public MatchError(Set<String> expected, String actual) {
		super("Unexpected result");
		for(String s : expected)
			this.expected.add(new ExpectedEntry(s));
		this.actual = actual;
	}

	public String getExpected() {
		String result = "[";
		for(ExpectedEntry pattern : expected)
			result += ("["+pattern.getEntry()+"]");
		result += "]";
		return result;
	}

	public String getActual() {
		return actual;
	}

}
