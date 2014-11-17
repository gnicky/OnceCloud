package com.oncecloud.main;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class SSH {

	/**
	 * The host name of the agent, in form of IP address
	 */
	private String hostname;

	/**
	 * The user name of the agent
	 */
	private String username;

	/**
	 * The password of the agent, corresponds to the user-name
	 */
	private String password;

	/**
	 * SSH connection between the console and agent
	 */
	private static Connection conn;

	private Session session;

	private Integer exitCode = -1;

	public Integer getExitCode() {
		return exitCode;
	}

	public SSH(String hostname, String username, String password) {
		this.hostname = hostname;
		this.username = username;
		this.password = password;
	}

	public boolean Connect() {
		boolean isAuthenticated = false;
		conn = new Connection(hostname);
		try {
			conn.connect();
			isAuthenticated = conn.authenticateWithPassword(username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isAuthenticated;
	}

	public int Command(String commandLine) throws Exception {
		session = conn.openSession();
		session.execCommand(commandLine);
		InputStream stdout = new StreamGobbler(session.getStdout());
		InputStream stderr = new StreamGobbler(session.getStderr());

		BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(
				stdout));
		BufferedReader stderrReader = new BufferedReader(new InputStreamReader(
				stderr));

		while (true) {
			String line = stdoutReader.readLine();
			if (line == null)
				break;
		}

		while (true) {
			String line = stderrReader.readLine();
			if (line == null)
				break;
		}
		int strexit = session.getExitStatus();
		session.close();

		return strexit;
	}

	public boolean SCPFile(String localFile, String remoteDir) {
		SCPClient cp = new SCPClient(conn);
		try {
			cp.put(localFile, remoteDir);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean rmFile(String toDelFileName) {
		try {
			SFTPv3Client sftpClient = new SFTPv3Client(conn);
			sftpClient.rm(toDelFileName);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void CloseSsh() {
		conn.close();
	}

	public String toString() {
		return hostname + "/" + username + "/" + password;
	}

	public boolean GetFile(String remoteFile, String localDir) {
		SCPClient sc = new SCPClient(conn);
		try {
			sc.get(remoteFile, localDir);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean MakeDir(String dir) {
		try {
			this.Command("test -d " + dir + " || mkdir " + dir);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
