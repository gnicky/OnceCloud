package com.oncecloud.mom;

import com.oncecloud.main.OnceConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class RecvVMSyncThread extends Thread {
	private static final String EXCHANGE_NAME = "oncecloud";
	private static final String AMPQHostIP = OnceConfig.getValue("rabbitmq");
	private static final int Port = 5672;
	private static final String HostUser = "root";
	private static final String HostPwd = "onceas";
	private ProcessMsg processMsg;

	private ProcessMsg getProcessMsg() {
		return processMsg;
	}

	private void setProcessMsg(ProcessMsg processMsg) {
		this.processMsg = processMsg;
	}

	public RecvVMSyncThread(ProcessMsg processMsg) {
		this.setProcessMsg(processMsg);
	}

	public void run() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(AMPQHostIP);
		factory.setPort(Port);
		factory.setUsername(HostUser);
		factory.setPassword(HostPwd);
		Connection connection = null;
		try {
			connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "topic");
			String queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, EXCHANGE_NAME, "SyncVM");
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, true, consumer);
			while (true) {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				this.getProcessMsg().ProcessSync(message);
				System.out.println(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
