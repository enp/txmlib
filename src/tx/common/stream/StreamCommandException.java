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
package tx.common.stream;

import java.util.HashSet;
import java.util.Set;

import tx.common.MatchException;


/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class StreamCommandException extends MatchException {

	private static final long serialVersionUID = 5485195737568383806L;
	
	private long begin;
	private long end;

	public StreamCommandException(Set<String> expected, String actual, long begin, long end) {
		super(expected, actual);
		this.begin = begin;
		this.end = end;
	}
	
	private static Set<String> extectedFromArray(String[] expected) {
		Set<String> e = new HashSet<String>();
		for(String s : expected)
			e.add(s);
		return e;
	}

	public StreamCommandException(String[] expected, String actual, long begin, long end) {
		super(extectedFromArray(expected), actual);
		this.begin = begin;
		this.end = end;
	}

	public long getBegin() {
		return begin;
	}

	public long getEnd() {
		return end;
	}

}
