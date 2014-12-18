/**
 * Copyright (2014, ) Institute of Software, Chinese Academy of Sciences
 */
package com.oncecloud.ha.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.oncecloud.ha.core.entities.SSHEntity;

/**
 * @author henry
 * @date   2014年9月23日
 *
 */
public class SSHUtils {

	public synchronized static Channel createSSHChannel(SSHEntity ssh) {
		return createSSHChannel(ssh.getUrl(), ssh.getPort(), ssh.getUser(),
				ssh.getPwd(), ssh.getTimeout());
	}

	public synchronized static Channel createSSHChannel(String target, int port,
			String user, String pwd, int timeout) {
		JSch jsch = new JSch();
		Session session;
		try {
			session = jsch.getSession(user, target, port);
			session.setPassword(pwd);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect(timeout);
			return session.openChannel("exec");
		} catch (JSchException e) {
			return null;
		}

	}
	
	public final static String getOvsIP(Channel channel, String name) {
		String command = "ifconfig " + name + " | grep 'inet addr'";
		BufferedReader br = null;
		String line = null;
		if (channel != null) {
			((ChannelExec) channel).setCommand(command);
			try {
				channel.setInputStream(null);
				((ChannelExec) channel).setErrStream(System.err);
				br = new BufferedReader(new InputStreamReader(
						channel.getInputStream()));
				channel.connect();
				line = br.readLine();
				int start = line.indexOf("addr");
				int end = line.indexOf(" Bcast");
				line = line.substring(start + 5, end).trim();
			} catch (Exception e) {
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return line;
	}
	
	public final static boolean copyVMConfig(SSHEntity ssh, String srcDir,
			String destDir) {
		Channel channel = createSSHChannel(ssh.getUrl(), ssh.getPort(),
				ssh.getUser(), ssh.getPwd(), ssh.getTimeout());
		boolean sucessful = execCommand(channel,
				dirCopyCommand(srcDir, destDir));
		closeSSHConnection(channel);
		return sucessful;
	}
	
	private final static String dirCopyCommand(String srcDir, String destDir) {
		return "cp -r " + srcDir + " " + destDir;
	}
	
	private static void closeSSHConnection(Channel channel) {
		Session session = null;
		if (channel != null) {
			try {
				session = channel.getSession();
			} catch (JSchException e) {
				//
			}
			channel.disconnect();
		}

		if (session != null) {
			session.disconnect();
		}
	}
	
	private static boolean execCommand(Channel channel, String command) {
		boolean sucessful = false;
		BufferedReader br = null;
		if (channel != null) {
			((ChannelExec) channel).setCommand(command);
			try {
				channel.setInputStream(null);
				((ChannelExec) channel).setErrStream(System.err);
				br = new BufferedReader(new InputStreamReader(
						channel.getInputStream()));
				channel.connect();
				String line = null;
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
				sucessful = true;
			} catch (Exception e) {
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return sucessful;
	}
	
	public final static boolean restartXend(SSHEntity ssh) {
		Channel channel = createSSHChannel(ssh.getUrl(), ssh.getPort(),
				ssh.getUser(), ssh.getPwd(), ssh.getTimeout());
		boolean sucessful = execCommand(channel, "/etc/init.d/xend restart");
		closeSSHConnection(channel);
		return sucessful;
	}
	
	public final static boolean removeAllVMsConfig(SSHEntity ssh) {
		Channel channel = createSSHChannel(ssh.getUrl(), ssh.getPort(),
				ssh.getUser(), ssh.getPwd(), ssh.getTimeout());
		boolean sucessful = execCommand(channel, "rm -rf /var/lib/xend/domains/*");
		closeSSHConnection(channel);
		return sucessful;
	}
	
	public final static boolean removeAllVMsConfig(SSHEntity ssh, String uuid) {
		Channel channel = createSSHChannel(ssh.getUrl(), ssh.getPort(),
				ssh.getUser(), ssh.getPwd(), ssh.getTimeout());
		boolean sucessful = execCommand(channel, "rm -rf /var/lib/xend/domains/" + uuid);
		closeSSHConnection(channel);
		return sucessful;
	}
	
	public final static boolean mountAll(SSHEntity ssh) {
		Channel channel = createSSHChannel(ssh.getUrl(), ssh.getPort(),
				ssh.getUser(), ssh.getPwd(), ssh.getTimeout());
		boolean sucessful = execCommand(channel, "/bin/mount -a");
		closeSSHConnection(channel);
		return sucessful;
	}
	
	public final static boolean startSRagent(SSHEntity ssh) {
		Channel channel = createSSHChannel(ssh.getUrl(), ssh.getPort(),
				ssh.getUser(), ssh.getPwd(), ssh.getTimeout());
		boolean sucessful = execCommand(channel, "/etc/init.d/sragent start");
		closeSSHConnection(channel);
		return sucessful;
	}
	
	public final static boolean startPasswd(SSHEntity ssh) {
		Channel channel = createSSHChannel(ssh.getUrl(), ssh.getPort(),
				ssh.getUser(), ssh.getPwd(), ssh.getTimeout());
		boolean sucessful = execCommand(channel, "/etc/init.d/setPasswd start");
		closeSSHConnection(channel);
		return sucessful;
	}

}
