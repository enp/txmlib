/*
 * Copyright 2009-2010 Eugene Prokopiev <enp@itx.ru>
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
package ru.itx.txmlib.common.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Eugene Prokopiev <enp@itx.ru>
 *
 */
public class Command {

	@SuppressWarnings("unused")
	private long id;
	
	private String text;
	private String pullGroup;	
	private List<CommandResult> results = new ArrayList<CommandResult>();
	private Error error;
	
	private Date beginTime;
	private Date endTime;
	
	public Command() {}

	public Command(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

	public void setPullGroup(String number) {
		this.pullGroup = number;		
	}

	public String getPullGroup() {
		return pullGroup;
	}
	
	public void addResult(String result) {
		if (result != null)
			results.add(new CommandResult(result));
	}
	
	public void addResult(CommandResult result) {
		if (result != null)
			results.add(result);
	}

	public CommandResult getResult() {
		if (results.size() > 0)
			return results.get(results.size()-1);
		else
			return null;
	}
	
	public List<CommandResult> getResults() {
		return Collections.unmodifiableList(results);
	}

	public void setError(Error e) {
		this.error = e;		
	}
	
	public Error getError() {
		return error;
	}
	
	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
