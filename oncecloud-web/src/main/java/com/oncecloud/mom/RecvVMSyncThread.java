package com.oncecloud.mom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.main.OnceConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @author hehai
 * @version 2014/09/27
 */
@Component
public class RecvVMSyncThread extends Thread {
	private static final String EXCHANGE_NAME = "oncecloud";
	private static final String AMPQHostIP = OnceConfig.getValue("rabbitmq");
	private static final int Port = 5672;
	private static final String HostUser = "root";
	private static final String HostPwd = "onceas";
	private ProcessMsg processMsg;

	public ProcessMsg getProcessMsg() {
		return processMsg;
	}

	@Autowired
	public void setProcessMsg(ProcessMsg processMsg) {
		this.processMsg = processMsg;
	}
	
	public RecvVMSyncThread() {
		// do nothing
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
			System.out.println("Exchange Name: " + EXCHANGE_NAME);
			channel.exchangeDeclare(EXCHANGE_NAME, "topic");
			String queueName = channel.queueDeclare().getQueue();
			System.out.println("Queue Name: " + queueName);
			channel.queueBind("myQueue", EXCHANGE_NAME, "SyncVM");
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume("myQueue", true, consumer);
			System.out.println("Begin listen to RabbitMQ on " + AMPQHostIP + ":" + Port);
			if (this.getProcessMsg() == null) {
				System.out.println("Is null");
			} else {
				System.out.println("Not null");
			}
//			while (true) {
//				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
//				String message = new String(delivery.getBody());
//				System.out.println("Receive new Message: " + message);
//				this.getProcessMsg().ProcessSync(message);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
