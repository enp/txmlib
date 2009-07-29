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
package tx.ewsd;

import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import tx.common.Command;
import tx.common.CommandDump;
import tx.common.PullCommandManager;
import tx.common.PullCommandTest;
import tx.common.TextFileCommandDump;

/**
 * @author Eugene Prokopiev <eugene.prokopiev@gmail.com>
 *
 */
public class EwsdCommandTest extends PullCommandTest {

	public static void main(String[] args) throws Exception {
		
		PullCommandManager manager = new EwsdCommandManager();
		
		Properties params = new Properties();
		params.load(new FileInputStream("conf/ewsd.conf"));
		
		CommandDump dump = new TextFileCommandDump("dump/ewsd.dump");
		
		Map<Command,String[]> commands = new LinkedHashMap<Command,String[]>();
		
		//commands.put(new Command("DISPTIME"), new String[] {"TASK SUBMITTED","TASK EXECUTED"});
		
		commands.put(new Command("ACTWST:DN=2687534"), new String[] {
			"COMMAND SUBMITTED",
			"ACCEPTED",
			"EXEC'D",
			"NEXT CALLTYPE FOR ACCEPTANCE"
		});
		
		commands.put(new Command("STARTLTEST:LAC=8863,DN=2310000"), new String[] {
			"COMMAND SUBMITTED",
			"EXEC'D"
		});
		
		commands.put(new Command("TESTLINE:FCT=GT"), new String[] {
			"COMMAND SUBMITTED",
			"ACCEPTED",
			"EXEC'D"
		});
		
		commands.put(new Command("TESTLINE:FCT=GR"), new String[] {
			"COMMAND SUBMITTED",
			"EXEC'D"
		});
		
		commands.put(new Command("DACTWST"), new String[] {
			"COMMAND SUBMITTED",
			"EXEC'D"
		});
		
		new EwsdCommandTest().executeCommands(manager, params, dump, commands);
		
	}

}
