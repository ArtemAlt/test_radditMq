import com.rabbitmq.client.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ITConsumer {
    private static final String EXCHANGE_NAME = "NEWS-CHANNEL";
    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("123456");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            String queueName = channel.queueDeclare().getQueue();
            System.out.println("My queue name: " + queueName);
            System.out.println("Welcome to command line 'IT BLOG NEWS ' ");
            System.out.println("Enter your subscription. Example - set_topic php");
            Scanner in = new Scanner(System.in);
            String topic;
            do {
                String inputCommand = in.nextLine();
                String[] separateInput = inputCommand.split(" ");
                topic = separateInput[1];
                channel.queueBind(queueName, EXCHANGE_NAME, topic);
                System.out.println(" [*] Waiting for messages from channel - "+topic);
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    System.out.println(" [x] Received '" + message + "'");
                };
                channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
                });
            } while (!topic.equals("exit"));
        }
    }
}
