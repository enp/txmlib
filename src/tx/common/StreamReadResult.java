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

public class StreamReadResult {	

	private int index;
	private String text;
	private int begin;
	private int end;
	
	public StreamReadResult(int index, String text, int begin, int end) {
		this.index = index;
		this.text = text;
		this.begin = begin;
		this.end = end;
	}
	
	public String toString() {
		return index + " : " + text;
	}

	public int getIndex() {
		return index;
	}

	public String getText() {
		return text;
	}

	public int getBegin() {
		return begin;
	}

	public int getEnd() {
		return end;
	}

}
